package com.twlrg.slbl.json;



import com.twlrg.slbl.entity.VersionInfo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 作者：王先云 on 2016/11/1 13:48
 * 邮箱：wangxianyun1@163.com
 * 描述：版本更新
 */
public class VersionInfoHandler extends JsonHandler
{

    private VersionInfo mVersionInfo;

    public VersionInfo getVersionInfo()
    {
        return mVersionInfo;
    }

    @Override
    protected void parseJson(JSONObject obj) throws Exception
    {

        JSONObject jsonObject = obj.optJSONObject("data");


        try
        {
            mVersionInfo = new VersionInfo(jsonObject);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
