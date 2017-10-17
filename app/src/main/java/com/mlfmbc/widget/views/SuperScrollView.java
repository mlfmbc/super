package com.mlfmbc.widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.mlfmbc.util.RefreshScrollingUtil;
import com.mlfmbc.widget.ContentViewInterface;

/**
 * Created by chang on 2017/10/16.
 */

public class SuperScrollView extends ScrollView implements ContentViewInterface {
    public SuperScrollView(Context context) {
        this(context,null,0);
    }

    public SuperScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isToTop() {
        return RefreshScrollingUtil.isScrollViewOrWebViewToTop(this);
    }

    @Override
    public boolean isToBottom() {
        return RefreshScrollingUtil.isScrollViewToBottom(this);
    }
}
