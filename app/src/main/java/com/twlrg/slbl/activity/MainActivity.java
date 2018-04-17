package com.twlrg.slbl.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.fragment.HomeFragment;
import com.twlrg.slbl.fragment.MessageFragment;
import com.twlrg.slbl.fragment.OrderFragment;
import com.twlrg.slbl.fragment.UserCenterFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity
{


    @BindView(android.R.id.tabhost)
    FragmentTabHost fragmentTabHost;


    private String texts[]         = {"首页", "消息", "订单", "我的"};
    private int    imageButton[]   = {
            R.drawable.ic_home_selector, R.drawable.ic_message_selector,
            R.drawable.ic_order_selector, R.drawable.ic_user_center_selector};
    private Class  fragmentArray[] = {HomeFragment.class, MessageFragment.class, OrderFragment.class, UserCenterFragment.class};

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);
        setStatusColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
        fragmentTabHost.setup(this, getSupportFragmentManager(),
                R.id.main_layout);

        for (int i = 0; i < texts.length; i++)
        {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArray[i], null);

            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            // fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.main_tab_selector);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(R.color.transparent);
    }

    private View getView(int i)
    {
        //取得布局实例
        View view = View.inflate(MainActivity.this, R.layout.tabcontent, null);
        //取得布局对象
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);

        //设置图标
        imageView.setImageResource(imageButton[i]);
        //设置标题
        textView.setText(texts[i]);
        return view;
    }

    public void changeTab(int index)
    {
        fragmentTabHost.setCurrentTab(index);

        switch (index)
        {
            case 0:
                setStatusColor(ContextCompat.getColor(this, R.color.black));
                break;
        }


    }
}
