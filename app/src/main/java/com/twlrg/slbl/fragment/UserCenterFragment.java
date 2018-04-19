package com.twlrg.slbl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.slbl.MyApplication;
import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.BaseHandler;
import com.twlrg.slbl.activity.LoginActivity;
import com.twlrg.slbl.activity.MainActivity;
import com.twlrg.slbl.activity.ModifyPwdActivity;
import com.twlrg.slbl.http.DataRequest;
import com.twlrg.slbl.http.HttpRequest;
import com.twlrg.slbl.http.IRequestListener;
import com.twlrg.slbl.json.LoginHandler;
import com.twlrg.slbl.json.ResultHandler;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.ConstantUtil;
import com.twlrg.slbl.utils.LogUtil;
import com.twlrg.slbl.utils.ToastUtil;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：个人中心
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener, IRequestListener
{

    @BindView(R.id.tv_edit)
    TextView        tvEdit;
    @BindView(R.id.tv_cancel)
    TextView        tvCancel;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.et_nickName)
    EditText        etNickName;
    @BindView(R.id.tv_account)
    TextView        tvAccount;
    @BindView(R.id.et_userName)
    EditText        etUserName;
    @BindView(R.id.tv_userSex)
    TextView        tvUserSex;
    @BindView(R.id.tv_userPhone)
    TextView        tvUserPhone;
    @BindView(R.id.et_userEmail)
    EditText        etUserEmail;
    @BindView(R.id.tv_modify_pwd)
    TextView        tvModifyPwd;
    @BindView(R.id.tv_version)
    TextView        tvVersion;
    @BindView(R.id.btn_logout)
    Button          btnLogout;
    @BindView(R.id.topView)
    View            topView;
    private View rootView = null;
    private Unbinder unbinder;

    private int mEditStatus;


    private static final int    REQUEST_LOGIN_SUCCESS = 0x01;
    public static final  int    REQUEST_FAIL          = 0x02;
    private static final String GET_USER_INFO         = "get_user_info";


    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {


                case REQUEST_LOGIN_SUCCESS:

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
            rootView = inflater.inflate(R.layout.fragment_user_center, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        // 缓存的rootView需要判断是否已经被加过parent
        // 如果有parent需要从parent删除，否则会发生这个rootView已经有parent的错误
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
        ((MainActivity) getActivity()).changeTabStatusColor(3);
        if (!MyApplication.getInstance().isLogin())
        {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

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
        tvEdit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvModifyPwd.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        showEditStatus(false);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));

        ImageLoader.getInstance().displayImage(Urls.getImgUrl(ConfigManager.instance().getUserPic()), ivUserHead);
        etNickName.setText(ConfigManager.instance().getUserNickName());
        tvAccount.setText(ConfigManager.instance().getMobile());
        etUserName.setText(ConfigManager.instance().getUserName());

        int sex = ConfigManager.instance().getUserSex();

        if (sex == 0)
        {
            tvUserSex.setText("保密");
        }
        else if (sex == 1)
        {
            tvUserSex.setText("男");
        }
        else
        {
            tvUserSex.setText("女");
        }

        tvUserPhone.setText(ConfigManager.instance().getMobile());
        etUserEmail.setText(ConfigManager.instance().getUserEmail());
        tvVersion.setText("版本：V" + APPUtils.getVersionName(getActivity()));

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


    private void showEditStatus(boolean isEdit)
    {
        if (isEdit)
        {
            tvCancel.setVisibility(View.VISIBLE);
            tvEdit.setText("保存");
            mEditStatus = 1;
            etNickName.setEnabled(true);
            etUserName.setEnabled(true);
            etUserEmail.setEnabled(true);
            tvUserSex.setEnabled(true);
        }
        else
        {
            tvCancel.setVisibility(View.GONE);
            tvEdit.setText("编辑");
            mEditStatus = 0;
            etNickName.setEnabled(false);
            etUserName.setEnabled(false);
            etUserEmail.setEnabled(false);
            tvUserSex.setEnabled(false);
        }
    }


    @Override
    public void onClick(View v)
    {

        if (v == tvCancel)
        {
            showEditStatus(false);
        }
        else if (v == tvEdit)
        {
            if (mEditStatus == 0)
            {
                showEditStatus(true);
            }
            else
            {
                //TODO 执行保存操作  保存成功后 调用  showEditStatus(false);
            }
        }
        else if (v == btnLogout)
        {
            ConfigManager.instance().setUserId("");
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        else if (v == tvModifyPwd)
        {
            startActivity(new Intent(getActivity(), ModifyPwdActivity.class));
        }
        else if (v == tvUserSex)
        {

        }

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_USER_INFO.equals(action))
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
}
