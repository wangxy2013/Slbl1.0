package com.twlrg.slbl.im;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2018/05/11 5.6.6 
 */

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.imcore.FriendGroupVec;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.group.TIMGroupPendencyGetParam;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.group.TIMGroupPendencyListGetSucc;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureMeta;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.imsdk.ext.sns.TIMGetFriendFutureListSucc;
import com.tencent.imsdk.ext.sns.TIMPageDirectionType;
import com.tencent.qalsdk.QALSDKManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.twlrg.slbl.R;
import com.twlrg.slbl.im.model.UserInfo;
import com.twlrg.slbl.im.utils.Foreground;
import com.twlrg.slbl.utils.ConfigManager;
import com.twlrg.slbl.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

public class TencentCloud {


    public static final String UID_PREFIX = "slbl_client_";
    public static final String UID_PREFIX_SERVER = "slbl_server_";
    private static final String PASSWORD = "slbl123456";

    private static final String TAG = "TencentCloud";

    public static void init(final Application applicationContext) {


        Foreground.init(applicationContext);

        if (MsfSdkUtils.isMainProcess(applicationContext)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(applicationContext, R.drawable.ic_launcher);
                    }
                }
            });
        }

    }


    public static void getConversation(final ConversationListener listener) {
        final String userID = ConfigManager.instance().getUserID();
        LogUtil.d(TAG, userID + "获取消息列表");
        if (TextUtils.isEmpty(userID)) {
            //未登录
            LogUtil.d(TAG, "未登录，获取消息列表失败");
            listener.onComplete(new ArrayList<TIMConversation>());
            return;
        }

        final String identifier = ConfigManager.instance().getIdentifier();
        TLSHelper instance = TLSHelper.getInstance();
        if (!instance.needLogin(identifier)) {
            LogUtil.d(TAG, "已登录腾讯云");
            getConversationInner(listener);
            return;
        }
        login(identifier, new LoginListener() {
            @Override
            public void onSuccess(String identifier) {
                getConversation(listener);
            }

            @Override
            public void onFail(String msg, int code) {
                if (code == 229) {
                    //账号未注册
                    LogUtil.d(TAG, identifier + " not register");
                    register(userID, new LoginListener() {
                        @Override
                        public void onSuccess(String identifier) {
                            LogUtil.d(TAG, identifier + " register success ");
                            getConversation(listener);
                        }

                        @Override
                        public void onFail(String msg, int code2) {
                            LogUtil.d(TAG, " register fail " + msg);
                        }
                    });
                } else {
                    listener.onComplete(new ArrayList<TIMConversation>());
                    LogUtil.d(TAG, code + "-->获取消息列表失败:" + msg);
                }
            }
        });

    }

    /**
     * 获取好友关系链最后一条消息,和未读消息数
     * 包括：好友已决系统消息，好友未决系统消息，推荐好友消息
     */
    public static void getFriendshipLastMessage(final FriendshipListener listener) {
        TIMFriendFutureMeta meta = new TIMFriendFutureMeta();
        meta.setReqNum(1);
        meta.setDirectionType(TIMPageDirectionType.TIM_PAGE_DIRECTION_DOWN_TYPE);
        long reqFlag = 0;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK;
        reqFlag |= TIMFriendshipManager.TIM_PROFILE_FLAG_ALLOW_TYPE;

        long futureFlags = TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_DECIDE_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE
                | TIMFriendshipManagerExt.TIM_FUTURE_FRIEND_RECOMMEND_TYPE;

        TIMFriendshipManagerExt.getInstance().getFutureFriends(reqFlag, futureFlags, null, meta, new TIMValueCallBack<TIMGetFriendFutureListSucc>() {

            @Override
            public void onError(int arg0, String arg1) {
                Log.i(TAG, "onError code" + arg0 + " msg " + arg1);
            }

            @Override
            public void onSuccess(TIMGetFriendFutureListSucc arg0) {
                long unread = arg0.getMeta().getPendencyUnReadCnt() +
                        arg0.getMeta().getDecideUnReadCnt() +
                        arg0.getMeta().getRecommendUnReadCnt();
                if (listener != null && arg0.getItems().size() > 0) {
                    listener.onGetFriendshipLastMessage(arg0.getItems().get(0), unread);
                }
            }

        });
    }


    /**
     * 获取群管理最有一条消息,和未读消息数
     * 包括：加群等已决和未决的消息
     */
    public static void getGroupManageLastMessage(final GroupManageListener messageView) {
        TIMGroupPendencyGetParam param = new TIMGroupPendencyGetParam();
        param.setNumPerPage(1);
        param.setTimestamp(0);
        TIMGroupManagerExt.getInstance().getGroupPendencyList(param, new TIMValueCallBack<TIMGroupPendencyListGetSucc>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError code" + i + " msg " + s);
            }

            @Override
            public void onSuccess(TIMGroupPendencyListGetSucc timGroupPendencyListGetSucc) {
                if (messageView != null && timGroupPendencyListGetSucc.getPendencies().size() > 0) {
                    messageView.onGetGroupManageLastMessage(timGroupPendencyListGetSucc.getPendencies().get(0),
                            timGroupPendencyListGetSucc.getPendencyMeta().getUnReadCount());
                }
            }
        });
    }


    public static void login(String identifier, final LoginListener listener) {
        TLSHelper instance = TLSHelper.getInstance();


        //        if (!instance.needLogin(account)) {
        //            listener.onSuccess(account);`
        //            return;
        //        }

        LogUtil.d(TAG, identifier + " login TLS");

        instance.TLSPwdLogin(identifier, PASSWORD.getBytes(), new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                LogUtil.d(TAG, "TLS Success");
                TLSService tlsService = TLSService.getInstance();
                UserInfo.getInstance().setUserSig(tlsService.getUserSig(tlsUserInfo.identifier));
                UserInfo.getInstance().setId(tlsUserInfo.identifier);
                listener.onSuccess(tlsUserInfo.identifier);
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
                LogUtil.d(TAG, "TLS OnPwdLoginReaskImgCodeSuccess");
                listener.onFail("OnPwdLoginReaskImgCodeSuccess", -1);
            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
                String error = TencentCloud.toString(tlsErrInfo);
                LogUtil.d(TAG, "TLS failed:" + error);

                listener.onFail(error, tlsErrInfo.ErrCode);
            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {
                String error = TencentCloud.toString(tlsErrInfo);
                LogUtil.d(TAG, "TLS failed:" + error);

                listener.onFail(error, tlsErrInfo.ErrCode);
            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
                String error = TencentCloud.toString(tlsErrInfo);
                LogUtil.d(TAG, "TLS failed:" + error);

                listener.onFail(error, tlsErrInfo.ErrCode);
            }
        });


    }

    private static void register(String uid, final LoginListener listener) {
        String account = UID_PREFIX + uid;
        LogUtil.d(TAG, "register " + account);
        TLSHelper instance = TLSHelper.getInstance();
        if (!instance.needLogin(account)) {
            listener.onSuccess(account);
            return;
        }

        LogUtil.d(TAG, "register " + account);

        instance.TLSStrAccReg(account, PASSWORD, new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
                LogUtil.d(TAG, "OnStrAccRegSuccess:" + tlsUserInfo.identifier + "");
                listener.onSuccess(tlsUserInfo.identifier);
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
                LogUtil.d(TAG, "OnStrAccRegFail:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                listener.onFail(TencentCloud.toString(tlsErrInfo), tlsErrInfo.ErrCode);

            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
                LogUtil.d(TAG, "OnStrAccRegTimeout:" + tlsErrInfo.Msg + " " + tlsErrInfo.ExtraMsg);
                listener.onFail(TencentCloud.toString(tlsErrInfo), tlsErrInfo.ErrCode);
            }
        });


    }

    private static void getConversationInner(final ConversationListener listener) {
        List<TIMConversation> list = TIMManagerExt.getInstance().getConversationList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : list) {
            if (conversation.getType() == TIMConversationType.System) continue;
            result.add(conversation);
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "get message error " + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        TIMMessage message = timMessages.get(0);
                        listener.updateMessage(message);
                    }

                }
            });

        }
        LogUtil.d(TAG, result.toString());
        LogUtil.d(TAG, "获取消息列表结束");
        listener.onComplete(result);
    }

    public static void IMLogin(final String identifier, final LoginListener listener) {
        LogUtil.d(TAG, "IMLogin");
        login(identifier, new LoginListener() {
            @Override
            public void onSuccess(final String identifier) {

                String userSig = TLSService.getInstance().getUserSig(identifier);
                LoginBusiness.loginIm(identifier, userSig, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        LogUtil.d(TAG, "IM onError:" + s);
                        listener.onFail(s, i);
                    }

                    @Override
                    public void onSuccess() {
                        LogUtil.d(TAG, "IM onSuccess:");
                        listener.onSuccess(identifier);

                    }
                });
            }

            @Override
            public void onFail(String msg, int code2) {

            }
        });

    }

    public static void logout() {
        TlsBusiness.logout(TLSService.getInstance().getLastUserIdentifier());

        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG, "onError:" + s);
            }

            @Override
            public void onSuccess() {
                LogUtil.d(TAG, "onSuccess:");
            }
        });
    }

    public static String toString(TLSErrInfo errInfo) {
        if (errInfo == null) return "null";
        return "code:" + errInfo.ErrCode + " Msg:" + errInfo.Msg + " extra:" + errInfo.ExtraMsg;
    }

    public interface ConversationListener {
        void updateMessage(TIMMessage message);

        void onComplete(List<TIMConversation> result);
    }

    public interface LoginListener {
        void onSuccess(String identifier);

        void onFail(String msg, int code2);
    }

    /**
     * 好友关系链管理消息接口
     */
    public interface FriendshipListener {


        /**
         * 获取好友关系链管理最后一条系统消息的回调
         *
         * @param message     最后一条消息
         * @param unreadCount 未读数
         */
        void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount);

        /**
         * 获取好友关系链管理最后一条系统消息的回调
         *
         * @param message 消息列表
         */
        void onGetFriendshipMessage(List<TIMFriendFutureItem> message);


    }

    /**
     * 群管理消息接口
     */
    public interface GroupManageListener {

        /**
         * 获取群管理最后一条系统消息的回调
         *
         * @param message     最后一条消息
         * @param unreadCount 未读数
         */
        void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount);

        /**
         * 获取群管理系统消息的回调
         *
         * @param message 分页的消息列表
         */
        void onGetGroupManageMessage(List<TIMGroupPendencyItem> message);
    }

}
