package com.twlrg.slbl.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.twlrg.slbl.R;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.LogUtil;
import com.twlrg.slbl.widget.AutoFitTextView;


import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/25 16:30
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class LocationActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.bmapView)
    MapView         bmapView;
    BaiduMap mBaiduMap;
    private Marker     mMarkerA;
    private InfoWindow mInfoWindow;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);


    private String hotelName;
    private String lat, lng;

    @Override
    protected void initData()
    {
        hotelName = getIntent().getStringExtra("HOTEL_NAME");
        lat = getIntent().getStringExtra("LAT");
        lng = getIntent().getStringExtra("LNG");

        LogUtil.e("TAG", lat + "、" + lng);
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_location);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText(hotelName);
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));

        mBaiduMap = bmapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            public boolean onMarkerClick(final Marker marker)
            {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA)
                {
                    button.setText(hotelName);
                    button.setTextColor(Color.BLACK);
                    listener = new InfoWindow.OnInfoWindowClickListener()
                    {
                        public void onInfoWindowClick()
                        {
                            //                            LatLng ll = marker.getPosition();
                            //                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                            //                                    ll.longitude + 0.005);
                            //                            marker.setPosition(llNew);
                            //                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

    }

    public void initOverlay()
    {
        LatLng llA = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(9).draggable(true);
        //        if (animationBox.isChecked()) {
        //            // 掉下动画
        //            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        //        }
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(llA)
                //放大地图到20倍
                .zoom(16)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected void onDestroy()
    {
        // 退出时销毁定位
        // 关闭定位图层
        bmapView.onDestroy();
        bmapView = null;
        bdA.recycle();
        super.onDestroy();
    }

}
