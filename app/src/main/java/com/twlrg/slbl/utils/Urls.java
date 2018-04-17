package com.twlrg.slbl.utils;

/**
 * URL管理类
 *
 * @date 2014年9月16日 上午9:48:03
 * @since[产品/模块版本]
 * @seejlj
 */
public class Urls
{
    public static final String HTTP_IP = "http://www.shanglvbuluo.com";

    public static final String BASE_URL = HTTP_IP + "/api/";


    public static String getImgUrl(String url)
    {
        return HTTP_IP + url;
    }

    //用戶登录
    public static String getLoginUrl()
    {
        return BASE_URL + "clientLogin.do";
    }


    //首页酒店列表
    public static String getHotelListUrl()
    {
        return BASE_URL + "merchant/search";
    }


    public static String getVersionUrl()
    {
        return BASE_URL + "merchant/search";
    }
}
