package com.twlrg.slbl.json;


import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.StringUtils;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2016/9/7 13:48
 * 邮箱：wangxianyun1@163.com
 * 描述：用户登录
 */
public class LoginHandler extends JsonHandler
{

    private String userId;

    public String getUserId()
    {
        return userId;
    }
    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {

            JSONObject obj = jsonObj.optJSONObject("data");
            userId = obj.optString("uid");
            //ConfigManager.instance().setUserId(obj.optString("uid"));
            ConfigManager.instance().setUserName(obj.optString("name"));
            ConfigManager.instance().setToken(obj.optString("token"));
            ConfigManager.instance().setUserNickName(obj.optString("nickname"));
            ConfigManager.instance().setMobile(obj.optString("mobile"));

            String portrait = obj.optString("portrait");
            if (StringUtils.stringIsEmpty(portrait))
            {
                portrait = portrait.replace("./", "/");
            }
            ConfigManager.instance().setUserPic(portrait);
            ConfigManager.instance().setUserSex(obj.optInt("sex"));
            ConfigManager.instance().setUserEmail(obj.optString("email"));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
