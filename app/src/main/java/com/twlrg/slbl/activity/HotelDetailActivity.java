package com.twlrg.slbl.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.HotelInfoListHandler;
import com.twlrg.slbl.utils.LogUtil;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.widget.AutoFitTextView;
import com.twlrg.slbl.widget.ObservableScrollView;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/17 22:01
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
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
    @BindView(R.id.tv_text1)
    TextView             tvText1;
    @BindView(R.id.iv_arrow_right)
    ImageView            ivArrowRight;
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
    LinearLayout         llBreakfast;
    @BindView(R.id.rv_room)
    RecyclerView         rvRoom;
    @BindView(R.id.iv_room_more)
    ImageView            ivRoomMore;
    @BindView(R.id.ll_room_more)
    LinearLayout         llRoomMore;
    @BindView(R.id.tv_conference)
    TextView             tvConference;
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
    @BindView(R.id.ll_chat)
    LinearLayout         llChat;
    @BindView(R.id.tv_breakfast_type11)
    TextView             tvBreakfastType11;
    @BindView(R.id.tv_breakfast_type22)
    TextView             tvBreakfastType22;
    @BindView(R.id.tv_breakfast_type33)
    TextView             tvBreakfastType33;
    @BindView(R.id.tv_breakfast_type44)
    TextView             tvBreakfastType44;
    @BindView(R.id.ll_breakfast1)
    LinearLayout         llBreakfast1;
    @BindView(R.id.tv_jl)
    TextView             tvJl;
    @BindView(R.id.ll_head)
    LinearLayout         llHead;


    private String id, city_value, s_date, e_date, lng, lat;
    private int topHeight;


    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL    = 0x02;
    private static final int GET_VIEW        = 0x12;


    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(HotelDetailActivity.this, msg.obj.toString());

                    break;
                case GET_VIEW:
                    Rect frame = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;//状态栏高度

                    int titleBarHeight = llHead.getMeasuredHeight();
                    LogUtil.e("TAG", "titleBarHeight---->" + titleBarHeight);

                    topHeight = titleBarHeight + statusBarHeight;
                    LogUtil.e("TAG", "topHeight---->" + topHeight);
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
    }

    @Override
    protected void initViewData()
    {
        mHandler.sendEmptyMessageDelayed(GET_VIEW, 1000);
    }



    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

    }


}
