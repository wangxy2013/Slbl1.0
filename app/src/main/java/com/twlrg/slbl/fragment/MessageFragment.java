package com.twlrg.slbl.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.twlrg.slbl.MyApplication;
import com.twlrg.slbl.activity.LoginActivity;
import com.twlrg.slbl.im.event.LoginEvent;
import com.twlrg.slbl.im.ui.ConversationFragment;

import de.greenrobot.event.EventBus;

/**
 * 作者：王先云 on 2018/4/12 20:19
 * 邮箱：wangxianyun1@163.com
 * 描述：消息
 */
public class MessageFragment extends ConversationFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEventMainThread(LoginEvent event){
        reloadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MyApplication.getInstance().isLogin()) {
            LoginActivity.start(getActivity(), true);
        }
    }
}
