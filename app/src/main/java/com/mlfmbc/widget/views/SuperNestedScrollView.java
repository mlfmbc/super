package com.mlfmbc.widget.views;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.mlfmbc.widget.ContentViewInterface;

/**
 * Created by chang on 2017/10/16.
 */

public class SuperNestedScrollView extends NestedScrollView implements ContentViewInterface{
    public SuperNestedScrollView(Context context) {
        this(context,null,0);
    }

    public SuperNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isToTop() {
        return getScrollY() == 0;
    }

    @Override
    public boolean isToBottom() {
        return isNestedScrollViewToBottom(this);
    }
    public static boolean isNestedScrollViewToBottom(NestedScrollView scrollView) {
        if (scrollView != null) {
            View childView = scrollView.getChildAt(0);
            return  childView.getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight();
        }
        return false;
    }
}
