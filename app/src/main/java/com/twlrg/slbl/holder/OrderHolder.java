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
import com.twlrg.slbl.entity.OrderInfo;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;


/**
 * Date:
 */
public class OrderHolder extends RecyclerView.ViewHolder
{


    private AutoFitTextView mHotelNameTv;
    private TextView        mPriceTv;
    private TextView        mTitleTv;
    private TextView        mNamelTv;
    private TextView        mCreateTimeTv;

    private LinearLayout mItemLayout;

    private MyItemClickListener listener1;

    public OrderHolder(View rootView, MyItemClickListener listener1)
    {
        super(rootView);
        mHotelNameTv = (AutoFitTextView) rootView.findViewById(R.id.tv_merchant);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_total_fee);
        mTitleTv = (TextView) rootView.findViewById(R.id.tv_title);
        mNamelTv = (TextView) rootView.findViewById(R.id.tv_name);
        mCreateTimeTv = (TextView) rootView.findViewById(R.id.tv_create_time);
        mItemLayout = (LinearLayout) rootView.findViewById(R.id.ll_item);
        this.listener1 = listener1;
    }


    public void setOrderInfo(OrderInfo mOrderInfo, Context mContext, final int p)
    {
        mHotelNameTv.setText(mOrderInfo.getMerchant());
        mPriceTv.setText("￥"+mOrderInfo.getTotal_fee());
        mTitleTv.setText(mOrderInfo.getTitle() +"  "+mOrderInfo.getBuynum() +"间  " + mOrderInfo.getDays() +"晚");
        mNamelTv.setText(mOrderInfo.getName() +" " +mOrderInfo.getCheck_in()+"入住" );
        mCreateTimeTv.setText(mOrderInfo.getCreate_time());

        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener1.onItemClick(v, p);
            }
        });
    }

}
