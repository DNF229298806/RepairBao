package cn.edu.zwu.repairbao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lixiaohui8636.widget.ClauseView;
import com.wyp.avatarstudio.AvatarStudio;

import java.io.File;

import cn.edu.zwu.repairbao.Gson.Engineer;
import cn.edu.zwu.repairbao.Interface.ActivityInitControl;
import cn.edu.zwu.repairbao.Util.JsonUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 使用glide-transformations 和 me.thewyp:avatar 框架实现背景图和圆形头像
 * 并实现上传头像，未实现的是上传照片文件到服务器的功能
 * 实现了读取json进行界面初始化的功能 但是没有完成的是 如果更新数据以后是不是要重新请求json串啊 而且是不是要写个if判断啊更新以后就
 * 不用去得到engineer了啊？
 * 2018年4月20日11:10:58 已经完成本地化储存 考虑加密问题
 */
public class MyInfoActivity extends AppCompatActivity implements ActivityInitControl, View.OnClickListener {

    public static final String MODIFY_SPECIALTY = "modify_specialty";
    public static final String MODIFY_INTRODUCE = "modify_introduce";
    public static final String MODIFY_PWD = "modify_pwd";
    private Button bt_myinfo_back;              //返回按钮
    private ImageView h_back;                   //背景图（毛玻璃）
    private ImageView h_head;                   //圆形头像
    private TextView tv_user_name;              //圆形头像下的用户名
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

        //得到MainActivity2传过来的engineer 开始初始化界面把json中的信息填入控件
        //Engineer engineer = (Engineer) getIntent().getSerializableExtra("engineer");
        String json = JsonUtil.getJson(MyInfoActivity.this);
        Engineer engineer = JsonUtil.handleEngineerResponse(json);
        Log.d("MyInfoActivity", "engineer " + engineer);
        tv_user_name.setText(engineer.engineerData.name);
        cv_my_grade.setRemark(engineer.engineerData.grade);
        cv_my_receive_number.setRemark(engineer.engineerData.receiveNumber);
        cv_my_end_number.setRemark(engineer.engineerData.endNumber);
        cv_my_back_number.setRemark(engineer.engineerData.backNumber);
        cv_my_specialty.setRemark(engineer.engineerData.introduce);
        //个人介绍？json不存在的 修改头像的图片路径？json 不存在的
        //cv_my_introduce.setRemark(engineer.engineerData);
        //cv_my_head.setRemark(engineer.engineerData);
        cv_my_phone.setRemark(engineer.engineerData.phoneNumber);
        //cv_my_password.setRemark(engineer.engineerData);
        cv_my_idcard.setRemark(engineer.engineerData.idCard);
    }


    public static void actionStart(Context context, String specialty, String introduce, String pwd) {
        Intent intent = new Intent(context, MyInfoActivity.class);
        intent.putExtra("specialty", specialty);
        intent.putExtra("introduce", introduce);
        intent.putExtra("pwd", pwd);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        //了解一下flag
        context.startActivity(intent);
    }

    //对intent携带的值进行判断 如果是null 应该对null值进行处理 嗯就是这样

    @Override
    public void initControl() {
        bt_myinfo_back = (Button) findViewById(R.id.bt_myinfo_back);
        h_back = (ImageView) findViewById(R.id.h_back);
        h_head = (ImageView) findViewById(R.id.h_head);
        tv_user_name = (TextView) findViewById(R.id.user_name);
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
        View[] arr_Control = {cv_my_grade, cv_my_receive_number, cv_my_end_number,
                cv_my_back_number, cv_my_specialty, cv_my_introduce, cv_my_head,
                cv_my_phone, cv_my_password, cv_my_idcard, bt_myinfo_back};
        for (int i = 0; i < arr_Control.length; i++) {
            arr_Control[i].setOnClickListener(this);
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
                MyInfoItemActivity.actionStart(MyInfoActivity.this, MyInfoActivity.MODIFY_SPECIALTY);
                break;

            case R.id.cv_my_introduce:
                Toast.makeText(MyInfoActivity.this, "个人介绍", Toast.LENGTH_SHORT).show();
                MyInfoItemActivity.actionStart(MyInfoActivity.this, MyInfoActivity.MODIFY_INTRODUCE);
                break;

            case R.id.cv_my_head:
                Toast.makeText(MyInfoActivity.this, "修改头像", Toast.LENGTH_SHORT).show();
                new AvatarStudio.Builder(MyInfoActivity.this)
                        .needCrop(true)//是否裁剪，默认裁剪
                        .setTextColor(Color.BLACK)
                        .dimEnabled(true)//背景是否dim 默认true
                        .setAspect(1, 1)//裁剪比例 默认1：1
                        .setOutput(200, 200)//裁剪大小 默认200*200
                        .setText("打开相机", "从相册中选取", "取消")
                        .show(new AvatarStudio.CallBack() {
                            @Override
                            public void callback(String uri) {

                                //得到这个file对象  以后要上传到服务器的
                                File file = new File(uri);

                                //uri为图片路径
                                Context context = MyInfoActivity.this;
                                //毛玻璃效果
                                Glide.with(context).load(file)
                                        .bitmapTransform(new BlurTransformation(context, 25), new CenterCrop(context))
                                        .into(h_back);

                                //圆角头像 到时候还要加入默认的圆角头像的 比如丑丑的未上传头像
                                Glide.with(context).load(new File(uri))
                                        .bitmapTransform(new CropCircleTransformation(context))
                                        .into(h_head);
                            }
                        });
                break;

            case R.id.cv_my_phone:
                Toast.makeText(MyInfoActivity.this, "修改绑定手机", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_my_password:
                Toast.makeText(MyInfoActivity.this, "修改密码", Toast.LENGTH_SHORT).show();
                MyInfoItemActivity.actionStart(MyInfoActivity.this, MyInfoActivity.MODIFY_PWD);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            String type = data.getStringExtra("type");
            String data_data = data.getStringExtra("data");
            if (type.equals(MODIFY_SPECIALTY)) {
                cv_my_specialty.setRemark(data_data);
            }
        }
    }
}
