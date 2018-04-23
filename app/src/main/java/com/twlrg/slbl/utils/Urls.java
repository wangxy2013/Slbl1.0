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

    //获取版本信息
    public static String getVersionUrl()
    {
        return BASE_URL + "merchant/search";
    }

    public static String getImgUrl(String url)
    {
        return HTTP_IP + url;
    }

    //用戶登录
    public static String getLoginUrl()
    {
        return BASE_URL + "user/login";
    }


    //首页酒店列表
    public static String getHotelListUrl()
    {
        return BASE_URL + "merchant/search";
    }

    //酒店详细
    public static String getHotelDetailUrl()
    {
        return BASE_URL + "merchant/detail";
    }

    //用户注册
    public static String getRegisterUrl()
    {
        return BASE_URL + "user/register";
    }

    //获取用户信息
    public static String getUserInfoUrl()
    {
        return BASE_URL + "user/user_info";
    }

    //获取短信验证码
    public static String getVerifycodeUrl()
    {
        return BASE_URL + "user/verifycode";
    }

    //修改密码
    public static String getUpdatePwdUrl()
    {
        return BASE_URL + "user/update_pwd";
    }

    //找回密码
    public static String getForgetPwdUrl()
    {
        return BASE_URL + "user/set_pwd";
    }


    //修改用户信息
    public static String getUpdateUserInfoUrl()
    {
        return BASE_URL + "user/update_userinfo";
    }



    //获取订单列表
    public static String getOrderListUrl()
    {
        return BASE_URL + "order/list_info";
    }

    //获取订单详情
    public static String getOrderDetailUrl()
    {
        return BASE_URL + "order/order_detail";
    }

    //取消订单申请
    public static String getOrderCancelUrl()
    {
        return BASE_URL + "order/order_cancel";
    }








}
