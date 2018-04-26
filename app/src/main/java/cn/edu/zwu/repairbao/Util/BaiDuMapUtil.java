package cn.edu.zwu.repairbao.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.edu.zwu.repairbao.BNDemoGuideActivity;
import cn.edu.zwu.repairbao.BNEventHandler;
import cn.edu.zwu.repairbao.Bean.MarkInfo;
import cn.edu.zwu.repairbao.Impl.OnMapClickImpl;
import cn.edu.zwu.repairbao.Impl.OnMarkerClickImpl;
import cn.edu.zwu.repairbao.Listener.MyOrientationListener;
import cn.edu.zwu.repairbao.MyApplication;
import cn.edu.zwu.repairbao.R;

import static cn.edu.zwu.repairbao.R.drawable.mark;
import static com.baidu.navisdk.adapter.PackageUtil.getSdcardDir;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/10
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class BaiDuMapUtil {

    public static List<Activity> activityList = new LinkedList<Activity>();
    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    private float myCurrentX;

    private String[] types = {"普通地图", "卫星地图", "热力地图(已关闭)"};

    private MyLocationListener myListener = new MyLocationListener();

    public MyOrientationListener myOrientationListener;

    private Activity activity;

    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo-RB";
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";
    private LatLng destLocationData = null;
    private LatLng myLocationData = null;
    public BaiDuMapUtil(BaiduMap baiduMap, Activity activity) {
        this.baiduMap = baiduMap;
        this.activity = activity;
    }

    /**
     * 初始化地图以及地图的设置
     */
    public void initLocation(LocationClient locationClient) {
        locationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(3000);
        //每3秒更新一下当前的位置
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

        useLocationOrientationListener();
        //调用方向传感器
        locationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        //初始化导航代码
        if (initDirs()) {
            initNavi();
        }

        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(activity,"设置目的地成功",Toast.LENGTH_SHORT).show();
                destLocationData = latLng;
                addDestInfoOverlay(latLng);
            }
        });
    }

    /**
     * 长按添加目的地
     * @param destInfo
     */
    private void addDestInfoOverlay(LatLng destInfo){
        baiduMap.clear();
        OverlayOptions options = new MarkerOptions().position(destInfo)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_tips))
                .zIndex(5);
        baiduMap.addOverlay(options);
    }
    /**
     * @author wyb
     * 定位结合方向传感器，从而可以实时监测到X轴坐标的变化，从而就可以检测到
     * 定位图标方向变化，只需要将这个动态变化的X轴的坐标更新myCurrentX值，
     * 最后在MyLocationData data.driection(myCurrentX);
     */
    private void useLocationOrientationListener() {
        myOrientationListener = new MyOrientationListener(MyApplication.getContext());
        myOrientationListener.setMyOrientationListener(new MyOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {//监听方向的改变，方向改变时，需要得到地图上方向图标的位置
                myCurrentX = x;
                System.out.println("方向:x---->" + x);
            }
        });
    }


    /**
     * 获取位置信息的客户端对象的监听器类MyLocationListener
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (isFirstLocate) {//表示用户第一次打开，就定位到用户当前位置，即此时只要将地图的中心点设置为用户此时的位置即可
                getMyLatestLocation(latitude, longitude);//获得最新定位的位置,并且地图的中心点设置为我的位置
                isFirstLocate = false;//表示第一次才会去定位到中心点
                final String locationTextString = "" + location.getAddrStr();//这里得到地址必须需要在设置LocationOption的时候需要设置isNeedAddress为true;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView mylocation_text = (TextView) activity.findViewById(R.id.mylocation_text);
                        mylocation_text.setText(locationTextString);
                    }
                });
            }
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//精度半径
                    .direction(myCurrentX)//方向
                    .latitude(location.getLatitude())//经度
                    .longitude(location.getLongitude())//纬度
                    .build();
            baiduMap.setMyLocationData(data);
            //配置自定义的定位图标,需要在紧接着setMyLocationData后面设置
            //调用自定义定位图标
            //toast("经度："+latitude+"     纬度:"+longtitude);
            myLocationData = new LatLng(data.latitude,data.longitude);
        }
    }


    /**
     * @author wyb
     * 获得最新定位的位置,并且地图的中心点设置为我的位置
     */
    private void getMyLatestLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);//创建一个经纬度对象，需要传入当前的经度和纬度两个参数
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 15.0f);
        ;//创建一个地图最新更新的状态对象，需要传入一个最新经纬度对象
        baiduMap.animateMapStatus(msu);//表示使用动画的效果传入，通过传入一个地图更新状态对象，然后利用百度地图对象来展现和还原那个地图更新状态，即此时的地图显示就为你现在的位置
    }

    /**
     * 初始化自定义定位模式、定位图标、精度圈颜色
     */
    public void initMyLocationConfiguration() {
        //初始化自定义定位模式、定位图标、精度圈颜色
        //定位跟随态FOLLOWING
        //默认为 LocationMode.NORMAL 普通态NORMAL
        //定位罗盘态COMPASS
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;  //定位罗盘态
        //支持自定义定位图标样式，替换定位icon
        //BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc);
        /*int accuracyCircleFillColor = 0xAAFFFF88;//自定义精度圈填充颜色
        int accuracyCircleStrokeColor = 0xAA00FF00;//自定义精度圈边框颜色
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, null,
                accuracyCircleFillColor, accuracyCircleStrokeColor));*/
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, null));
    }

    /**
     * @author wyb
     * 修改默认百度地图的View
     */
    public void changeDefaultBaiduMapView(MapView bdMapView) {
        //设置隐藏缩放和扩大的百度地图的默认的比例按钮
        bdMapView.showZoomControls(false);
        bdMapView.showScaleControl(false);
        bdMapView.removeViewAt(1);//最后移除默认百度地图的logo View
    }

    /**
     * @author wyb
     * 是否打开实时交通
     */
    public void switchRoadCondition(ImageView road_condition, Context context) {
        if (baiduMap.isTrafficEnabled()) {//如果是开着的状态，当点击后，就会出关闭状态
            baiduMap.setTrafficEnabled(false);
            Toast.makeText(context, "实时交通已关闭", Toast.LENGTH_SHORT).show();
            road_condition.setImageResource(R.drawable.main_icon_roadcondition_off);
        } else {//如果是的关闭的状态，当点击后，就会处于开启的状态
            baiduMap.setTrafficEnabled(true);
            Toast.makeText(context, "实时交通已开启", Toast.LENGTH_SHORT).show();
            road_condition.setImageResource(R.drawable.main_icon_roadcondition_on);
        }
    }

    /**
     * @param context 传入调用该方法的上下文
     * @author wyb
     * 选择地图的类型
     */
    public void selectMapType(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                Toast.makeText(context, "热力地图已关闭", Toast.LENGTH_SHORT).show();
                                types[which] = "热力地图(已关闭)";
                            } else {
                                baiduMap.setBaiduHeatMapEnabled(true);
                                Toast.makeText(context, "热力地图已打开", Toast.LENGTH_SHORT).show();
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
    public float narrowMapScale(float current) {
        current -= 0.5f;
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f + current);
        baiduMap.animateMapStatus(msu);
        return current;
    }

    /**
     * @author wyb
     * 缩小地图的比例
     */
    public float expandMapScale(float current) {
        current += 0.5f;
        MapStatusUpdate msu2 = MapStatusUpdateFactory.zoomTo(15.0f + current);
        baiduMap.animateMapStatus(msu2);
        return current;
    }

    /**
     * @author Mikyou
     * 添加覆盖物
     */
    public void addMapMarks(List<MarkInfo> markInfoList) {
        //List<MarkInfo> markInfoList = initMarksData();
        baiduMap.clear();//先清除一下图层
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        BitmapDescriptor myMarks = BitmapDescriptorFactory.fromResource(mark);//引入自定义的覆盖物图标，将其转化成一个BitmapDescriptor对象
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
     * @author wyb
     * 初始化覆盖物信息数据
     */
    //32.079254, 118.787623
    public void initMarksData(OnMarkerClickImpl onMarkerClickImpl, OnMapClickImpl onMapClickImpl) {
        Log.d("initMarksData", "初始化开始调用 ");
        List<MarkInfo> markInfoList = new ArrayList<MarkInfo>();
        markInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型",
                "报价", "维修时间", "报修信息",
                "用户姓名", "手机", null, "已维修"));
        markInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑",
                "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志",
                "17899992222", null, "未维修"));
        // markInfoList.add(new MarkInfo(28.7487420000, 115.8748860000, R.drawable.pic1, "华东交通大学南区", "距离5米", 888));
        // markInfoList.add(new MarkInfo(28.7534890000, 115.8767960000, R.drawable.pic1, "华东交通大学北区", "距离10米", 188));
        baiduMap.setOnMarkerClickListener(onMarkerClickImpl);
        baiduMap.setOnMapClickListener(onMapClickImpl);
        Log.d("initMarksData", "初始化结束调用 ");
        for (int i = 0; i < markInfoList.size(); i++) {
            Log.d("initMarksData", markInfoList.get(i).getRepair_loc());
        }

        baiduMap.clear();//先清除一下图层
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        BitmapDescriptor myMarks = BitmapDescriptorFactory.fromResource(mark);//引入自定义的覆盖物图标，将其转化成一个BitmapDescriptor对象
        //遍历MarkInfo的List一个MarkInfo就是一个Mark
        for (int i = 0; i < markInfoList.size(); i++) {
            Log.d("initMarksData", markInfoList.get(i).getBreakdown_content());
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

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                activity.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(activity, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(activity, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(activity, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText(activity, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(activity, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null /*ttsHandler, ttsPlayStateListener  mTTSCallback*/);

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = activity.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "11064024");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    private BNRoutePlanNode.CoordinateType mCoordinateType = null;

    public void routeplanToNavi() {
        BNRoutePlanNode.CoordinateType cotype = BNRoutePlanNode.CoordinateType.GCJ02;
        if (!hasInitSuccess) {
            Toast.makeText(activity, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    activity.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(activity, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(myLocationData.longitude, myLocationData.latitude, "百度大厦", null, cotype);
        eNode = new BNRoutePlanNode(destLocationData.longitude, destLocationData.latitude, "目的地", null, cotype);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(activity, list, 1, true, new DemoRoutePlanListener(sNode),
                    eventListerner);
        }
    }

    private boolean hasCompletePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = activity.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, activity.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(activity, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            activity.startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(activity, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
}
