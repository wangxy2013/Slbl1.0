package com.twlrg.slbl.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twlrg.slbl.R;
import com.twlrg.slbl.utils.StringUtils;
import com.twlrg.slbl.utils.ToastUtil;

import butterknife.BindView;

/**
 * 作者：王先云 on 2018/4/12 23:12
 * 邮箱：wangxianyun1@163.com
 * 描述：修改密码
 */
public class ModifyPwdActivity extends BaseActivity
{
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.et_oldPwd)
    EditText       etOldPwd;
    @BindView(R.id.et_newPwd)
    EditText       etNewPwd;
    @BindView(R.id.et_newPwd1)
    EditText       etNewPwd1;
    @BindView(R.id.btn_submit)
    Button         btnSubmit;

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_modify_pwd);
        setStatusColor(ContextCompat.getColor(this, R.color.windowBackground));
    }

    @Override
    protected void initEvent()
    {
        rlBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("修改密码");
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == rlBack)
        {
            finish();
        }
        else if (v == btnSubmit)
        {
            String oldPwd = etOldPwd.getText().toString();
            String newPwd = etNewPwd.getText().toString();
            String newPwd1 = etNewPwd1.getText().toString();

            if (StringUtils.stringIsEmpty(oldPwd))
            {
                ToastUtil.show(this, "请输入旧密码");
                return;
            }


            if (StringUtils.stringIsEmpty(newPwd))
            {
                ToastUtil.show(this, "请输入新密码");
                return;
            }

            if (newPwd.length() < 8)
            {
                ToastUtil.show(this, "请输入8-16位新密码");
                return;
            }


            if (!newPwd.equals(newPwd1))
            {
                ToastUtil.show(this, "两次新密码输入不一致");
                return;
            }
        }
    }
}
