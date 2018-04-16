package com.twlrg.slbl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.FilterInfo;

import java.util.ArrayList;
import java.util.List;


public class FilterAdapter extends BaseAdapter
{
    private List<FilterInfo> infos = new ArrayList<>();

    private  Context mContext;
    public FilterAdapter(List<FilterInfo> infos, Context mContext)
    {
        this.infos = infos;
        this.mContext = mContext;
    }

    @Override
    public int getCount()
    {
        return infos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pop_list, null);
            holder = new ViewHolder();
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        FilterInfo mQQInfo = infos.get(position);
        holder.mNameTv.setText(mQQInfo.getTitle());

        return convertView;
    }




    private class ViewHolder
    {
        TextView mNameTv;
    }
}
