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
import com.twlrg.slbl.entity.ConferenceInfo;
import com.twlrg.slbl.entity.HotelInfo;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;


/**
 * Date:
 */
public class ConferenceHolder extends RecyclerView.ViewHolder
{
    private ImageView mRoomImgIv;


    private AutoFitTextView mtTitleTv;
    private TextView        mPriceTv;
    private TextView        mAreaTv;
    private TextView        mFloorTv;
    private TextView        mTheatreTv;
    private TextView        mDeskTv;
    private TextView        mBanquetTv;


    public ConferenceHolder(View rootView)
    {
        super(rootView);
        mtTitleTv = (AutoFitTextView) rootView.findViewById(R.id.tv_title);
        mRoomImgIv = (ImageView) rootView.findViewById(R.id.iv_room_img);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);
        mAreaTv = (TextView) rootView.findViewById(R.id.tv_area);
        mFloorTv = (TextView) rootView.findViewById(R.id.tv_floor);
        mTheatreTv = (TextView) rootView.findViewById(R.id.tv_theatre);
        mDeskTv = (TextView) rootView.findViewById(R.id.tv_desk);
        mBanquetTv = (TextView) rootView.findViewById(R.id.tv_banquet);
    }


    public void setConferenceInfo(ConferenceInfo mConference, Context mContext)
    {

        int width = APPUtils.getScreenWidth(mContext);
        int height = (int) (width * 0.66);
        mRoomImgIv.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mConference.getPic1()), mRoomImgIv);
        mtTitleTv.setText(mConference.getTitle());
        mPriceTv.setText("￥" + mConference.getPrice() + "起");
        mAreaTv.setText(mConference.getArea() + "㎡");
        mFloorTv.setText(mConference.getFloor() + "楼");
        mTheatreTv.setText("剧院" + mConference.getTheatre() + "人");
        mDeskTv.setText("课桌" + mConference.getDesk() + "人");
        mBanquetTv.setText("宴会" + mConference.getBanquet() + "人");
    }

}
