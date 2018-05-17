package com.twlrg.slbl.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.imsdk.TIMConversationType;
import com.twlrg.slbl.MyApplication;
import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.LoginActivity;
import com.twlrg.slbl.activity.OrderDetailActivity;
import com.twlrg.slbl.entity.SaleInfo;
import com.twlrg.slbl.im.ui.ChatActivity;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.StringUtils;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;


/**
 * Date:
 */
public class HotelSaleHolder extends RecyclerView.ViewHolder
{
    private ImageView mUserPicIv;
    private TextView  mUserNameTv;
    private TextView  mPositionTv;
    private ImageView mPhoneIv;
    private ImageView mMessageIv;

    private Context mContext;

    public HotelSaleHolder(View rootView, Context mContext)
    {
        super(rootView);
        this.mContext = mContext;
        mUserPicIv = (ImageView) rootView.findViewById(R.id.iv_user_head);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_salesperson);
        mPositionTv = (TextView) rootView.findViewById(R.id.tv_position);
        mPhoneIv = (ImageView) rootView.findViewById(R.id.iv_phone);
        mMessageIv = (ImageView) rootView.findViewById(R.id.iv_message);
    }


    public void setSaleInfo(final SaleInfo mSaleInfo)
    {
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mSaleInfo.getPortrait()), mUserPicIv);
        mUserNameTv.setText(mSaleInfo.getNickname());
        mPositionTv.setText(mSaleInfo.getPosition());
        mPhoneIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mSaleInfo && !StringUtils.stringIsEmpty(mSaleInfo.getPhone()))
                {
                    APPUtils.callPhone(mContext, mSaleInfo.getPhone());
                }
                else
                {
                    ToastUtil.show(mContext, "暂时无法电话联系销售人员,请尝试其他方式");
                }
            }
        });


        mMessageIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MyApplication.getInstance().isLogin())
                {
                    ChatActivity.navToChat(mContext, "slbl_serve_" + mSaleInfo.getId(), TIMConversationType.C2C);
                }
                else
                {
                    LoginActivity.start(mContext, true);
                }
            }
        });
    }

}
