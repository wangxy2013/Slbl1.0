package com.twlrg.slbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.slbl.R;
import com.twlrg.slbl.adapter.ConferenceAdapter;
import com.twlrg.slbl.adapter.RoomAdapter;
import com.twlrg.slbl.entity.ConferenceInfo;
import com.twlrg.slbl.entity.HotelInfo;
import com.twlrg.slbl.entity.RoomInfo;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.HotelDetailHandler;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;
import com.twlrg.slbl.widget.EmptyDecoration;
import com.twlrg.slbl.widget.FullyLinearLayoutManager;
import com.twlrg.slbl.widget.ObservableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/17 22:01
 * 邮箱：wangxianyun1@163.com
 * 描述：酒店详情
 */
public class HotelDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView            ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView      tvTitle;
    @BindView(R.id.iv_hotel_img)
    ImageView            ivHotelImg;
    @BindView(R.id.rb_star)
    RatingBar            rbStar;
    @BindView(R.id.tv_address)
    TextView             tvAddress;
    @BindView(R.id.tv_distance)
    TextView             tvDistance;
    @BindView(R.id.iv_location)
    ImageView            ivLocation;
    @BindView(R.id.tv_reviews_label)
    TextView             tvReviewsLabe;
    @BindView(R.id.tv_comment_count)
    TextView             tvCommentCount;
    @BindView(R.id.tv_start_date)
    TextView             tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView             tvEndDate;
    @BindView(R.id.tv_breakfast_type1)
    TextView             tvBreakfastType1;
    @BindView(R.id.tv_breakfast_type2)
    TextView             tvBreakfastType2;
    @BindView(R.id.tv_breakfast_type3)
    TextView             tvBreakfastType3;
    @BindView(R.id.tv_breakfast_type4)
    TextView             tvBreakfastType4;
    @BindView(R.id.ll_breakfast)
    LinearLayout         mBreakfastLayout;
    @BindView(R.id.rv_room)
    RecyclerView         rvRoom;
    @BindView(R.id.iv_room_more)
    ImageView            ivRoomMore;
    @BindView(R.id.ll_room_more)
    LinearLayout         llRoomMore;
    @BindView(R.id.rv_conference)
    RecyclerView         rvConference;
    @BindView(R.id.iv_conference_more)
    ImageView            ivConferenceMore;
    @BindView(R.id.ll_conference_more)
    LinearLayout         llConferenceMore;
    @BindView(R.id.tv_summary)
    TextView             tvSummary;
    @BindView(R.id.tv_summary_more)
    TextView             tvSummaryMore;
    @BindView(R.id.rl_policy)
    RelativeLayout       rlPolicy;
    @BindView(R.id.rl_facilities)
    RelativeLayout       rlFacilities;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.rl_chat)
    RelativeLayout       llChat;

    @BindView(R.id.ll_breakfast1)
    LinearLayout mTopBreakfastLayout;
    @BindView(R.id.tv_jl)
    TextView     tvJl;
    @BindView(R.id.tv_breakfast_type11)
    TextView     tvBreakfastType11;
    @BindView(R.id.tv_breakfast_type22)
    TextView     tvBreakfastType22;
    @BindView(R.id.tv_breakfast_type33)
    TextView     tvBreakfastType33;
    @BindView(R.id.tv_breakfast_type44)
    TextView     tvBreakfastType44;
    private String id, city_value, s_date, e_date, lng, lat, title;
    private boolean summary_is_open;


    private RoomAdapter       mRoomAdapter;
    private ConferenceAdapter mConferenceAdapter;

    private List<RoomInfo>       roomInfoListAll       = new ArrayList<>();
    private List<ConferenceInfo> conferenceInfoListAll = new ArrayList<>();

    private List<RoomInfo>       roomInfoList       = new ArrayList<>();
    private List<ConferenceInfo> conferenceInfoList = new ArrayList<>();


    private static final int    REQUEST_SUCCESS  = 0x01;
    private static final int    REQUEST_FAIL     = 0x02;
    private static final String GET_HOTEL_DETAIL = "GET_HOTEL_DETAIL";


    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    HotelDetailHandler mHotelDetailHandler = (HotelDetailHandler) msg.obj;
                    HotelInfo mHotelInfo = mHotelDetailHandler.getHotelInfo();

                    if (null != mHotelInfo)
                    {
                        int width = APPUtils.getScreenWidth(HotelDetailActivity.this);
                        int height = (int) (width * 0.66);
                        ivHotelImg.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mHotelInfo.getHotel_img()), ivHotelImg);
                        rbStar.setRating(Float.parseFloat(mHotelInfo.getStar() + ""));
                        tvAddress.setText("地址:" + mHotelInfo.getAddress());
                        tvDistance.setText(mHotelInfo.getJl() + "公里");
                        tvReviewsLabe.setText(mHotelInfo.getReviews_label());
                        tvCommentCount.setText(mHotelInfo.getCount() + "条评论");
                        tvStartDate.setText(s_date);
                        tvEndDate.setText(e_date);
                        tvSummary.setText(mHotelInfo.getSummary());
                        tvSummary.setLines(3);
                    }
                    roomInfoListAll.addAll(mHotelDetailHandler.getRoomInfoList());
                    conferenceInfoListAll.addAll(mHotelDetailHandler.getConferenceInfoList());

                    roomInfoList.addAll(mHotelDetailHandler.getRoomInfoList());
                    conferenceInfoList.addAll(mHotelDetailHandler.getConferenceInfoList());

                    mConferenceAdapter.notifyDataSetChanged();
                    mRoomAdapter.notifyDataSetChanged();


                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(HotelDetailActivity.this, msg.obj.toString());

                    break;

            }
        }
    };


    @Override
    protected void initData()
    {
        Intent mIntent = getIntent();
        id = mIntent.getStringExtra("ID");
        city_value = mIntent.getStringExtra("CITY_VALUE");
        s_date = mIntent.getStringExtra("S_DATE");
        e_date = mIntent.getStringExtra("E_DATE");
        lng = mIntent.getStringExtra("LNG");
        lat = mIntent.getStringExtra("LAT");
        title = mIntent.getStringExtra("TITLE");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_hotel_detail);
        setStatusColor(ContextCompat.getColor(this, R.color.windowBackground));
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        tvCommentCount.setOnClickListener(this);
        llRoomMore.setOnClickListener(this);
        llConferenceMore.setOnClickListener(this);
        tvSummaryMore.setOnClickListener(this);
        rlPolicy.setOnClickListener(this);
        rlFacilities.setOnClickListener(this);

        tvBreakfastType1.setOnClickListener(this);
        tvBreakfastType2.setOnClickListener(this);
        tvBreakfastType3.setOnClickListener(this);
        tvBreakfastType4.setOnClickListener(this);

        tvBreakfastType11.setOnClickListener(this);
        tvBreakfastType22.setOnClickListener(this);
        tvBreakfastType33.setOnClickListener(this);
        tvBreakfastType44.setOnClickListener(this);


        scrollView.setOnScrollListener(new ObservableScrollView.OnScrollListener()
        {
            @Override
            public void onScroll(int scrollY)
            {
                int mHeight = mBreakfastLayout.getTop();
                //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。

                if (scrollY > 0 && scrollY >= mHeight)
                {
                    if (!mTopBreakfastLayout.isShown())
                    {
                        mTopBreakfastLayout.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    if (mTopBreakfastLayout.isShown())
                    {
                        mTopBreakfastLayout.setVisibility(View.GONE);

                    }

                }
            }
        });
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText(title);

        rvRoom.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvRoom.addItemDecoration(new EmptyDecoration(HotelDetailActivity.this, ""));
        mRoomAdapter = new RoomAdapter(roomInfoList, HotelDetailActivity.this, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }
        });
        rvRoom.setAdapter(mRoomAdapter);


        rvConference.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvConference.addItemDecoration(new EmptyDecoration(HotelDetailActivity.this, ""));

        mConferenceAdapter = new ConferenceAdapter(conferenceInfoList, HotelDetailActivity.this);
        rvConference.setAdapter(mConferenceAdapter);

        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", id);
        valuePairs.put("lat", lat);
        valuePairs.put("lng", lng);
        valuePairs.put("city_value", city_value);
        valuePairs.put("s_date", s_date);
        valuePairs.put("e_date", e_date);
        DataRequest.instance().request(HotelDetailActivity.this, Urls.getHotelDetailUrl(), this, HttpRequest.POST, GET_HOTEL_DETAIL, valuePairs,
                new HotelDetailHandler());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            finish();
        }
        //地图定位
        else if (v == ivLocation)
        {

        }
        //评论列表
        else if (v == tvCommentCount)
        {

        }
        //更多房间
        else if (v == llRoomMore)
        {

        }
        //更多会议室
        else if (v == llConferenceMore)
        {

        }
        //查看全部简介
        else if (v == tvSummaryMore)
        {
            if (summary_is_open)
            {
                summary_is_open = false;
                tvSummary.setMaxLines(3);
                tvSummaryMore.setText("查看更多酒店详情");
            }
            else
            {
                summary_is_open = true;
                tvSummary.setMaxLines(99);
                tvSummaryMore.setText("收起更多酒店详情");
            }
        }
        //查看政策
        else if (v == rlPolicy)
        {

        }
        //查看设施
        else if (v == rlFacilities)
        {

        }
        //跳转聊天
        else if (v == llChat)
        {

        }
        //不限早餐
        else if (v == tvBreakfastType1 || v == tvBreakfastType11)
        {

        }
        //无早餐
        else if (v == tvBreakfastType2 || v == tvBreakfastType22)
        {

        }
        //单早餐
        else if (v == tvBreakfastType3 || v == tvBreakfastType33)
        {

        }
        //双早餐
        else if (v == tvBreakfastType4 || v == tvBreakfastType44)
        {

        }

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_HOTEL_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


}