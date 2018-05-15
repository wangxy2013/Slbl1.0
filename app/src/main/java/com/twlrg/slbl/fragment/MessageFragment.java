package com.twlrg.slbl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.twlrg.slbl.R;
import com.twlrg.slbl.activity.MainActivity;
import com.twlrg.slbl.im.TencentCloud;
import com.twlrg.slbl.im.model.Conversation;
import com.twlrg.slbl.im.model.NomalConversation;
import com.twlrg.slbl.im.ui.ChatActivity;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.widget.list.refresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者：王先云 on 2018/4/12 20:19
 * 邮箱：wangxianyun1@163.com
 * 描述：消息
 */
public class MessageFragment extends BaseFragment implements TencentCloud.FriendshipListener, TencentCloud.GroupManageListener {
    @BindView(R.id.topView)
    View topView;
    @BindView(R.id.pullToRefreshRecyclerView)
    PullToRefreshListView pullListView;
    private View rootView = null;
    private Unbinder unbinder;
    private List<Conversation> conversationList = new LinkedList<>();
    private List<String> groupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @OnClick(R.id.topView)
    void onClickView(View v) {
        String identifier = ConfigManager.instance().getIdentifier();
        TencentCloud.IMLogin(identifier,new TencentCloud.LoginListener() {
            @Override
            public void onSuccess(String identifier) {
                ChatActivity.navToChat(getActivity(), "slbl_serve_495", TIMConversationType.C2C);
            }

            @Override
            public void onFail(String msg, int code2) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).changeTabStatusColor(1);
    }

    @Override
    protected void initData() {

        TencentCloud.getConversation(new TencentCloud.ConversationListener() {
            @Override
            public void updateMessage(TIMMessage message) {

            }

            @Override
            public void onComplete(List<TIMConversation> result) {
                initView(result);
            }
        });

    }


    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        TencentCloud.getFriendshipLastMessage(this);
        TencentCloud.getGroupManageLastMessage(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initViewData() {
        //topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(getActivity())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {

    }

    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {

    }

    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {

    }

    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }
}
