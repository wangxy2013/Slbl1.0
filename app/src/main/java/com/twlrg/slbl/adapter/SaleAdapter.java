package com.twlrg.slbl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.CommentInfo;
import com.twlrg.slbl.entity.SaleInfo;
import com.twlrg.slbl.holder.CommentHolder;
import com.twlrg.slbl.holder.SaleHolder;
import com.twlrg.slbl.listener.MyItemClickListener;

import java.util.List;

/**
 */
public class SaleAdapter extends RecyclerView.Adapter<SaleHolder>
{

    private MyItemClickListener listener;
    private List<SaleInfo>      list;

    public SaleAdapter(List<SaleInfo> list, MyItemClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public SaleHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        SaleHolder mHolder = new SaleHolder(itemView, listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(SaleHolder holder, int position)
    {
        SaleInfo mSaleInfo = list.get(position);
        holder.setSaleInfo(mSaleInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}
