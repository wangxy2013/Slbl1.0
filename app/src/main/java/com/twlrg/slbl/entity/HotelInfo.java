package com.twlrg.slbl.entity;

import org.json.JSONObject;

/**
 * 作者：王先云 on 2018/4/17 14:34
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class HotelInfo
{
    private String id;//4,//酒店ID
    private String title;//"某某酒店1",//酒店名称
    private String jl;//12472.47,//当前位置距离酒店距离
    private double price;//200, //无早最低价格
    private String position_label;//"某某地铁/某某商圈", //位置标签
    private String service_label;//"前台热情/会议酒店" //特色服务标签
    private String lat;
    private String lng;
    private String hotel_img;
    private double star;

    public HotelInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.title = obj.optString("title");
        this.jl = obj.optString("jl");
        this.price = obj.optDouble("price", 0);
        this.position_label = obj.optString("position_label");
        this.service_label = obj.optString("service_label");
        this.lng = obj.optString("lng");
        this.lat = obj.optString("lat");
        this.hotel_img = obj.optString("hotel_img");
        this.star = obj.optDouble("star", 5);

    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getJl()
    {
        return jl;
    }

    public void setJl(String jl)
    {
        this.jl = jl;
    }


    public String getPosition_label()
    {
        return position_label;
    }

    public void setPosition_label(String position_label)
    {
        this.position_label = position_label;
    }

    public String getService_label()
    {
        return service_label;
    }

    public void setService_label(String service_label)
    {
        this.service_label = service_label;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getHotel_img()
    {
        return hotel_img;
    }

    public void setHotel_img(String hotel_img)
    {
        this.hotel_img = hotel_img;
    }

    public double getStar()
    {
        return star;
    }

    public void setStar(double star)
    {
        this.star = star;
    }
}
