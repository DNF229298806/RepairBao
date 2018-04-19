package cn.edu.zwu.repairbao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import cn.edu.zwu.repairbao.gson.Engineer;
import cn.edu.zwu.repairbao.util.HttpUtil;
import cn.edu.zwu.repairbao.util.JsonUtil;
import cn.edu.zwu.repairbao.util.LoginUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpPostRequest("http://www.wanlixueyuan.com/engineer/findInfoByPhoneNumber.action",
                        LoginUtil.getResponseBody("1"), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println("连接失败");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                Engineer engineer = JsonUtil.handleEngineerResponse(response.body().string());
                                System.out.println("success:" + engineer.success);
                                System.out.println("message:" + engineer.message);
                                System.out.println("data:" + engineer.engineerData);
                                System.out.println("id:" + engineer.engineerData.id);
                                System.out.println("phoneNumber:" + engineer.engineerData.phoneNumber);
                                System.out.println("password" + engineer.engineerData.password);
                                System.out.println("name:" + engineer.engineerData.name);
                                System.out.println("wechat:" + engineer.engineerData.wechat);
                                System.out.println("specialty:" + engineer.engineerData.specialty);
                                System.out.println("introduce:" + engineer.engineerData.introduce);
                                System.out.println("idCard:" + engineer.engineerData.idCard);
                                System.out.println("grade:" + engineer.engineerData.grade);
                                System.out.println("receiveNumber:" + engineer.engineerData.receiveNumber);
                                System.out.println("endNumber:" + engineer.engineerData.endNumber);
                                System.out.println("backNumber:" + engineer.engineerData.backNumber);
                                System.out.println("status:" + engineer.engineerData.status);
                            }
                        });
            }
        });
    }

}
