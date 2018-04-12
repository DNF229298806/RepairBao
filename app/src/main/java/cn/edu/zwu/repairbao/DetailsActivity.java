package cn.edu.zwu.repairbao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.ExecutionException;

import cn.edu.zwu.repairbao.util.PictureUtil;

/**
 * 有一个bug就是当用户手动删除图片以后 无法点击查看图片 想要重新刷新图片 只能重新create这个Activity
 * 要么就把判断文件这个地方也写在点击事件里面
 * 2018年4月12日14:29:52
 * 待完成的方法：抢单按钮 退单按钮 checkIntent()中的对未完成的订单和已完成的订单的逻辑处理(详情见该方法的注释)
 */
public class DetailsActivity extends AppCompatActivity {

    private ImageSlideshow imageSlideshow;

    private TextView Phone_Details_tv;

    private Button bt_Call_User;

    private Button bt_Rob_Order_Details;

    private Button bt_Back_Order_Details;

    private String[] imageUrls = {"http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg",
            "http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg",
            "http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg",
            "http://pic1.zhimg.com/b6f59c017b43937bb85a81f9269b1ae8.jpg",
            "http://pic2.zhimg.com/a62f9985cae17fe535a99901db18eba9.jpg"};

    private Bitmap[] bitmap = new Bitmap[imageUrls.length];

    private File[] files = new File[imageUrls.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //初始化控件
        initControl();
        //对控件进行监听事件的注册
        setListeners();
        //检查对方传过来的intent携带的type值来决定按钮和图片的可见与不可见
        checkIntent();
        // 初始化imageSlideshow数据
        initData();
        // 为ImageSlideshow设置数据
        imageSlideshow.setDotSpace(20);     //设置小圆点的间距
        imageSlideshow.setDotSize(20);      //设置小圆点的大小
        imageSlideshow.setDelay(5000);      //设置播放的间隔时间

        imageSlideshow.commit();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        /*String[] imageUrls = {"http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg",
                "http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg",
                "http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg",
                "http://pic1.zhimg.com/b6f59c017b43937bb85a81f9269b1ae8.jpg",
                "http://pic2.zhimg.com/a62f9985cae17fe535a99901db18eba9.jpg"};*/
        String[] titles = {"读读日报 24 小时热门 TOP 5 · 余文乐和「香港贾玲」乌龙绯闻",
                "写给产品 / 市场 / 运营的数据抓取黑科技教程",
                "学做这些冰冰凉凉的下酒宵夜，简单又方便",
                "知乎好问题 · 有什么冷门、小众的爱好？",
                "欧洲都这么发达了，怎么人均收入还比美国低"};
        for (int i = 0; i < 5; i++) {
            imageSlideshow.addImageTitle(imageUrls[i], titles[i]);
        }
    }

    @Override
    protected void onDestroy() {
        // 释放imageSlideshow资源
        imageSlideshow.releaseResource();
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    private void initControl() {
        imageSlideshow = (ImageSlideshow) findViewById(R.id.is_gallery);
        Phone_Details_tv = (TextView) findViewById(R.id.phone_details_tv);
        bt_Call_User = (Button) findViewById(R.id.bt_call_user);
        bt_Rob_Order_Details = (Button) findViewById(R.id.bt_rob_order_details);
        bt_Back_Order_Details = (Button) findViewById(R.id.bt_back_order_details);
    }

    /**
     * 注册监听器
     */
    private void setListeners() {
        //直接调用系统的拨号界面
        bt_Call_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Phone_Details_tv.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        bt_Rob_Order_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //抢单逻辑
            }
        });

        bt_Back_Order_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退单逻辑
            }
        });
        imageSlideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //ImageView im = (ImageView) findViewById(R.id.test);
                switch (position) {
                    case 0:
                        Toast.makeText(DetailsActivity.this, "点击了第一张图", Toast.LENGTH_SHORT).show();
                        PictureUtil.seePicture(files[0], DetailsActivity.this);
                        //im.setImageBitmap(bitmap[0]);
                        break;
                    case 1:
                        Toast.makeText(DetailsActivity.this, "点击了第二张图", Toast.LENGTH_SHORT).show();
                        PictureUtil.seePicture(files[1], DetailsActivity.this);
                        //im.setImageBitmap(bitmap[1]);
                        break;
                    case 2:
                        Toast.makeText(DetailsActivity.this, "点击了第三张图", Toast.LENGTH_SHORT).show();
                        PictureUtil.seePicture(files[2], DetailsActivity.this);
                        //im.setImageBitmap(bitmap[2]);
                        break;
                    case 3:
                        Toast.makeText(DetailsActivity.this, "点击了第四张图", Toast.LENGTH_SHORT).show();
                        PictureUtil.seePicture(files[3], DetailsActivity.this);
                        //im.setImageBitmap(bitmap[3]);
                        break;
                    case 4:
                        Toast.makeText(DetailsActivity.this, "点击了第五张图", Toast.LENGTH_SHORT).show();
                        PictureUtil.seePicture(files[4], DetailsActivity.this);
                        //im.setImageBitmap(bitmap[4]);
                        break;
                }
            }
        });
    }

    /**
     * 检查意图并调用载入对应的图片
     */
    public void checkIntent() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        //查看订单 退单按钮不可见 载入图片
        if (type.equals("no_rob_order")) {
            imageSlideshow.setVisibility(View.VISIBLE);
            bt_Call_User.setVisibility(View.VISIBLE);
            bt_Rob_Order_Details.setVisibility(View.VISIBLE);
            bt_Back_Order_Details.setVisibility(View.GONE);
            loadPicture();
        }
        //else if 是未完成的订单过来的抢单不可见不载入图片  elseif是完成订单过来的 全部不可见不载入图片
    }

    /**
     * 载入报修图片并把该图片保存到本地/storage/emulated/0/Android/data/cn.edu.zwu.repairbao/cache下
     * 等等换成使用经纬度加上数字来作为图片的命名
     * 开启一个子线程 在子线程中完成耗时操作载入图片 保存图片
     */
    public void loadPicture() {
        //检查SD卡的挂载状态 如果没有挂载直接return
        if (!checkSDCardState()) {
            return;
        }
        final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cn.edu.zwu.repairbao/images/";
        System.out.println("博客：" + dir);
        //开始加载图片并把图片加载到本地  当然第一步需要检查图片是否已经在本地
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //如果文件目录不存在 那么就去创建这个目录
                    PictureUtil.createDir(dir);
                    for (int i = 0; i < bitmap.length; i++) {
                        //开始生成图片的文件名 使用经纬度生成
                        String fileName_String = "111.555,111.666宁波";
                        //通过i的编号来生成数字文件名
                        int fileName_Int = i;
                        String fileName = fileName_String + fileName_Int;
                        //通过输出流写出到SD卡上
                        files[i] = new File(dir + fileName + ".jpg");
                        //如果文件已经存在那么什么都不做
                        if (files[i].exists()) {
                            System.out.println("图片已经存在了你知道么 瓜皮");
                        } else {
                            //生成bitmap图片
                            bitmap[i] = Glide.with(DetailsActivity.this).load(imageUrls[i]).asBitmap().centerCrop().into(800, 600).get();
                            //把图片保存到本地
                            PictureUtil.savePicture(files[i], bitmap[i]);
                            //保存图片后发送广播通知更新数据库
                            PictureUtil.flushGallery(files[i], DetailsActivity.this);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取内部储存状态 如果没有挂载SD卡返回false
     */
    public boolean checkSDCardState() {
        //获取内部存储状态
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(DetailsActivity.this, "你没有挂载SD卡无法使用本功能！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
