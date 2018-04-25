package com.twlrg.slbl.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.CommentInfo;
import com.twlrg.slbl.entity.OrderInfo;
import com.twlrg.slbl.utils.Urls;


/**
 * Date:
 */
public class PriceDetailHolder extends RecyclerView.ViewHolder
{


    private TextView mDateTv;
    private TextView mPriceTv;


    public PriceDetailHolder(View rootView)
    {
        super(rootView);
        mDateTv = (TextView) rootView.findViewById(R.id.tv_date);
        mPriceTv = (TextView) rootView.findViewById(R.id.tv_price);

    }


    public void setOrderInfo(OrderInfo mOrderInfo)
    {
        mDateTv.setText(mOrderInfo.getDate());
        mPriceTv.setText(mOrderInfo.getPrice() + " x " + mOrderInfo.getBuynum());
    }

}
