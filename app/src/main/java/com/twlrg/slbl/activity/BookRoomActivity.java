package com.twlrg.slbl.activity;

        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.suke.widget.SwitchButton;
        import com.twlrg.slbl.R;
        import com.twlrg.slbl.utils.APPUtils;
        import com.twlrg.slbl.utils.LogUtil;

        import butterknife.BindView;
        import butterknife.ButterKnife;

/**
 * 作者：王先云 on 2018/4/23 16:48
 * 邮箱：wangxianyun1@163.com
 * 描述：房间预订
 */
public class BookRoomActivity extends BaseActivity
{
    @BindView(R.id.topView)
    View         topView;
    @BindView(R.id.iv_back)
    ImageView    ivBack;
    @BindView(R.id.tv_title)
    TextView     tvTitle;
    @BindView(R.id.tv_buynum)
    TextView     tvBuynum;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.btn_submit)
    Button       btnSubmit;

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_book_room);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
    }

    @Override
    protected void initViewData()
    {


        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked)
            {
                LogUtil.e("TAG", "isChecked--->" + isChecked);
                switchButton.setChecked(isChecked);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
