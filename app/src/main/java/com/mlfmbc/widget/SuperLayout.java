package com.mlfmbc.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by chang on 2017/10/11.
 */

public class SuperLayout extends LinearLayout {
    private static final String TAG = "SuperLayout";
    private HeaderViewInterface headerViewInterface;
    private FooterViewInterface footerViewInterface;
    private View headerView, footerView;
    private View mContentView;

    public SuperLayout(Context context) {
        this(context, null);
    }

    public SuperLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }

    //添加头部
    public void addHeaderView(View headerView) {
        if (headerView instanceof HeaderViewInterface) {
            if (this.headerView != null) {
                removeView(this.headerView);
            }
            this.headerView = headerView;
            addView(headerView, 0);
        }else{
            throw new RuntimeException(SuperLayout.class.getSimpleName()+" addHeaderView" + "必须继承HeaderViewInterface");
        }

    }

    // 添加底部
    public void addfooterView(View footerView) {
        if (footerView instanceof FooterViewInterface) {
            if (this.footerView != null) {
                removeView(this.footerView);
            }
            this.footerView = footerView;
        }else{
            throw new RuntimeException(SuperLayout.class.getSimpleName()+" addfooterView" + "必须继承FooterViewInterface");
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new RuntimeException(SuperLayout.class.getSimpleName() + "必须有且只有一个子控件");
        }
        mContentView = getChildAt(0);
        ((LayoutParams) mContentView.getLayoutParams()).weight = 1;
        if (mContentView instanceof CoordinatorLayout) {
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (footerView != null) {
            int count = getChildCount();
            addView(footerView, count);
        }

    }
}
