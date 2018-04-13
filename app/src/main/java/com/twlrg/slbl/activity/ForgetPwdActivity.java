package com.twlrg.slbl.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.ResultHandler;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.StringUtils;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/13 10:28
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ForgetPwdActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_phone)
    EditText  etPhone;
    @BindView(R.id.et_code)
    EditText  etCode;
    @BindView(R.id.tv_get_code)
    TextView  tvGetCode;
    @BindView(R.id.et_pwd)
    EditText  etPwd;
    @BindView(R.id.et_pwd1)
    EditText  etPwd1;
    @BindView(R.id.btn_save)
    Button    btnSave;

    private static final int    REQUEST_REGISTER_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL             = 0x02;
    private static final int    GET_CODE_SUCCESS         = 0x03;
    private static final String USER_REGISTER            = "user_register";
    private static final String GET_CODE                 = "GET_CODE";


    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_REGISTER_SUCCESS:
                    ToastUtil.show(ForgetPwdActivity.this, "注册成功!");
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(ForgetPwdActivity.this, msg.obj.toString());
                    break;

                case GET_CODE_SUCCESS:
                    ToastUtil.show(ForgetPwdActivity.this, "验证码已发送");
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

        setContentView(R.layout.activity_forget_pwd);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvGetCode)
        {
            String phone = etPhone.getText().toString();

            if (StringUtils.stringIsEmpty(phone) || phone.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }


            Map<String, String> valuePairs = new HashMap<>();
            DataRequest.instance().request(ForgetPwdActivity.this, Urls.getLoginUrl(), this, HttpRequest.POST, GET_CODE, valuePairs,
                    new ResultHandler());
        }
        else if (v == btnSave)
        {
            String phone = etPhone.getText().toString();
            String code = etCode.getText().toString();
            String pwd = etPwd.getText().toString();
            String pwd1 = etPwd1.getText().toString();


            if (StringUtils.stringIsEmpty(phone) || phone.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }
            if (StringUtils.stringIsEmpty(code))
            {
                ToastUtil.show(this, "请输入验证码");
                return;
            }
            if (StringUtils.stringIsEmpty(pwd) || pwd.length() < 8)
            {
                ToastUtil.show(this, "请输入8-16密码");
                return;
            }

            if (!pwd.equals(pwd1))
            {
                ToastUtil.show(this, "两次密码输入不一致");
                return;
            }


            Map<String, String> valuePairs = new HashMap<>();
            DataRequest.instance().request(ForgetPwdActivity.this, Urls.getLoginUrl(), this, HttpRequest.POST, USER_REGISTER, valuePairs,
                    new ResultHandler());


        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (USER_REGISTER.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_REGISTER_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

        if (GET_CODE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_CODE_SUCCESS, obj));
            }

            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }


    }
}
