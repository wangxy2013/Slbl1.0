package com.twlrg.slbl.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twlrg.slbl.MyApplication;
import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.AddCommentActivity;
import com.twlrg.slbl.activity.BaseHandler;
import com.twlrg.slbl.activity.HotelDetailActivity;
import com.twlrg.slbl.activity.LoginActivity;
import com.twlrg.slbl.activity.MainActivity;
import com.twlrg.slbl.activity.OrderDetailActivity;
import com.twlrg.slbl.adapter.OrderAdapter;
import com.twlrg.slbl.entity.HotelInfo;
import com.twlrg.slbl.entity.OrderInfo;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.HotelInfoListHandler;
import com.twlrg.slbl.json.OrderListHandler;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;
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
 * 描述：订单
 */
public class OrderFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView>, IRequestListener
{

    @BindView(R.id.topView)
    View                      topView;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private View rootView = null;
    private Unbinder unbinder;

    private RecyclerView mRecyclerView;
    private int pn = 1;
    private int mRefreshStatus;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private OrderAdapter mOrderAdapter;
    private static final String GET_ORDER_LIST = "get_order_list";

    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL    = 0x02;
    private static final int INIT_ONRESUME   = 0x04;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    OrderListHandler mOrderListHandler = (OrderListHandler) msg.obj;
                    orderInfoList.addAll(mOrderListHandler.getOrderInfoList());
                    mOrderAdapter.notifyDataSetChanged();

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());

                    break;

                case INIT_ONRESUME:
                    if (((MainActivity) getActivity()).getTabIndex() == 2)
                    {
                        ((MainActivity) getActivity()).changeTabStatusColor(2);

                        if (MyApplication.getInstance().isLogin())
                        {
                            orderInfoList.clear();
                            pn = 1;
                            mRefreshStatus = 0;
                            getOrderList();
                        }
                        else
                        {
                            LoginActivity.start(getActivity(), true);
                        }
                    }
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order, null);
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
    public void onResume()
    {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(INIT_ONRESUME, 200);


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
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new EmptyDecoration(getActivity(), ""));

        mOrderAdapter = new OrderAdapter(orderInfoList, getActivity(), new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("ORDER_ID", orderInfoList.get(position).getId()));
            }
        }
        );
        mRecyclerView.setAdapter(mOrderAdapter);


    }

    private void getOrderList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("uid", ConfigManager.instance().getUserID());
        valuePairs.put("token", ConfigManager.instance().getToken());
        valuePairs.put("role", "1");
        valuePairs.put("type", "0");
        valuePairs.put("page", pn + "");
        DataRequest.instance().request(getActivity(), Urls.getOrderListUrl(), this, HttpRequest.POST, GET_ORDER_LIST, valuePairs,
                new OrderListHandler());
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        orderInfoList.clear();
        pn = 1;
        mRefreshStatus = 0;
        getOrderList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        getOrderList();
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

        if (GET_ORDER_LIST.equals(action))
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
}
