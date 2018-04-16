package cn.edu.zwu.repairbao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lixiaohui8636.widget.ClauseView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        ClauseView c = (com.lixiaohui8636.widget.ClauseView)findViewById(R.id.cv_my_phone);
        ImageView h_back = (ImageView) findViewById(R.id.h_back);
        ImageView h_head = (ImageView) findViewById(R.id.h_head);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyInfoActivity.this,"日你哥",Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(this).load(R.drawable.shuaige)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(h_back);

        Glide.with(this).load(R.drawable.shuaige)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(h_head);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyInfoActivity.class);
        context.startActivity(intent);
    }
}
