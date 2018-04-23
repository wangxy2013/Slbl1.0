package com.twlrg.slbl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.CommentInfo;
import com.twlrg.slbl.holder.CommentHolder;
import com.twlrg.slbl.holder.FacilitiesHolder;

import java.util.List;

/**
 */
public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesHolder>
{

    private List<String>      list;

    public FacilitiesAdapter(List<String> list)
    {
        this.list = list;
    }

    @Override
    public FacilitiesHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facilities, parent, false);
        FacilitiesHolder mHolder = new FacilitiesHolder(itemView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(FacilitiesHolder holder, int position)
    {
        holder.setFacilities(list.get(position));
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
