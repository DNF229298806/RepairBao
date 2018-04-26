package cn.edu.zwu.repairbao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.edu.zwu.repairbao.Gson.Engineer;
import cn.edu.zwu.repairbao.Interface.ActivityInitControl;
import cn.edu.zwu.repairbao.MyUI.DeleteEditText;
import cn.edu.zwu.repairbao.Util.JsonUtil;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 2018年4月17日14:58:08
 * 明天回来写fdvc和监听器 溜了溜了
 * 2018年4月18日11:20:56 实际上功能并没有实现 修改功能 主要是还要测试连接服务器
 * 还有就是修改头像部分 具体考虑要不要做
 * 2018年4月18日16:23:09 修改头像已经实现
 * 2018年4月20日13:44:23 修改密码保存到本地已经实现
 * 还没有完成上传服务器的功能
 */
public class MyInfoItemActivity extends AppCompatActivity implements ActivityInitControl, View.OnClickListener {

    private TextView tv_myinfo_item;
    private Button bt_myinfo_item_back;
    private LinearLayout ll_modify_specialty;
    private RadioButton rb_electric_appliance;
    private RadioButton rb_electric_kitchen;
    private RadioButton rb_electronics;
    private RadioButton rb_others;
    private LinearLayout ll_modify_introduce;
    private EditText et_myinfo_item_introduce;
    private LinearLayout ll_modify_pwd;
    private DeleteEditText det_myinfo_item_old_pwd;
    private DeleteEditText det_myinfo_item_new_pwd;
    private DeleteEditText det_myinfo_item_new_pwd_again;
    private Button bt_myinfo_item_save;
    private String type = null;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_item);
        //初始化控件fdvc
        initControl();
        //为控件注册监听器
        setListeners();
        //检查从别的Activity发送过来的意图
        checkIntent();
    }

    @Override
    public void initControl() {
        json = JsonUtil.getJson(MyInfoItemActivity.this);
        tv_myinfo_item = (TextView) findViewById(R.id.tv_myinfo_item);
        bt_myinfo_item_back = (Button) findViewById(R.id.bt_myinfo_item_back);
        ll_modify_specialty = (LinearLayout) findViewById(R.id.ll_modify_specialty);
        rb_electric_appliance = (RadioButton) findViewById(R.id.rb_electric_appliance);
        rb_electric_kitchen = (RadioButton) findViewById(R.id.rb_electric_kitchen);
        rb_electronics = (RadioButton) findViewById(R.id.rb_electronics);
        rb_others = (RadioButton) findViewById(R.id.rb_others);
        ll_modify_introduce = (LinearLayout) findViewById(R.id.ll_modify_introduce);
        et_myinfo_item_introduce = (EditText) findViewById(R.id.et_myinfo_item_introduce);
        ll_modify_pwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        det_myinfo_item_old_pwd = (DeleteEditText) findViewById(R.id.det_myinfo_item_old_pwd);
        det_myinfo_item_new_pwd = (DeleteEditText) findViewById(R.id.det_myinfo_item_new_pwd);
        det_myinfo_item_new_pwd_again = (DeleteEditText) findViewById(R.id.det_myinfo_item_new_pwd_again);
        bt_myinfo_item_save = (Button) findViewById(R.id.bt_myinfo_item_save);
        //进行载入的初始化 选中 与json中相同的按钮
        RadioButton rb[] = {rb_electric_appliance, rb_electric_kitchen, rb_electronics, rb_others};
        String json = JsonUtil.getJson(MyInfoItemActivity.this);
        if (json == null) {
            //发起http请求 重新获取JSON 然后进行解析
            Toast.makeText(this, "发生未知错误，请重启程序", Toast.LENGTH_SHORT).show();
        } else {
            Engineer engineer = JsonUtil.handleEngineerResponse(json);
            for (int i = 0; i < rb.length; i++) {
                if (rb[i].getText().equals(engineer.engineerData.introduce)) {
                    rb[i].setChecked(true);
                }
            }
        }
    }

    @Override
    public void setListeners() {
        bt_myinfo_item_back.setOnClickListener(this);
        bt_myinfo_item_save.setOnClickListener(this);
    }

    //重写onclick方法 写成用bt_myinfo_item_save来表示的 麻烦的一比 又要判断intent
    //要增加post部分 把修改的数据传到服务器上 增加提示toast
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_myinfo_item_save:
                if (type.equals(MyInfoActivity.MODIFY_SPECIALTY)) {
                    modify_specialty();
                } else if (type.equals(MyInfoActivity.MODIFY_INTRODUCE)) {
                    modify_introduce();
                } else if (type.equals(MyInfoActivity.MODIFY_PWD)) {
                    modify_pwd();
                }
                break;

            case R.id.bt_myinfo_item_back:
                finish();
                break;

            default:
                break;
        }
    }

    public void checkIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals(MyInfoActivity.MODIFY_SPECIALTY)) {
            setControlVisibility(View.VISIBLE, View.GONE, View.GONE, "特长专修");
        } else if (type.equals(MyInfoActivity.MODIFY_INTRODUCE)) {
            setControlVisibility(View.GONE, View.VISIBLE, View.GONE, "个人介绍");
        } else if (type.equals(MyInfoActivity.MODIFY_PWD)) {
            setControlVisibility(View.GONE, View.GONE, View.VISIBLE, "修改密码");
        }
    }

    public static void actionStart(Activity activity, String type) {
        Intent intent = new Intent(activity, MyInfoItemActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, 1);
    }

    public void setControlVisibility(int ll_modify_specialty_visibility, int ll_modify_introduce_visibility,
                                     int ll_modify_pwd_visibility, String tv_myinfo_item_text) {
        ll_modify_specialty.setVisibility(ll_modify_specialty_visibility);
        ll_modify_introduce.setVisibility(ll_modify_introduce_visibility);
        ll_modify_pwd.setVisibility(ll_modify_pwd_visibility);
        tv_myinfo_item.setText(tv_myinfo_item_text);
    }

    public void setResultData(String type, String data) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("data", data);
        setResult(4, intent);
        finish();
    }

    public void modify_specialty() {
        //应该发起网络请求 传送到服务器上进行保存 然后从服务器再读取到本地
        String temp_checked = null;
        if (rb_electric_appliance.isChecked()) {
            temp_checked = rb_electric_appliance.getText().toString();
        } else if (rb_electric_kitchen.isChecked()) {
            temp_checked = rb_electric_kitchen.getText().toString();
        } else if (rb_electronics.isChecked()) {
            temp_checked = rb_electronics.getText().toString();
        } else if (rb_others.isChecked()) {
            temp_checked = rb_others.getText().toString();
        }
        //MyInfoActivity.actionStart(MyInfoItemActivity.this, temp_checked, null, null);
        setResultData(type, temp_checked);
        String json = JsonUtil.getJson(MyInfoItemActivity.this);
        if (json == null) {
            //发起http请求 重新获取JSON 然后进行解析
            Toast.makeText(this, "发生未知错误，请重启程序", Toast.LENGTH_SHORT).show();
        } else {
            //json不为空 开始解析json串 转化为GSON
            Engineer engineer = JsonUtil.handleEngineerResponse(json);
            //写入per本地文件并上传
            engineer.engineerData.introduce = temp_checked;
            JsonUtil.saveJson(MyInfoItemActivity.this, new Gson().toJson(engineer));
        }
        Toast.makeText(MyInfoItemActivity.this, temp_checked, Toast.LENGTH_SHORT).show();
    }

    public void modify_introduce() {
        //MyInfoActivity.actionStart(MyInfoItemActivity.this, null, et_myinfo_item_introduce.getText().toString(), null);
        setResultData(type, et_myinfo_item_introduce.getText().toString());
        Toast.makeText(MyInfoItemActivity.this, et_myinfo_item_introduce.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void modify_pwd() {
        //密码这一块还没有调用setResultData  ！！！
        String old_pwd = det_myinfo_item_old_pwd.getText().toString();
        String new_pwd = det_myinfo_item_new_pwd.getText().toString();
        final String new_pwd_again = det_myinfo_item_new_pwd_again.getText().toString();
        //发起网络请求 要求和服务器保存的密码进行比对
        //if 密码一样 接着判断两次输入的新密码是不是一样 如果密码一样 弹出选择框 选择框点击确定以后返回MyInfoActivity并提示修改成功！
        String json = JsonUtil.getJson(MyInfoItemActivity.this);
        if (json == null) {
            //发起http请求 重新获取JSON 然后进行解析
            Toast.makeText(this, "发生未知错误，请重启程序", Toast.LENGTH_SHORT).show();
        } else {
            //json不为空 开始解析json串 转化为GSON
            final Engineer engineer = JsonUtil.handleEngineerResponse(json);
            //当输入的旧密码和工程师密码不一致时
            if (old_pwd.equals("") | new_pwd.equals("") | new_pwd_again.equals("")) {
                Toast.makeText(MyInfoItemActivity.this, "任何输入框都不能为空！", Toast.LENGTH_SHORT).show();
            } else if (!old_pwd.equals(engineer.engineerData.password)) {
                Toast.makeText(this, "旧密码与原密码不一致！请检查重试！", Toast.LENGTH_LONG).show();
                //清空输入框
                det_myinfo_item_old_pwd.setText("");
                det_myinfo_item_new_pwd.setText("");
                det_myinfo_item_new_pwd_again.setText("");
                //获得焦点
                det_myinfo_item_old_pwd.setFocusable(true);
                det_myinfo_item_old_pwd.setFocusableInTouchMode(true);
                det_myinfo_item_old_pwd.requestFocus();
            } else {
                if (!new_pwd.equals(new_pwd_again)) {
                    Toast.makeText(this, "两次输入的新密码不一致，请检查重试！", Toast.LENGTH_SHORT).show();
                    //清空输入框
                    det_myinfo_item_new_pwd.setText("");
                    det_myinfo_item_new_pwd_again.setText("");
                    //获得焦点
                    det_myinfo_item_new_pwd.setFocusable(true);
                    det_myinfo_item_new_pwd.setFocusableInTouchMode(true);
                    det_myinfo_item_new_pwd.requestFocus();
                } else {
                    PromptDialog promptDialog = new PromptDialog(MyInfoItemActivity.this);
                    promptDialog.showWarnAlert("你确定要修改密码？", new PromptButton("取消", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            Toast.makeText(MyInfoItemActivity.this, button.getText(), Toast.LENGTH_SHORT).show();
                        }
                    }), new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton promptButton) {
                            //写入per本地文件并上传
                            engineer.engineerData.password = new_pwd_again;
                            JsonUtil.saveJson(MyInfoItemActivity.this, new Gson().toJson(engineer));
                            //MyInfoActivity.actionStart(MyInfoItemActivity.this, null, null, "123456");
                            finish();
                        }
                    }));
                }
            }
        }
    }

}
