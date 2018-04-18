package com.twlrg.slbl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.HotelInfo;
import com.twlrg.slbl.entity.RoomInfo;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;


/**
 * Date:
 */
public class RoomHolder extends RecyclerView.ViewHolder
{
    private ImageView mRoomImgIv;


    private AutoFitTextView mTitleNameTv;
    private TextView        mPriceTv;
    private TextView        mAreaTv;
    private TextView        mBedTypeTv;
    private TextView        mReserveTv;

    private MyItemClickListener listener1;

    public RoomHolder(View rootView, MyItemClickListener listener1)
    {
        super(rootView);
        mTitleNameTv = (AutoFitTextView) rootView.findViewById(R.id.tv_title);
        mRoomImgIv = (ImageView) rootView.findViewById(R.id.iv_room_img);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);
        mAreaTv = (TextView) rootView.findViewById(R.id.tv_area);
        mBedTypeTv = (TextView) rootView.findViewById(R.id.tv_bed_type);
        mReserveTv = (TextView) rootView.findViewById(R.id.tv_reserve);

        this.listener1 = listener1;
    }


    public void setRoomInfo(RoomInfo mRoomInfo, Context mContext, final int p)
    {

        int width = APPUtils.getScreenWidth(mContext);
        int height = (int) (width * 0.66);
        mRoomImgIv.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mRoomInfo.getPic1()), mRoomImgIv);
        mTitleNameTv.setText(mRoomInfo.getTitle());
        mPriceTv.setText("￥" + mRoomInfo.getPrice() + "起");

        String smokeless = mRoomInfo.getSmokeless();
        String wifi = mRoomInfo.getWifi();
        String window = mRoomInfo.getWindow();
        String price_type = mRoomInfo.getPrice_type();

        String mSmokeless = "有烟房";
        String mWifi = "有WIFI";
        String mWindow = "有窗";
        String zc = "无早餐";

        if (!"1".equals(smokeless))
        {
            mSmokeless = "无烟房";
        }
        if (!"1".equals(wifi))
        {
            mWifi = "无WIFI";
        }

        if (!"1".equals(window))
        {
            mWindow = "无窗";
        }
        if ("wz".equals(price_type))
        {
            zc = "无早餐";
        }
        else if ("dz".equals(price_type))
        {
            zc = "单早餐";
        }
        else if ("sz".equals(price_type))
        {
            zc = "双早餐";
        }

        mAreaTv.setText(mRoomInfo.getArea() + "/" + mRoomInfo.getFloor() + "/" + mWindow + "/" + mSmokeless);
        mBedTypeTv.setText(mRoomInfo.getBed_type() + "  " + zc + "  " + mWifi);

        mReserveTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onItemClick(v, p);
            }
        });
    }

}
