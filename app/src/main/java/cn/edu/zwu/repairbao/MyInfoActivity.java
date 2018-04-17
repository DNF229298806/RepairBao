package cn.edu.zwu.repairbao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lixiaohui8636.widget.ClauseView;

import cn.edu.zwu.repairbao.Interface.ActivityInitControl;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyInfoActivity extends AppCompatActivity implements ActivityInitControl, View.OnClickListener {

    private Button bt_myinfo_back;              //返回按钮
    private ImageView h_back;                   //背景图（毛玻璃）
    private ImageView h_head;                   //圆形头像
    private ClauseView cv_my_grade;             //我的星级
    private ClauseView cv_my_receive_number;    //接单数
    private ClauseView cv_my_end_number;        //结单数
    private ClauseView cv_my_back_number;       //退单数
    private ClauseView cv_my_specialty;         //专业特长
    private ClauseView cv_my_introduce;         //个人介绍
    private ClauseView cv_my_head;              //修改头像
    private ClauseView cv_my_phone;             //手机号码
    private ClauseView cv_my_password;          //修改密码
    private ClauseView cv_my_idcard;            //身份证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        //初始化控件
        initControl();
        //设置监听器
        setListeners();
        //毛玻璃效果
        Glide.with(this).load(R.drawable.default_head)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(h_back);

        //圆角头像 到时候还要加入默认的圆角头像的 比如丑丑的未上传头像
        Glide.with(this).load(R.drawable.default_head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(h_head);
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initControl() {
        bt_myinfo_back = (Button) findViewById(R.id.bt_myinfo_back);
        h_back = (ImageView) findViewById(R.id.h_back);
        h_head = (ImageView) findViewById(R.id.h_head);
        cv_my_grade = (ClauseView) findViewById(R.id.cv_my_grade);
        cv_my_receive_number = (ClauseView) findViewById(R.id.cv_my_receive_number);
        cv_my_end_number = (ClauseView) findViewById(R.id.cv_my_end_number);
        cv_my_back_number = (ClauseView) findViewById(R.id.cv_my_back_number);
        cv_my_specialty = (ClauseView) findViewById(R.id.cv_my_specialty);
        cv_my_introduce = (ClauseView) findViewById(R.id.cv_my_introduce);
        cv_my_head = (ClauseView) findViewById(R.id.cv_my_head);
        cv_my_phone = (ClauseView) findViewById(R.id.cv_my_phone);
        cv_my_password = (ClauseView) findViewById(R.id.cv_my_password);
        cv_my_idcard = (ClauseView) findViewById(R.id.cv_my_idcard);
    }

    @Override
    public void setListeners() {
        ClauseView[] arr_CV = {cv_my_grade, cv_my_receive_number, cv_my_end_number,
                cv_my_back_number, cv_my_specialty, cv_my_introduce, cv_my_head,
                cv_my_phone, cv_my_password, cv_my_idcard};
        for (int i = 0; i < arr_CV.length; i++) {
            arr_CV[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_my_grade:
                Toast.makeText(MyInfoActivity.this, "我的星级", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_receive_number:
                Toast.makeText(MyInfoActivity.this, "接单数", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_end_number:
                Toast.makeText(MyInfoActivity.this, "结单数", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_back_number:
                Toast.makeText(MyInfoActivity.this, "退单数", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_specialty:
                Toast.makeText(MyInfoActivity.this, "特长专修", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyInfoActivity.this, MyInfoItemActivity.class);
                startActivity(intent);
                break;

            case R.id.cv_my_introduce:
                Toast.makeText(MyInfoActivity.this, "个人介绍", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_head:
                Toast.makeText(MyInfoActivity.this, "修改头像", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_phone:
                Toast.makeText(MyInfoActivity.this, "修改绑定手机", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_password:
                Toast.makeText(MyInfoActivity.this, "修改密码", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_idcard:
                Toast.makeText(MyInfoActivity.this, "身份证", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_myinfo_back:
                finish();
                break;

            default:
                break;
        }
    }
}
