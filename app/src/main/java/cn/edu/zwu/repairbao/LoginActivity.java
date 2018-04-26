package cn.edu.zwu.repairbao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.zwu.repairbao.Gson.Engineer;
import cn.edu.zwu.repairbao.Interface.ActivityInitControl;
import cn.edu.zwu.repairbao.MyUI.DeleteEditText;
import cn.edu.zwu.repairbao.Util.HttpUtil;
import cn.edu.zwu.repairbao.Util.JsonUtil;
import cn.edu.zwu.repairbao.Util.LoginUtil;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 2018年4月19日15:41:35
 * 登录功能完成 同时把engineer这个类传递到了Main2Activity中 记得要在Main中进行接收写getIntent
 * 再考虑要不要做数据的持久化 储存 比如数据库/xml保存
 * 明天过来写 写什么呢 写个人中心的刷新 毕竟得到了工程师的数据 总要刷新吧
 */
public class LoginActivity extends AppCompatActivity implements ActivityInitControl {

    public static final String ADDRESS = "http://www.wanlixueyuan.com/engineer/findInfoByPhoneNumber.action";
    private Button bt_Login;
    private Button bt_Forget_Pwd;
    private Button bt_Register;
    private DeleteEditText det_Username;
    private DeleteEditText det_Pwd;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当sdk版本大于5.0的时候可以实现状态栏透明的效果
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        //初始化控件
        initControl();
        //对控件进行监听
        setListeners();
        promptDialog = new PromptDialog(LoginActivity.this);
        //测试用例
        det_Username.setText("15604096714");
        det_Pwd.setText("123");
    }

    /**
     * 初始化控件
     */
    public void initControl() {
        bt_Login = (Button) findViewById(R.id.bt_login);
        bt_Forget_Pwd = (Button) findViewById(R.id.bt_forget_pwd);
        bt_Register = (Button) findViewById(R.id.bt_register);
        det_Username = (DeleteEditText) findViewById(R.id.det_username);
        det_Pwd = (DeleteEditText) findViewById(R.id.det_pwd);
    }

    /**
     * 设置按钮的监听事件
     */
    public void setListeners() {
        //处理登录业务逻辑
        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户名或者密码为空的这种情况
                if (det_Username.getText().length() == 0 || det_Pwd.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
                    det_Username.setFocusable(true);
                    return;
                }
                //正则验证手机号码
                String regEx = "^1(([3]|[5]|[7]|[8])[0-9]|[4][579])\\d{8}$";
                //编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(det_Username.getText());
                //字符串是否与正则表达式相配
                boolean rs = matcher.matches();
                //用户名为不合法的手机号这中情况
                if (!rs) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号码！长度为11位", Toast.LENGTH_SHORT).show();
                    //设置焦点到用户名这一格上
                    det_Username.setFocusable(true);
                    return;
                } else {
                    promptDialog.showLoading("正在登录");
                    String phone = det_Username.getText().toString();
                    System.out.println("phone" + phone);
                    final String pwd = det_Pwd.getText().toString();
                    System.out.println("pwd" + pwd);
                    //开启子线程进行网络请求
                    HttpUtil.sendOkHttpPostRequest(ADDRESS, LoginUtil.getResponseBody(phone), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    promptDialog.dismiss();
                                    promptDialog.showError("登录失败");
                                    Toast.makeText(LoginActivity.this, "请检查你的网络连接", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //连接失败 请连接网络
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //String json = response.body().string();
                            final String json = "{\n" +
                                    "    \"success\": true,\n" +
                                    "    \"message\": null,\n" +
                                    "    \"data\": {\n" +
                                    "        \"id\": 1011,\n" +
                                    "        \"phoneNumber\": \"15604096714\",\n" +
                                    "        \"password\": \"123\",\n" +
                                    "        \"name\": \"容楠\",\n" +
                                    "        \"wechat\": \"15604096714\",\n" +
                                    "        \"specialty\": 6,\n" +
                                    "        \"introduce\": \"手机维修\",\n" +
                                    "        \"idCard\": \"110428199102162786\",\n" +
                                    "        \"grade\": 1.5,\n" +
                                    "        \"receiveNumber\": 842,\n" +
                                    "        \"endNumber\": 212,\n" +
                                    "        \"backNumber\": 630,\n" +
                                    "        \"status\": 9\n" +
                                    "    }\n" +
                                    "}";
                            System.out.println(json);
                            if (json.contains("html")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "服务器出错了，请等待工程师修复", Toast.LENGTH_SHORT).show();
                                        promptDialog.dismiss();
                                    }
                                });
                            } else {
                                final Engineer engineer = JsonUtil.handleEngineerResponse(json);
                                if (engineer.success.equals("false")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            promptDialog.dismiss();
                                            promptDialog.showError("登录失败");
                                            Toast.makeText(LoginActivity.this, "该号码有误或该号码还未注册", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (engineer.success.equals("true")) {
                                    if (!pwd.equals(engineer.engineerData.password)) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                promptDialog.dismiss();
                                                promptDialog.showError("登录失败");
                                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                promptDialog.dismiss();
                                                promptDialog.showSuccess("登陆成功");
                                                //把这次请求的json串保存到本地
                                                JsonUtil.saveJson(LoginActivity.this,json);
                                                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                    //发送网络请求 如果返回正确 发送开启发送意图调用MapActivity 如果返回错误 那么显示用户名或密码错误
                }
            }
        });

        //处理忘记密码逻辑
        bt_Forget_Pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "忘记密码了", Toast.LENGTH_SHORT).show();
            }
        });

        //处理注册用户逻辑
        bt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "新用户注册", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
