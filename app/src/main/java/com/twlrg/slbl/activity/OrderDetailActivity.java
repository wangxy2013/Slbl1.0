package com.twlrg.slbl.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.slbl.R;
import com.twlrg.slbl.entity.OrderInfo;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.OrderInfoHandler;
import com.twlrg.slbl.json.ResultHandler;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.StringUtils;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.AutoFitTextView;
import com.twlrg.slbl.widget.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/23 13:47
 * 邮箱：wangxianyun1@163.com
 * 描述：订单详情
 */
public class OrderDetailActivity extends BaseActivity implements IRequestListener
{


    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.tv_merchant)
    AutoFitTextView tvMerchant;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_mobile)
    TextView        tvMobile;
    @BindView(R.id.tv_room)
    TextView        tvRoom;
    @BindView(R.id.tv_buynum)
    TextView        tvBuynum;
    @BindView(R.id.tv_time)
    TextView        tvTime;
    @BindView(R.id.tv_days)
    TextView        tvDays;
    @BindView(R.id.tv_status)
    TextView        tvStatus;
    @BindView(R.id.tv_total_fee)
    TextView        tvTotalFee;
    @BindView(R.id.tv_price_detail)
    TextView        tvPriceDetail;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.tv_salesperson)
    TextView        tvSalesperson;
    @BindView(R.id.tv_position)
    TextView        tvPosition;
    @BindView(R.id.iv_message)
    ImageView       ivMessage;
    @BindView(R.id.iv_phone)
    ImageView       ivPhone;
    @BindView(R.id.rl_sale)
    RelativeLayout  rlSale;
    @BindView(R.id.btn_cancel)
    Button          btnCancel;
    @BindView(R.id.btn_status)
    Button          btnStatus;
    private String    order_id;
    private OrderInfo mOrderInfo;

    private static final int REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int REQUEST_FAIL          = 0x02;
    private static final int ORDER_CANCEL_SUCCESS  = 0x03;

    private static final String GET_ORDER_INFO = "get_order_info";
    private static final String ORDER_CANCEL   = "ORDER_CANCEL";

    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_LOGIN_SUCCESS:
                    OrderInfoHandler mOrderInfoHandler = (OrderInfoHandler) msg.obj;
                    mOrderInfo = mOrderInfoHandler.getOrderInfo();

                    if (null != mOrderInfo)
                    {
                        tvTitle.setText("订单号" + mOrderInfo.getId());
                        tvName.setText(mOrderInfo.getName());
                        tvMobile.setText(mOrderInfo.getMobile());
                        tvMerchant.setText(mOrderInfo.getMerchant());
                        tvTotalFee.setText("总额:￥" + mOrderInfo.getTotal_fee());

                        tvTime.setText(mOrderInfo.getCheck_in() + "至" + mOrderInfo.getCheck_out());

                        String price_type = mOrderInfo.getPrice_type();
                        String zc = "无早餐";
                        if ("wz".equals(price_type))
                        {
                            zc = "无早餐";
                        }
                        else if ("dz".equals(price_type))
                        {
                            zc = "单早餐";
                        }
                        else if ("sz".equals(price_type))
                        {
                            zc = "双早餐";
                        }

                        tvRoom.setText(mOrderInfo.getTitle() + "[" + zc + "]");
                        tvBuynum.setText(mOrderInfo.getBuynum() + "间");
                        tvDays.setText(mOrderInfo.getDays() + "晚");
                        tvStatus.setText("0".equals(mOrderInfo.getStatus()) ? "未支付" : "已支付");

                        if (StringUtils.stringIsEmpty(mOrderInfo.getSalesperson()) ||"-".equals(mOrderInfo.getSalesperson()))
                        {
                            rlSale.setVisibility(View.GONE);
                        }
                        else
                        {
                            rlSale.setVisibility(View.VISIBLE);
                        }

                        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mOrderInfo.getPortrait()), ivUserHead);
                        tvSalesperson.setText(mOrderInfo.getSalesperson());
                        tvPosition.setText(mOrderInfo.getPosition());


                        switch (mOrderInfo.getIs_used())
                        {
                            case 0:
                                btnCancel.setVisibility(View.VISIBLE);
                                btnStatus.setText("去支付");
                                btnStatus.setEnabled(true);
                                break;
                            case 1:
                                btnCancel.setVisibility(View.VISIBLE);
                                btnStatus.setText("已预订成功");
                                btnStatus.setEnabled(false);

                                break;
                            case 2:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("酒店满房拒单");
                                btnStatus.setEnabled(false);
                                break;
                            case 3:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("取消确认中");
                                btnStatus.setEnabled(false);
                                break;
                            case 4:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("已取消");
                                btnStatus.setEnabled(false);
                                break;
                            case 5:
                                btnCancel.setVisibility(View.GONE);
                                btnStatus.setText("酒店拒绝取消");
                                btnStatus.setEnabled(false);
                                break;
                        }


                    }

                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(OrderDetailActivity.this, msg.obj.toString());
                    break;


                case ORDER_CANCEL_SUCCESS:
                    ToastUtil.show(OrderDetailActivity.this, "申请退订提交成功");
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        order_id = getIntent().getStringExtra("ORDER_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_order_detail);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
        tvPriceDetail.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("order_id", order_id);
        DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrderDetailUrl(), this, HttpRequest.POST, GET_ORDER_INFO, valuePairs,
                new OrderInfoHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == ivPhone)
        {
            if (null != mOrderInfo && !StringUtils.stringIsEmpty(mOrderInfo.getSale_mobile()))
            {
                APPUtils.callPhone(OrderDetailActivity.this, mOrderInfo.getSale_mobile());
            }
            else
            {
                ToastUtil.show(OrderDetailActivity.this, "暂时无法电话联系销售人员,请尝试其他方式");
            }
        }
        else if (v == ivMessage)
        {

        }
        else if (v == btnCancel)//取消订单
        {
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("token", ConfigManager.instance().getToken());
            valuePairs.put("uid", ConfigManager.instance().getUserID());
            valuePairs.put("order_id", order_id);
            DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrderCancelUrl(), this, HttpRequest.POST, ORDER_CANCEL, valuePairs,
                    new ResultHandler());

        }
        else if (v == btnStatus)//去支付
        {

        }
        else if (v == tvPriceDetail)
        {

        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ORDER_INFO.equals(action))
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
        else if (ORDER_CANCEL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ORDER_CANCEL_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

}
