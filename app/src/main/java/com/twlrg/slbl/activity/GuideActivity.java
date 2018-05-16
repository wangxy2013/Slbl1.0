package com.twlrg.slbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twlrg.slbl.R;
import com.twlrg.slbl.adapter.ViewPagerAdapter;
import com.twlrg.slbl.utils.ConfigManager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/12 15:46
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener
{
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.btn_submit)
    Button    mSubmitBtn;


    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View>  views;
    // 引导图片资源
    private static final int[] pics = {
            R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3};


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_guide);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {

        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GuideActivity.this, WelComeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void initViewData()
    {
        views = new ArrayList<View>();
        // 实例化ViewPager
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);


        if (!ConfigManager.instance().getIsFristLogin())
        {
            startActivity(new Intent(GuideActivity.this, WelComeActivity.class));
            finish();
        }
        ConfigManager.instance().setIsFristLogin(false);


        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++)
        {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            //防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片资源
            iv.setImageResource(pics[i]);
            views.add(iv);
        }

        // 设置数据
        viewpager.setAdapter(vpAdapter);
        // 设置监听
        viewpager.addOnPageChangeListener(this);


    }

    @Override
    public void onClick(View v)
    {
        int position = (Integer) v.getTag();
        setCurView(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length)
        {
            return;
        }
        viewpager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        //判断是否是最后一页，若是则显示按钮

        if (position == pics.length - 1)
        {

            mSubmitBtn.setVisibility(View.VISIBLE);

        }
        else
        {

            mSubmitBtn.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
}
