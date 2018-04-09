package cn.edu.zwu.repairbao;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button bt_Login;
    private Button bt_Forget_Pwd;
    private Button bt_Register;
    private DeleteEditText det_Username;
    private DeleteEditText det_Pwd;

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
        bt_Login = (Button) findViewById(R.id.bt_login);
        bt_Forget_Pwd = (Button) findViewById(R.id.bt_forget_pwd);
        bt_Register = (Button) findViewById(R.id.bt_register);
        det_Username = (DeleteEditText) findViewById(R.id.det_username);
        det_Pwd = (DeleteEditText) findViewById(R.id.det_pwd);

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
                }else{
                    //发送网络请求 如果返回正确 发送开启发送意图调用MapActivity 如果返回错误 那么显示用户名或密码错误
                }
            }
        });

        //处理忘记密码逻辑
        bt_Forget_Pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"忘记密码了",Toast.LENGTH_SHORT).show();
            }
        });

        //处理注册用户逻辑
        bt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"新用户注册",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
