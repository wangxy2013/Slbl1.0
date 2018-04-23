package com.twlrg.slbl.json;


import com.twlrg.slbl.entity.CommentInfo;
import com.twlrg.slbl.entity.OrderInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CommentListHandler extends JsonHandler
{
    private List<CommentInfo> commentInfoList = new ArrayList<>();

    public List<CommentInfo> getCommentInfoList()
    {
        return commentInfoList;
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
                    CommentInfo mCommentInfo = new CommentInfo(arr.optJSONObject(i));
                    commentInfoList.add(mCommentInfo);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
