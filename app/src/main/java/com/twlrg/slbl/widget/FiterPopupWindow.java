package com.twlrg.slbl.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.twlrg.slbl.R;
import com.twlrg.slbl.utils.APPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件筛选
 */
public class FiterPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener
{
    View rootView;
    private ListView mQQListView;

    private Activity mContext;

    private QQAdapter mAdapter;

    private int mHeight;
    List<QQInfo> mQQList = new ArrayList<>();

    public FiterPopupWindow(Activity context)
    {
        super(context);
        this.mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.pop_list, null);
        setContentView(rootView);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
        initEvent();
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }


    private void initView()
    {
        mQQListView = (ListView) rootView.findViewById(R.id.lv_choice);

    }

    private void initEvent()
    {
        mQQListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                //调用QQ
//                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + mQQList.get(position).getQno();
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                dismiss();
            }
        });

        setOnDismissListener(this);
    }

    public void show(View view)
    {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - mHeight);

    }

    public void setData(List<QQInfo> list)
    {
        mQQList.clear();
        mQQList.addAll(list);
        mAdapter = new QQAdapter(mQQList, mContext);
        mQQListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mQQListView);
        mAdapter.notifyDataSetChanged();
    }

    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }

        int totalHeight = 0;


        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
        mHeight = params.height + 50;
    }

    @Override
    public void onDismiss()
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }
}
