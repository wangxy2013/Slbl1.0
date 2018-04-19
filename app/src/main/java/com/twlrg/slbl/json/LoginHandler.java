package com.twlrg.slbl.json;


import com.twlrg.slbl.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2016/9/7 13:48
 * 邮箱：wangxianyun1@163.com
 * 描述：用户登录
 */
public class LoginHandler extends JsonHandler
{


    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONObject obj = jsonObj.optJSONObject("data");
            ConfigManager.instance().setUserId(obj.optString("uid"));
            ConfigManager.instance().setUserName(obj.optString("name"));
            ConfigManager.instance().setToken(obj.optString("token"));
            ConfigManager.instance().setUserNickName(obj.optString("nickname"));
            ConfigManager.instance().setMobile(obj.optString("mobile"));
            ConfigManager.instance().setUserPic(obj.optString("portrait"));
            ConfigManager.instance().setUserSex(obj.optInt("sex"));
            ConfigManager.instance().setUserEmail(obj.optString("email"));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
