package com.twlrg.slbl.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.twlrg.slbl.R;
import com.twlrg.slbl.adapter.ReplyAdapter;
import com.twlrg.slbl.entity.CommentInfo;
import com.twlrg.slbl.listener.MyItemClickListener;
import com.twlrg.slbl.utils.Urls;
import com.twlrg.slbl.widget.DividerDecoration;
import com.twlrg.slbl.widget.FullyLinearLayoutManager;


/**
 * Date:
 */
public class CommentHolder extends RecyclerView.ViewHolder
{
    private TextView mReplyTv;

    private ImageView           mUserPicIv;
    private RecyclerView        mRecyclerView;
    private TextView            mUserNameTv;
    private TextView            mContentTv;
    private TextView            mTimeTv;

    public CommentHolder(View rootView, Context mContext)
    {
        super(rootView);
        mReplyTv = (TextView) rootView.findViewById(R.id.tv_reply);
        mUserNameTv = (TextView) rootView.findViewById(R.id.tv_user_name);
        mContentTv = (TextView) rootView.findViewById(R.id.tv_content);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_reply);
        mUserPicIv = (ImageView) rootView.findViewById(R.id.iv_user_head);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerDecoration(mContext));

    }


    public void setCommentInfo(CommentInfo mCommentInfo)
    {
        ImageLoader.getInstance().displayImage(Urls.getImgUrl(mCommentInfo.getPortrait()), mUserPicIv);
        mUserNameTv.setText(mCommentInfo.getNickname());
        mContentTv.setText(mCommentInfo.getContent());
        mTimeTv.setText(mCommentInfo.getCreate_time());


            mReplyTv.setVisibility(View.GONE);

        mRecyclerView.setAdapter(new ReplyAdapter(mCommentInfo.getReplyInfoList()));
    }
}
