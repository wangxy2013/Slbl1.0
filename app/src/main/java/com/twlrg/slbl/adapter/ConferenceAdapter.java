package com.twlrg.slbl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.ConferenceInfo;
import com.twlrg.slbl.entity.HotelInfo;
import com.twlrg.slbl.holder.ConferenceHolder;
import com.twlrg.slbl.holder.HotelHolder;
import com.twlrg.slbl.listener.MyItemClickListener;

import java.util.List;

/**
 * 会议室
 */
public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceHolder>
{

    private List<ConferenceInfo> list;
    private Context              mContext;
    private MyItemClickListener  listener;

    public ConferenceAdapter(List<ConferenceInfo> list, Context mContext)
    {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ConferenceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conference, parent, false);
        ConferenceHolder mHolder = new ConferenceHolder(itemView);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(ConferenceHolder holder, int position)
    {
        ConferenceInfo mHotelInfo = list.get(position);
        holder.setConferenceInfo(mHotelInfo, mContext);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
