package cn.edu.zwu.repairbao.impl;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.model.LatLng;

import cn.edu.zwu.repairbao.R;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/10
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class OnMapClickImpl implements BaiduMap.OnMapClickListener {
    /**
     * @author zhongqihong
     * 给整个地图添加的点击事件
     */

    private Activity mActivity;
    private BaiduMap mBaiduMap;
    public OnMapClickImpl(Activity activity,BaiduMap baiduMap) {
        mActivity = activity;
        mBaiduMap = baiduMap;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        //表示点击地图其他的地方使得覆盖物的详情介绍的布局隐藏，但是点击已显示的覆盖物详情布局上，则不会消失，因为在详情布局上添加了Clickable=true
        //由于事件的传播机制，因为点击事件首先会在覆盖物布局的父布局(map)中,由于map是可以点击的，map则会把点击事件给消费掉，如果加上Clickable=true表示点击事件由详情布局自己处理，不由map来消费
        LinearLayout markLayout = (LinearLayout) mActivity.findViewById(R.id.mark_layout);
        markLayout.setVisibility(View.GONE);
        mBaiduMap.hideInfoWindow();//隐藏InfoWindow
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }
}
