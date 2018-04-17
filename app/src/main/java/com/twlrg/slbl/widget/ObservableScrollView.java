package com.twlrg.slbl.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 作者：王先云 on 2018/4/17 21:57
 * 邮箱：wangxianyun1@163.com
 * 描述：一句话简单描述
 */
public class ObservableScrollView extends ScrollView
{
    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context)
    {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener)
    {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy)
    {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null)
        {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener
    {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
