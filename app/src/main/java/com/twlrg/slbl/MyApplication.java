package com.twlrg.slbl;

import android.app.Application;
import android.app.Service;
import android.os.StrictMode;
import android.os.Vibrator;


import com.baidu.mapapi.SDKInitializer;
import com.twlrg.slbl.im.TencentCloud;
import com.twlrg.slbl.service.LocationService;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.LogUtil;
import com.twlrg.slbl.utils.StringUtils;



/**
 * 作者：王先云 on 2016/8/5 14:46
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class MyApplication extends Application
{
    public         Vibrator        mVibrator;
    private static MyApplication   instance;

    public static MyApplication getInstance() {return instance;}


    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        APPUtils.configImageLoader(getApplicationContext());
        ConfigManager.instance().init(this);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        TencentCloud.init(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }


    public boolean isLogin()
    {
        if (StringUtils.stringIsEmpty(ConfigManager.instance().getUserID()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static MyApplication getContext(){
        return instance;
    }


}
