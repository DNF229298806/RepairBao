package cn.edu.zwu.repairbao.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import cn.edu.zwu.repairbao.R;
import cn.edu.zwu.repairbao.db.MarkInfo;
import cn.edu.zwu.repairbao.impl.OnMapClickImpl;
import cn.edu.zwu.repairbao.impl.OnMarkerClickImpl;

import static cn.edu.zwu.repairbao.R.drawable.mark;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/10
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class BaiDuMapUtil{

    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;

    private String[] types = {"普通地图", "卫星地图", "热力地图(已关闭)"};

    private MyLocationListener myListener = new MyLocationListener();

    public BaiDuMapUtil(BaiduMap baiduMap){
        this.baiduMap = baiduMap;
    }

    /**
     * 初始化地图以及地图的设置
     */
    public void initLocation(LocationClient locationClient) {
        locationClient.registerLocationListener(myListener);
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

        locationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    /**
     * 从北京天安门移动到用户当前经纬度的位置
     *
     * @param location 位置信息
     */
    public void navigateTo(BDLocation location) {
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
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
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
     * @author wyb
     * 选择地图的类型
     * @param context   传入调用该方法的上下文
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
        markInfoList.add(new MarkInfo(32.079254, 118.787623, "地点", "类型", "报价", "维修时间", "报修信息", "用户姓名", "手机", null));
        markInfoList.add(new MarkInfo(32.064355, 118.787624, "火星哈哈", "修电脑", "50万 ", "2018年4月10日-2018年4月13日", "电脑爆炸了呵呵呵", "石乐志", "17899992222", null));
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

}
