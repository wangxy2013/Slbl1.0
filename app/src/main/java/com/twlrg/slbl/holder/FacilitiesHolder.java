package com.twlrg.slbl.holder;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.AddCommentActivity;
import com.twlrg.slbl.entity.OrderInfo;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.widget.AutoFitTextView;

import java.security.acl.Group;


/**
 * Date:
 */
public class FacilitiesHolder extends RecyclerView.ViewHolder
{

    private TextView     mTitleTv;


    public FacilitiesHolder(View rootView)
    {
        super(rootView);
        mTitleTv = (TextView) rootView.findViewById(R.id.tv_title);
    }


    public void setFacilities(String name)
    {
        mTitleTv.setText(name);
    }

}
