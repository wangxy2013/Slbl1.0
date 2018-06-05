package com.twlrg.slbl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.ReplyInfo;
import com.twlrg.slbl.holder.ReplyHolder;

import java.util.List;

/**
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyHolder>
{

    private List<ReplyInfo> list;


    public ReplyAdapter(List<ReplyInfo> list)
    {
        this.list = list;
    }

    @Override
    public ReplyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
        ReplyHolder mHolder = new ReplyHolder(itemView,parent.getContext() );
        return mHolder;
    }


    @Override
    public void onBindViewHolder(ReplyHolder holder, int position)
    {
        ReplyInfo mReplyInfo = list.get(position);
        holder.setReplyInfo(mReplyInfo);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
