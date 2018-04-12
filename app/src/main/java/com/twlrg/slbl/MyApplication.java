package com.twlrg.slbl;

import android.app.Application;


import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：王先云 on 2016/8/5 14:46
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class MyApplication extends Application
{

    private static MyApplication instance;

    public static MyApplication getInstance() {return instance;}


    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        APPUtils.configImageLoader(getApplicationContext());
        ConfigManager.instance().init(this);


    }


}
