package com.twlrg.slbl.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.twlrg.slbl.R;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.im.TencentCloud;
import com.twlrg.slbl.im.model.UserInfo;
import com.twlrg.slbl.json.LoginHandler;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.DialogUtils;
import com.twlrg.slbl.utils.LogUtil;
import com.twlrg.slbl.utils.StringUtils;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * 作者：王先云 on 2018/4/13 09:53
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class LoginActivity extends BaseActivity implements IRequestListener
{

    public static void start(Context context, boolean loginIM)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra("loginIM", loginIM);
        context.startActivity(starter);
    }

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_register)
    TextView  tvRegister;
    @BindView(R.id.et_phone)
    EditText  etPhone;
    @BindView(R.id.et_pwd)
    EditText  etPwd;
    @BindView(R.id.btn_login)
    Button    btnLogin;
    @BindView(R.id.tv_forget_pwd)
    TextView  tvForgetPwd;

    String mUserName, mPwd;
    private String  userId;
    private RegisterImBroadcast mRegisterImBroadcast;
    private String REGISTER_IM = "REGISTER_IM";
    private String registerUid;
    private static final int    REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL          = 0x02;
    public static final  int    LOGIN_IM              = 0X03;
    public static final  int    ACTIVITY_FINISH       = 0X04;
    private static final int    TTS_REGISTER          = 0x06;
    private static final String USER_LOGIN            = "user_login";

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:
                    LoginHandler mLoginHandler1   = (LoginHandler)msg.obj;
                    userId = mLoginHandler1.getUserId();
                    ConfigManager.instance().setIdentifier(userId);
                    ConfigManager.instance().setUserPwd(mPwd);
                    ConfigManager.instance().setMobile(mUserName);
                    sendEmptyMessage(LOGIN_IM);
                    break;


                case REQUEST_FAIL:
                    hideProgressDialog();
                    ToastUtil.show(LoginActivity.this, msg.obj.toString());
                    break;

                case LOGIN_IM:
                    TencentCloud.LoginListener login = new TencentCloud.LoginListener()
                    {
                        @Override
                        public void onSuccess(String identifier)
                        {
                            TLSService.getInstance().setLastErrno(0);
                            modifyUserProfile();
                        }

                        @Override
                        public void onFail(String msg, int code2)
                        {
                            TLSService.getInstance().setLastErrno(-1);
                            LogUtil.d("login", "failed:" + msg + " " + code2);
                            hideProgressDialog();
                            ToastUtil.show(LoginActivity.this, "登录失败!");

                            registerUid = ConfigManager.instance().getUserID();
                            mHandler.sendEmptyMessage(TTS_REGISTER);

                        }
                    };
                    String identifier = ConfigManager.instance().getIdentifier();
                    if (getIntent().getBooleanExtra("loginIM", false))
                    {
                        TencentCloud.IMLogin(identifier, login);
                    }
                    else
                    {
                        TencentCloud.login(identifier, login);
                    }
                    break;

                case ACTIVITY_FINISH:
                    hideProgressDialog();
                    finish();
                    break;
                case TTS_REGISTER:

                    if (!StringUtils.stringIsEmpty(registerUid))
                    {

                        TLSHelper instance = TLSHelper.getInstance();
                        instance.TLSStrAccReg("slbl_client_" + registerUid, "slbl123456", new TLSStrAccRegListener()
                        {
                            @Override
                            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo)
                            {
                                //LogUtil.d(TAG, "OnStrAccRegSuccess:" + tlsUserInfo.identifier + "");
                            }

                            @Override
                            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo)
                            {
                                //LogUtil.d(TAG, "OnStrAccRegFail:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                                mHandler.sendEmptyMessageDelayed(TTS_REGISTER, 500);

                            }

                            @Override
                            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo)
                            {
                                mHandler.sendEmptyMessageDelayed(TTS_REGISTER, 500);
                                //LogUtil.d(TAG, "OnStrAccRegTimeout:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                            }
                        });
                    }
                    break;
            }
        }
    };


    @Override
    protected void initData()
    {
        mRegisterImBroadcast = new RegisterImBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(REGISTER_IM);
        registerReceiver(mRegisterImBroadcast, intentFilter);

    }


    private void modifyUserProfile()
    {
        ConfigManager.instance().setUserId(userId);
        String name = ConfigManager.instance().getUserNickName();
        String userPic = Urls.getImgUrl(ConfigManager.instance().getUserPic());
        FriendshipManagerPresenter.setMyInfo(name, userPic, new TIMCallBack()
        {
            @Override
            public void onError(int i, String s)
            {

            }

            @Override
            public void onSuccess()
            {
                hideProgressDialog();
                LogUtil.e("login", "modifyUserProfile onSuccess");
                ToastUtil.show(LoginActivity.this, "登录成功!");
                finish();
            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_login);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        etPhone.setText(ConfigManager.instance().getMobile());
        etPwd.setText(ConfigManager.instance().getUserPwd());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == ivBack)
        {
            sendBroadcast(new Intent().setAction("USER_LOGOUT"));
            finish();
        }
        else if (v == tvForgetPwd)
        {
            startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
        }
        else if (v == tvRegister)
        {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
        else if (v == btnLogin)
        {
            mUserName = etPhone.getText().toString();
            mPwd = etPwd.getText().toString();


            if (StringUtils.stringIsEmpty(mUserName) || mUserName.length() < 11)
            {
                ToastUtil.show(this, "请输入正确的手机号");
                return;
            }


            if (StringUtils.stringIsEmpty(mPwd))
            {
                ToastUtil.show(this, "请输入正确的密码");
                return;
            }
            mHandler.removeMessages(TTS_REGISTER);
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("mobile", mUserName);
            valuePairs.put("pwd", mPwd);
            valuePairs.put("role", "1");
            DataRequest.instance().request(LoginActivity.this, Urls.getLoginUrl(), this, HttpRequest.POST, USER_LOGIN, valuePairs,
                    new LoginHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (USER_LOGIN.equals(action))
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
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            sendBroadcast(new Intent().setAction("USER_LOGOUT"));
            finish();
            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (null != mRegisterImBroadcast)
        {
            unregisterReceiver(mRegisterImBroadcast);
            mRegisterImBroadcast = null;
        }

        if (mHandler.hasMessages(TTS_REGISTER))
        {
            mHandler.removeMessages(TTS_REGISTER);
        }
    }

    class RegisterImBroadcast extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            LogUtil.e("TAG", "11111111111");
            if (REGISTER_IM.equals(intent.getAction()))
            {
                registerUid = intent.getStringExtra("UID");
                mHandler.sendEmptyMessage(TTS_REGISTER);
            }
        }
    }

}
