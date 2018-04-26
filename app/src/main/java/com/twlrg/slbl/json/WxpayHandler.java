package com.twlrg.slbl.json;


import com.twlrg.slbl.entity.WxPayInfo;
import com.twlrg.slbl.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2016/9/7 13:48
 * 邮箱：wangxianyun1@163.com
 */
public class WxpayHandler extends JsonHandler
{
    private WxPayInfo wxPayInfo;

    public WxPayInfo getWxPayInfo()
    {
        return wxPayInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONObject obj = jsonObj.optJSONObject("data");
            wxPayInfo = new WxPayInfo(obj);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
