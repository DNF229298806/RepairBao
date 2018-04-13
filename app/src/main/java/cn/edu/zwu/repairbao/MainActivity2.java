package cn.edu.zwu.repairbao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;

import java.util.List;

import cn.edu.zwu.repairbao.bean.MarkInfo;
import cn.edu.zwu.repairbao.impl.OnMapClickImpl;
import cn.edu.zwu.repairbao.impl.OnMarkerClickImpl;
import cn.edu.zwu.repairbao.util.BaiDuMapUtil;
import cn.edu.zwu.repairbao.util.PermissionUtil;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/10
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class MainActivity2 extends AppCompatActivity {

    public static final String NO_ROB_ORDER = "no_rob_order";

    public LocationClient mLocationClient;

    private TextView tv_Position;

    private MapView bdMapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    private ImageView road_condition = null;

    private ImageView map_type = null;

    private float current;//放大或缩小的比例系数

    private ImageView expandMap;//放大地图控件

    private ImageView narrowMap;//缩小地图

    private ImageView addMarks;//添加覆盖物控件

    private BitmapDescriptor myMarks;

    private List<MarkInfo> markInfoList;

    private LinearLayout markLayout;

    private String[] types = {"普通地图", "卫星地图", "热力地图(已关闭)"};

    private ImageButton ib_My_Info;

    private DrawerLayout dl_Drawer_Layout;

    private NavigationView nv_nav_View;

    private Button bt_Rob_Order;

    private Button bt_See_Details;

    private OnMapClickImpl mOnMapClickImpl;

    private OnMarkerClickImpl mOnMarkerClickImpl;

    private BaiDuMapUtil mBaiDuMapUtil;
    //权限数组  精确定位权限 读取手机状态权限 写入外部储存器(SD卡)的权限
    private String[] arr_permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        //初始化一定要在setContentView之前调用 不然会出错
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //初始化控件
        initControl();
        //注册监听器
        setListeners();
        //加入权限列表
        PermissionUtil.addPermission(this, arr_permission);
        //检查权限是否被授予 如果权限全部拿到了的话 开始定位
        if (PermissionUtil.checkPermission(this))
            requestLocation();//开始定位
    }

    /**
     * 初始化控件
     */
    private void initControl() {
        tv_Position = (TextView) findViewById(R.id.tv_position);
        bdMapView = (MapView) findViewById(R.id.bmapView);
        road_condition = (ImageView) findViewById(R.id.road_condition);
        map_type = (ImageView) findViewById(R.id.map_type);
        expandMap = (ImageView) findViewById(R.id.add_scale);
        narrowMap = (ImageView) findViewById(R.id.low_scale);
        addMarks = (ImageView) findViewById(R.id.map_marker);
        markLayout = (LinearLayout) findViewById(R.id.mark_layout);
        ib_My_Info = (ImageButton) findViewById(R.id.ib_my_info);
        dl_Drawer_Layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nv_nav_View = (NavigationView) findViewById(R.id.nv_side_bar);
        bt_Rob_Order = (Button) findViewById(R.id.bt_rob_order);
        bt_See_Details = (Button) findViewById(R.id.bt_see_details);
        baiduMap = bdMapView.getMap();  //得到百度地图实例
        baiduMap.setMyLocationEnabled(true);    //设置我的位置图层开启
        mOnMapClickImpl = new OnMapClickImpl(this, baiduMap);   //实例化对应的接口实现类
        mOnMarkerClickImpl = new OnMarkerClickImpl(this, baiduMap);
        mBaiDuMapUtil = new BaiDuMapUtil(baiduMap);             //实例化自己封装的工具类
        mBaiDuMapUtil.initLocation(mLocationClient);            //初始化地图以及地图的设置
    }

    /**
     * 注册监听
     */
    private void setListeners() {
        road_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiDuMapUtil.switchRoadCondition(road_condition, MainActivity2.this);
            }
        });
        map_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiDuMapUtil.selectMapType(MainActivity2.this);
            }
        });
        expandMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = mBaiDuMapUtil.expandMapScale(current);
                //System.out.println("加"+current);
            }
        });
        narrowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = mBaiDuMapUtil.narrowMapScale(current);
                //System.out.println("减"+current);
            }
        });
        ib_My_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_Drawer_Layout.openDrawer(GravityCompat.START);
                //System.out.println("111111111");
            }
        });
        nv_nav_View.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.it_nav_call == item.getItemId()) {
                    //跳转到对应列表
                    startActivity(new Intent(MainActivity2.this, OrderListActivity.class));
                    Toast.makeText(MainActivity2.this, "未完成订单", Toast.LENGTH_SHORT).show();
                } else if (R.id.it_nav_friends == item.getItemId()) {
                    startActivity(new Intent(MainActivity2.this, OrderListActivity.class));
                    Toast.makeText(MainActivity2.this, "完成订单", Toast.LENGTH_SHORT).show();
                } else if (R.id.it_nav_location == item.getItemId()) {
                    Toast.makeText(MainActivity2.this, "我的信息", Toast.LENGTH_SHORT).show();
                } else if (R.id.it_nav_mail == item.getItemId()) {
                    Toast.makeText(MainActivity2.this, "滚出去", Toast.LENGTH_SHORT).show();
                } else if (R.id.it_nav_task == item.getItemId()) {
                    Toast.makeText(MainActivity2.this, "嘿嘿嘿？？？！！！", Toast.LENGTH_SHORT).show();
                }
                dl_Drawer_Layout.closeDrawers();
                return true;
            }
        });

        bt_Rob_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行抢单
                Toast.makeText(MainActivity2.this, "抢到了哈哈哈", Toast.LENGTH_SHORT).show();
            }
        });

        bt_See_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity2.this, DetailsActivity.class);
                //intent.putExtra("type","no_rob_order");
                //看看要不要传数据过去
                //startActivity(intent);
                DetailsActivity.actionStart(MainActivity2.this, NO_ROB_ORDER);
                Toast.makeText(MainActivity2.this, "点击了查看详情", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 向百度公司请求定位
     */
    private void requestLocation() {
        mBaiDuMapUtil.initMyLocationConfiguration();
        //开始定位
        mLocationClient.start();
        //清除百度地图的logo和比例尺以及缩放尺
        mBaiDuMapUtil.changeDefaultBaiduMapView(bdMapView);
        mBaiDuMapUtil.initMarksData(mOnMarkerClickImpl, mOnMapClickImpl);
        //mBaiDuMapUtil.initMarksData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才可以使用本程序！", Toast.LENGTH_SHORT).show();
                            finish();       //如果有权限没有同意 那么直接销毁activity
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bdMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bdMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        bdMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
