package com.twlrg.slbl.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.adapter.SaleAdapter;
import com.twlrg.slbl.entity.SaleInfo;
import com.twlrg.slbl.entity.SubOrderInfo;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.ResultHandler;
import com.twlrg.slbl.json.SaleInfoListHandler;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;
import com.twlrg.slbl.widget.DividerDecoration;
import com.twlrg.slbl.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/24 20:18
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class SubmitOrderActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_hotel_name)
    TextView        tvHotelName;
    @BindView(R.id.tv_occupant)
    TextView        tvOccupant;
    @BindView(R.id.tv_room_title)
    TextView        tvRoomTitle;
    @BindView(R.id.tv_days)
    TextView        tvDays;
    @BindView(R.id.tv_phone)
    TextView        tvPhone;
    @BindView(R.id.tv_time)
    TextView        tvTime;
    @BindView(R.id.tv_total_fee)
    TextView        tvTotalFee;
    @BindView(R.id.tv_price_detail)
    TextView        tvPriceDetail;
    @BindView(R.id.tv_cancel_policy)
    TextView        tvCancelPolicy;
    @BindView(R.id.recyclerView)
    RecyclerView    recyclerView;
    @BindView(R.id.btn_submit)
    Button          btnSubmit;
    @BindView(R.id.et_mark)
    EditText        etMark;
    private SubOrderInfo mSubOrderInfo;
    private SaleAdapter  mSaleAdapter;
    private              List<SaleInfo> saleInfoList          = new ArrayList<>();
    private static final int            REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int            REQUEST_FAIL          = 0x02;
    private static final int            GET_SALE_SUCCESS      = 0x03;

    private static final String      SUB_ORDER = "sub_order";
    private static final String      GET_SALE  = "get_sale";
    private              BaseHandler mHandler  = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:
                    ToastUtil.show(SubmitOrderActivity.this, "操作成功!");
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(SubmitOrderActivity.this, msg.obj.toString());
                    break;

                case GET_SALE_SUCCESS:
                    SaleInfoListHandler mSaleInfoListHandler = (SaleInfoListHandler) msg.obj;
                    saleInfoList.clear();
                    saleInfoList.addAll(mSaleInfoListHandler.getSaleInfoList());
                    mSaleAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        mSubOrderInfo = (SubOrderInfo) getIntent().getSerializableExtra("SubOrderInfo");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

        setContentView(R.layout.activity_submit_order);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("订单确认");
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        if (null != mSubOrderInfo)
        {
            saleInfoList.addAll(mSubOrderInfo.getSaleInfoList());
            tvHotelName.setText(mSubOrderInfo.getHotelName());
            tvOccupant.setText(mSubOrderInfo.getOccupant());
            tvPhone.setText(mSubOrderInfo.getPhone());
            tvRoomTitle.setText(mSubOrderInfo.getRoomTitle());
            tvDays.setText(mSubOrderInfo.getDays() + "晚  " + mSubOrderInfo.getBuynum() + "间");
            tvTime.setText(mSubOrderInfo.getS_data() + " 至 " + mSubOrderInfo.getE_data());
            tvCancelPolicy.setText(mSubOrderInfo.getCancel_policy());
            tvTotalFee.setText("￥" + mSubOrderInfo.getTotal_feel());
        }

        recyclerView.setLayoutManager(new FullyLinearLayoutManager(SubmitOrderActivity.this));
        recyclerView.addItemDecoration(new DividerDecoration(SubmitOrderActivity.this));

        mSaleAdapter = new SaleAdapter(saleInfoList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                for (int i = 0; i < saleInfoList.size(); i++)
                {
                    if (i == position)
                    {
                        saleInfoList.get(i).setSelected(true);
                    }
                    else
                    {
                        saleInfoList.get(i).setSelected(false);
                    }
                }

                mSaleAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mSaleAdapter);

        if (saleInfoList.isEmpty())
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("merchant_id", mSubOrderInfo.getMerchant_id());
            DataRequest.instance().request(SubmitOrderActivity.this, Urls.getSaleListUrl(), this, HttpRequest.POST, GET_SALE, valuePairs,
                    new SaleInfoListHandler());
        }


    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == btnSubmit)
        {
//            showProgressDialog();
//            Map<String, String> valuePairs = new HashMap<>();
//            valuePairs.put("order_id", mSubOrderInfo.getOrder_id());
//            valuePairs.put("sale_uid", getSaleUid());
//            valuePairs.put("remark", etMark.getText().toString());
//            DataRequest.instance().request(SubmitOrderActivity.this, Urls.getSelectSaleUrl(), this, HttpRequest.POST, SUB_ORDER, valuePairs,
//                    new ResultHandler());
        }
    }


    private String getSaleUid()
    {
        String sale_uid = "";
        for (int i = 0; i < saleInfoList.size(); i++)
        {
            if (saleInfoList.get(i).isSelected())
            {
                sale_uid = saleInfoList.get(i).getId();
            }
        }

        return sale_uid;
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (SUB_ORDER.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_LOGIN_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_SALE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_SALE_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


}
