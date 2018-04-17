package com.twlrg.slbl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.BaseHandler;
import com.twlrg.slbl.activity.HotelDetailActivity;
import com.twlrg.slbl.adapter.HotelAdapter;
import com.twlrg.slbl.entity.HotelInfo;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.HotelInfoListHandler;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.EmptyDecoration;
import com.twlrg.slbl.widget.list.refresh.PullToRefreshBase;
import com.twlrg.slbl.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：首页
 */
public class HomeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, View.OnClickListener, IRequestListener
{

    @BindView(R.id.tv_city)
    TextView                  tvCity;
    @BindView(R.id.rl_city)
    RelativeLayout            rlCity;
    @BindView(R.id.tv_check)
    TextView                  tvCheck;
    @BindView(R.id.tv_leave)
    TextView                  tvLeave;
    @BindView(R.id.tv_star)
    TextView                  tvStar;
    @BindView(R.id.rl_star)
    RelativeLayout            rlStar;
    @BindView(R.id.tv_distance)
    TextView                  tvDistance;
    @BindView(R.id.rl_distance)
    RelativeLayout            rlDistance;
    @BindView(R.id.tv_price)
    TextView                  tvPrice;
    @BindView(R.id.rl_price)
    RelativeLayout            rlPrice;
    @BindView(R.id.tv_more)
    TextView                  tvMore;
    @BindView(R.id.rl_more)
    RelativeLayout            rlMore;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    private RecyclerView mRecyclerView;
    private View rootView = null;
    private Unbinder unbinder;


    private int pn = 1;
    private int mRefreshStatus;

    private String mStartDate, mEndDate;
    private List<HotelInfo> hotelInfoList = new ArrayList<>();
    private HotelAdapter mHotelAdapter;
    private static final String GET_HOTEL_LIST = "GET_HOTEL_LIST";

    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL    = 0x02;

    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    HotelInfoListHandler mHotelInfoListHandler = (HotelInfoListHandler) msg.obj;
                    hotelInfoList.addAll(mHotelInfoListHandler.getHotelInfoList());
                    mHotelAdapter.notifyDataSetChanged();

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());

                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_home, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(getActivity(), ""));

        mHotelAdapter = new HotelAdapter(hotelInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                HotelInfo mHotelInfo = hotelInfoList.get(position);

                startActivity(new Intent(getActivity(), HotelDetailActivity.class)
                        .putExtra("ID", mHotelInfo.getId())
                        .putExtra("CITY_VALUE", mHotelInfo.getCity())
                        .putExtra("S_DATE", mStartDate)
                        .putExtra("E_DATE", mEndDate)
                        .putExtra("LNG", mHotelInfo.getLng())
                        .putExtra("lat", mHotelInfo.getLat())


                );
            }
        }
        );
        mRecyclerView.setAdapter(mHotelAdapter);
        getHotelList();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }
    }

    //star 星级（0代表不限；2代表经济型；3代表三星实惠；4代表四星豪华；5代表五星奢华）
    //range 距离（0代表不限；1代表500米内；2代表1公里内；3代表2公里内；4代表5公里内；5代表10公里内；6代表20公里内）
    //price 价格（0代表不限；1代表150元以下；2代表150-300元；3代表301-450元；4代表451-600元；5代表600元以上）
    private void getHotelList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("lat", "0");
        valuePairs.put("lng", "0");
        valuePairs.put("city_value", "2158");
        valuePairs.put("star", "0");
        valuePairs.put("range", "0");
        valuePairs.put("price", "0");
        valuePairs.put("s_date", "2018-04-17");
        valuePairs.put("e_date", "2018-04-18");
        valuePairs.put("page", pn + "");
        DataRequest.instance().request(getActivity(), Urls.getHotelListUrl(), this, HttpRequest.POST, GET_HOTEL_LIST, valuePairs,
                new HotelInfoListHandler());
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        hotelInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        getHotelList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        getHotelList();
    }


    @Override
    public void onClick(View v)
    {

    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_HOTEL_LIST.equals(action))
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
