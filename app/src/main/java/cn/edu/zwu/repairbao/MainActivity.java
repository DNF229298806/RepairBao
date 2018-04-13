package cn.edu.zwu.repairbao;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zwu.repairbao.bean.MarkInfo;

import static cn.edu.zwu.repairbao.R.drawable.mark;


public class MainActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener {

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

    private MyLocationListener myListener = new MyLocationListener();

    private ImageButton ib_My_Info;

    private DrawerLayout dl_Drawer_Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        //初始化一定要在setContentView之前调用 不然会出错
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
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
        NavigationView navView = (NavigationView) findViewById(R.id.nv_side_bar);
        road_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchRoadCondition();
            }
        });
        map_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMapType();
            }
        });
        expandMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandMapScale();
            }
        });
        narrowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                narrowMapScale();
            }
        });
        ib_My_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_Drawer_Layout.openDrawer(GravityCompat.START);
                System.out.println("111111111");
            }
        });
        navView.setCheckedItem(R.id.it_nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.it_nav_call == item.getItemId()) {
                    Toast.makeText(MainActivity.this,"未完成订单",Toast.LENGTH_SHORT).show();
                }else if (R.id.it_nav_friends==item.getItemId()){
                    Toast.makeText(MainActivity.this,"完成订单",Toast.LENGTH_SHORT).show();
                }else if (R.id.it_nav_location==item.getItemId()){
                    Toast.makeText(MainActivity.this,"我的信息",Toast.LENGTH_SHORT).show();
                }else if (R.id.it_nav_mail==item.getItemId()){
                    Toast.makeText(MainActivity.this,"滚出去",Toast.LENGTH_SHORT).show();
                }else if (R.id.it_nav_task==item.getItemId()){
                    Toast.makeText(MainActivity.this,"嘿嘿嘿？？？！！！",Toast.LENGTH_SHORT).show();
                }
                dl_Drawer_Layout.closeDrawers();
                return true;
            }
        });
        //得到baiduMap实例
        baiduMap = bdMapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        //无权限的时候去请求权限
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestLocation();
        }


    }


    /**
     * 向百度公司请求定位
     */
    private void requestLocation() {
        initLocation();
        initMyLocationConfiguration();
        //开始定位
        mLocationClient.start();
        //清除百度地图的logo和比例尺以及缩放尺
        changeDefaultBaiduMapView();
        addMapMarks();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        //每5秒更新一下当前的位置
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02

        option.setIsNeedAddress(true);
        //设置需要地址

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    /**
     * 初始化自定义定位模式、定位图标、精度圈颜色
     */
    private void initMyLocationConfiguration() {
        //初始化自定义定位模式、定位图标、精度圈颜色
        //定位跟随态FOLLOWING
        //默认为 LocationMode.NORMAL 普通态NORMAL
        //定位罗盘态COMPASS
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;  //定位罗盘态
        //支持自定义定位图标样式，替换定位icon
        //BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc);
        int accuracyCircleFillColor = 0xAAFFFF88;//自定义精度圈填充颜色
        int accuracyCircleStrokeColor = 0xAA00FF00;//自定义精度圈边框颜色
       /* baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker,
                accuracyCircleFillColor, accuracyCircleStrokeColor));*/
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, null,
                accuracyCircleFillColor, accuracyCircleStrokeColor));
    }

    /**
     * 从北京天安门移动到用户当前经纬度的位置
     *
     * @param location 位置信息
     */
    private void navigateTo(BDLocation location) {
        //如果是第一次定位的话 创建一个经纬度对象，传入用户当前的经纬度，设置当前的百度地图缩放级别为16
        //百度地图的缩放级别的取值为3-19 可以为
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //MapStatusUpdate updateLocation = MapStatusUpdateFactory.newLatLng(ll);
            //baiduMap.animateMapStatus(updateLocation);
            //MapStatusUpdate updateLevel = MapStatusUpdateFactory.zoomTo(18f);
            //baiduMap.animateMapStatus(updateLevel);
            MapStatusUpdate updateLocationAndLevel = MapStatusUpdateFactory.newLatLngZoom(ll, 15.0f);
            baiduMap.animateMapStatus(updateLocationAndLevel);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);

        //        tv_Position.setVisibility(View.VISIBLE);
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


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络定位");
            }
            tv_Position.setText(currentPosition);
            //s = currentPosition.toString();
            Log.d("呵呵", "草泥马");
            Log.d("傻大姐", "" + tv_Position.getText().toString());
            /*让地图移动到用户当前的经纬度上*/
            /*
            BaiduMap baiduMap = bdMapView.getMap();
            LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            */

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
    }

    /**
     * @author wyb
     * 修改默认百度地图的View
     */
    private void changeDefaultBaiduMapView() {
        //设置隐藏缩放和扩大的百度地图的默认的比例按钮
        bdMapView.showZoomControls(false);
        bdMapView.showScaleControl(false);
        bdMapView.removeViewAt(1);//最后移除默认百度地图的logo View
    }

    /**
     * @author wyb
     * 是否打开实时交通
     */
    private void switchRoadCondition() {
        if (baiduMap.isTrafficEnabled()) {//如果是开着的状态，当点击后，就会出关闭状态
            baiduMap.setTrafficEnabled(false);
            Toast.makeText(this, "实时交通已关闭", Toast.LENGTH_SHORT).show();
            road_condition.setImageResource(R.drawable.main_icon_roadcondition_off);
        } else {//如果是的关闭的状态，当点击后，就会处于开启的状态
            baiduMap.setTrafficEnabled(true);
            Toast.makeText(this, "实时交通已开启", Toast.LENGTH_SHORT).show();
            road_condition.setImageResource(R.drawable.main_icon_roadcondition_on);
        }
    }

    /**
     * @author wyb
     * 选择地图的类型
     */
    private void selectMapType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.icon_home_user_no_login)
                .setTitle("请选择地图的类型")
                .setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String select = types[which];
                        if (select.equals("普通地图")) {
                            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        } else if (select.equals("卫星地图")) {
                            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        } else if (select.equals("热力地图(已关闭)") || select.equals("热力地图(已打开)")) {
                            if (baiduMap.isBaiduHeatMapEnabled()) {
                                baiduMap.setBaiduHeatMapEnabled(false);
                                Toast.makeText(MainActivity.this, "热力地图已关闭", Toast.LENGTH_SHORT).show();
                                types[which] = "热力地图(已关闭)";
                            } else {
                                baiduMap.setBaiduHeatMapEnabled(true);
                                Toast.makeText(MainActivity.this, "热力地图已打开", Toast.LENGTH_SHORT).show();
                                types[which] = "热力地图(已打开)";
                            }
                        }
                    }
                }).show();
    }

    /**
     * @author wyb
     * 放大地图的比例
     */
    private void narrowMapScale() {
        current -= 0.5f;
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f + current);
        baiduMap.animateMapStatus(msu);
    }

    /**
     * @author wyb
     * 缩小地图的比例
     */
    private void expandMapScale() {
        current += 0.5f;
        MapStatusUpdate msu2 = MapStatusUpdateFactory.zoomTo(15.0f + current);
        baiduMap.animateMapStatus(msu2);
    }

    /**
     * @author Mikyou
     * 添加覆盖物
     */
    private void addMapMarks() {
        initMarksData();
        baiduMap.clear();//先清除一下图层
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        myMarks = BitmapDescriptorFactory.fromResource(mark);//引入自定义的覆盖物图标，将其转化成一个BitmapDescriptor对象
        //遍历MarkInfo的List一个MarkInfo就是一个Mark
        for (int i = 0; i < markInfoList.size(); i++) {
            //经纬度对象
            latLng = new LatLng(markInfoList.get(i).getLatitude(), markInfoList.get(i).getLongitude());//需要创建一个经纬对象，通过该对象就可以定位到处于地图上的某个具体点
            //图标
            options = new MarkerOptions().position(latLng).icon(myMarks).zIndex(6);
            marker = (Marker) baiduMap.addOverlay(options);//将覆盖物添加到地图上
            Bundle bundle = new Bundle();//创建一个Bundle对象将每个mark具体信息传过去，当点击该覆盖物图标的时候就会显示该覆盖物的详细信息
            bundle.putSerializable("mark", markInfoList.get(i));
            marker.setExtraInfo(bundle);
        }
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);//通过这个经纬度对象，地图就可以定位到该点
        baiduMap.animateMapStatus(msu);
    }

    /**
     * /**
     *
     * @author wyb
     * 初始化覆盖物信息数据
     */
    //32.079254, 118.787623
    private void initMarksData() {
        markInfoList = new ArrayList<MarkInfo>();
        markInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null,"已维修"));
        markInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null,"未维修"));
        // markInfoList.add(new MarkInfo(28.7487420000, 115.8748860000, R.drawable.pic1, "华东交通大学南区", "距离5米", 888));
        // markInfoList.add(new MarkInfo(28.7534890000, 115.8767960000, R.drawable.pic1, "华东交通大学北区", "距离10米", 188));
        baiduMap.setOnMarkerClickListener(this);
        baiduMap.setOnMapClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        MarkInfo MyMarker = (MarkInfo) bundle.getSerializable("mark");
        TextView tv_Repair_Loc_Content = (TextView) findViewById(R.id.tv_repair_loc_content);
        TextView tv_Repair_Type = (TextView) findViewById(R.id.tv_repair_type_content);
        TextView tv_User_Quote_Content = (TextView) findViewById(R.id.tv_user_quote_content);
        TextView tv_Repair_User_Time_Content = (TextView) findViewById(R.id.tv_repair_user_time_content);
        TextView tv_Breakdown_Content = (TextView) findViewById(R.id.tv_breakdown_content);
        tv_Repair_Loc_Content.setText(MyMarker.getRepair_loc());
        tv_Repair_Type.setText(MyMarker.getRepair_type());
        tv_User_Quote_Content.setText(MyMarker.getUser_quote());
        tv_Repair_User_Time_Content.setText(MyMarker.getUser_time());
        tv_Breakdown_Content.setText(MyMarker.getBreakdown_content());
        //ImageView iv = (ImageView) markLayout.findViewById(R.id.mark_image);
        //TextView distanceTv = (TextView) markLayout.findViewById(R.id.distance);
        //TextView nameTv = (TextView) markLayout.findViewById(R.id.name);
        //TextView zanNumsTv = (TextView) markLayout.findViewById(R.id.zan_nums);
        //iv.setImageResource(MyMarker.getimageId());
        //distanceTv.setText(MyMarker.getDistance() + "");
        //nameTv.setText(MyMarker.getName());
        //zanNumsTv.setText(MyMarker.getZanNum() + "");

        //初始化一个InfoWindow
        initInfoWindow(MyMarker, marker);
        markLayout.setVisibility(View.VISIBLE);
        return true;
    }

    /**
     * @author wyb
     * 初始化出一个InfoWindow
     */
    private void initInfoWindow(MarkInfo MyMarker, Marker marker) {
        // TODO Auto-generated method stub
        InfoWindow infoWindow;
        //InfoWindow中显示的View内容样式，显示一个TextView
        TextView infoWindowTv = new TextView(MainActivity.this);
        //infoWindowTv.setBackgroundResource(R.drawable.location_tips);
        infoWindowTv.setPadding(30, 20, 30, 50);
        infoWindowTv.setText(MyMarker.getRepair_loc());
        infoWindowTv.setTextColor(Color.parseColor("#000000"));
        infoWindowTv.setTextSize(15);
        final LatLng latLng = marker.getPosition();
        Point p = baiduMap.getProjection().toScreenLocation(latLng);//将地图上的经纬度转换成屏幕中实际的点
        p.y -= 47;//设置屏幕中点的Y轴坐标的偏移量
        LatLng ll = baiduMap.getProjection().fromScreenLocation(p);//把修改后的屏幕的点有转换成地图上的经纬度对象
        /**
         * @author mikyou
         * 实例化一个InfoWindow的对象
         * public InfoWindow(View view,LatLng position, int yOffset)通过传入的 view 构造一个 InfoWindow, 此时只是利用该view生成一个Bitmap绘制在地图中，监听事件由开发者实现。
         *  参数:
         * view - InfoWindow 展示的 view
         * position - InfoWindow 显示的地理位置
         * yOffset - InfoWindow Y 轴偏移量
         * */
        infoWindow = new InfoWindow(infoWindowTv, ll, 10);
        baiduMap.showInfoWindow(infoWindow);//显示InfoWindow
    }

    /**
     * @author zhongqihong
     * 给整个地图添加的点击事件
     */
    @Override
    public void onMapClick(LatLng arg0) {
        //表示点击地图其他的地方使得覆盖物的详情介绍的布局隐藏，但是点击已显示的覆盖物详情布局上，则不会消失，因为在详情布局上添加了Clickable=true
        //由于事件的传播机制，因为点击事件首先会在覆盖物布局的父布局(map)中,由于map是可以点击的，map则会把点击事件给消费掉，如果加上Clickable=true表示点击事件由详情布局自己处理，不由map来消费
        markLayout.setVisibility(View.GONE);
        baiduMap.hideInfoWindow();//隐藏InfoWindow
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
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
