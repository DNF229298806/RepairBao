package cn.edu.zwu.repairbao.impl;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

import cn.edu.zwu.repairbao.R;
import cn.edu.zwu.repairbao.bean.MarkInfo;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/10
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class OnMarkerClickImpl implements BaiduMap.OnMarkerClickListener {

    private Activity mActivity;
    private BaiduMap mBaiduMap;

    public OnMarkerClickImpl(Activity activity, BaiduMap baiduMap) {
        mActivity = activity;
        mBaiduMap = baiduMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        MarkInfo MyMarker = (MarkInfo) bundle.getSerializable("mark");
        TextView tv_Repair_Loc_Content = (TextView) mActivity.findViewById(R.id.tv_repair_loc_content);
        TextView tv_Repair_Type = (TextView) mActivity.findViewById(R.id.tv_repair_type_content);
        TextView tv_User_Quote_Content = (TextView) mActivity.findViewById(R.id.tv_user_quote_content);
        TextView tv_Repair_User_Time_Content = (TextView) mActivity.findViewById(R.id.tv_repair_user_time_content);
        TextView tv_Breakdown_Content = (TextView) mActivity.findViewById(R.id.tv_breakdown_content);
        tv_Repair_Loc_Content.setText(MyMarker.getRepair_loc());
        tv_Repair_Type.setText(MyMarker.getRepair_type());
        tv_User_Quote_Content.setText(MyMarker.getUser_quote());
        tv_Repair_User_Time_Content.setText(MyMarker.getUser_time());
        tv_Breakdown_Content.setText(MyMarker.getBreakdown_content());
        //初始化一个InfoWindow
        initInfoWindow(MyMarker, marker);
        LinearLayout markLayout = (LinearLayout) mActivity.findViewById(R.id.mark_layout);
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
        TextView infoWindowTv = new TextView(mActivity);
        //infoWindowTv.setBackgroundResource(R.drawable.location_tips);
        infoWindowTv.setPadding(30, 20, 30, 50);
        infoWindowTv.setText(MyMarker.getRepair_loc());
        infoWindowTv.setTextColor(Color.parseColor("#000000"));
        infoWindowTv.setTextSize(15);
        final LatLng latLng = marker.getPosition();
        Point p = mBaiduMap.getProjection().toScreenLocation(latLng);//将地图上的经纬度转换成屏幕中实际的点
        p.y -= 47;//设置屏幕中点的Y轴坐标的偏移量
        LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);//把修改后的屏幕的点有转换成地图上的经纬度对象
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
        mBaiduMap.showInfoWindow(infoWindow);//显示InfoWindow
    }
}
