package com.twlrg.slbl.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andexert.calendarlistview.library.DatePickerController;
import com.andexert.calendarlistview.library.DayPickerView;
import com.andexert.calendarlistview.library.SimpleMonthAdapter;
import com.twlrg.slbl.R;
import com.twlrg.slbl.utils.APPUtils;
import com.twlrg.slbl.utils.LogUtil;
import com.twlrg.slbl.widget.AutoFitTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：王先云 on 2018/4/26 17:08
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class DateSelectionActivity extends BaseActivity implements DatePickerController
{
    @BindView(R.id.topView)
    View            topView;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    AutoFitTextView tvTitle;
    @BindView(R.id.pickerView)
    DayPickerView   pickerView;

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_date_selection);
        setTranslucentStatus();
    }

    @Override
    protected void initEvent()
    {
        pickerView.setController(this);
    }

    @Override
    protected void initViewData()
    {
        setStatusBarTextDeep(true);
        topView.setVisibility(View.VISIBLE);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, APPUtils.getStatusBarHeight(this)));
        tvTitle.setText("日期选择");
    }

    @Override
    public int getMaxYear()
    {
        return 2025;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day)
    {
        LogUtil.e("Day Selected", day + " / " + month + " / " + year);
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays)
    {

    }


}
