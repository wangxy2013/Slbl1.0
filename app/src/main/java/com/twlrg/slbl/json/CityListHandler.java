package com.twlrg.slbl.json;


import com.twlrg.slbl.entity.CityInfo;
import com.twlrg.slbl.entity.CommentInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CityListHandler extends JsonHandler
{
    private List<CityInfo> cityInfoList = new ArrayList<>();

    public List<CityInfo> getCityInfoList()
    {
        return cityInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObj) throws Exception
    {
        try
        {
            JSONArray arr = jsonObj.optJSONArray("data");


            if (null != arr)
            {
                for (int i = 0; i < arr.length(); i++)
                {
                    CityInfo mCityInfo = new CityInfo(arr.optJSONObject(i));
                    cityInfoList.add(mCityInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
