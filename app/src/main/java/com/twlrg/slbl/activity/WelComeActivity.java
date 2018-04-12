package com.twlrg.slbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.twlrg.slbl.R;


/**
 * 作者：王先云 on 2018/4/12 16:37
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class WelComeActivity extends BaseActivity
{

    private int TIME = 10000;
    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_welcome);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
        //延迟进入
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                WelComeActivity.this.finish();
            }
        }, TIME);
    }
}
