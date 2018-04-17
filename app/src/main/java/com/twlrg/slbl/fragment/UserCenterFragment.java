package com.twlrg.slbl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.ModifyPwdActivity;
import com.twlrg.slbl.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 16:50
 * 邮箱：wangxianyun1@163.com
 * 描述：个人中心
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener
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
    private View rootView = null;
    private Unbinder unbinder;

    private int mEditStatus;

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

        }
        else if (v == tvModifyPwd)
        {
            startActivity(new Intent(getActivity(), ModifyPwdActivity.class));
        }
        else if (v == tvUserSex)
        {

        }

    }
}
