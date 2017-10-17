package com.mlfmbc.widget.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mlfmbc.R;
import com.mlfmbc.widget.ContentViewInterface;
import com.mlfmbc.widget.SuperLayout;

/**
 * Created by chang on 2017/10/16.
 */

public class SuperCoordinatorLayout extends CoordinatorLayout implements ContentViewInterface {
    private static final String TAG = "SuperCoordinatorLayout";
    private AppBarLayout appbar;
    private SuperNestedScrollView superNestedScrollView;
    private SuperLayout superLayout;
    public SuperCoordinatorLayout(Context context) {
        this(context,null,0);
    }

    public SuperCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
private boolean isToTop,isToBottom;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        superNestedScrollView= (SuperNestedScrollView) findViewById(R.id.SuperNestedScrollView);
        superLayout= (SuperLayout) getParent();
        appbar = (AppBarLayout) findViewById(R.id.app_bar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.e(TAG,verticalOffset+"");
                isToTop=verticalOffset==0;
            }
        });
    }

    @Override
    public boolean isToTop() {
        return isToTop;
//        !canScrollVertically(-1)
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG,"onTouchEvent");
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean isToBottom() {
        return false;
        //||superNestedScrollView.isToBottom()
//        !canScrollVertically(1)
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && dyUnconsumed == 0) {
            Log.e("上滑中。。。", "上滑中。。。");
        }
        isToBottom=dyConsumed == 0 && dyUnconsumed > 0;
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            Log.e(TAG,"onNestedScroll---"+dyUnconsumed);
            Log.e("到边界了还在上滑。。。", "到边界了还在上滑。。。");
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            Log.e("下滑中。。。", "下滑中。。。");
        }
        isToTop=dyConsumed == 0 && dyUnconsumed < 0;
        if (dyConsumed == 0 && dyUnconsumed < 0) {

            Log.e("到边界了，还在下滑。。。", "到边界了，还在下滑。。。");
        }
    }
}
