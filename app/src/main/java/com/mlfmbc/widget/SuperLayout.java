package com.mlfmbc.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by chang on 2017/10/11.
 */

public class SuperLayout extends LinearLayout {
    private static final String TAG = "SuperLayout";
    private HeaderViewInterface headerViewInterface;// 头部
    private ContentViewInterface contentViewInterface;//内容
    private FooterViewInterface footerViewInterface;// 底部
    private View headerView, footerView;
    private View mContentView;
    private int mTouchSlop;
    private boolean isAnimating;// 正在动画中

    public SuperLayout(Context context) {
        this(context, null);
    }

    public SuperLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 下拉刷新控件的高度
     */
    private int mRefreshHeaderViewHeight, mRefreshFooterViewHeight;

    //添加头部
    public void addHeaderView(View headerView) {
        if (headerView instanceof HeaderViewInterface) {
            if (this.headerView != null) {
                removeView(this.headerView);
            }
            this.headerView = headerView;
            headerView.measure(0,0);
            headerViewInterface = (HeaderViewInterface) headerView;
            mRefreshHeaderViewHeight = headerView.getMeasuredHeight();
            mMinWholeHeaderViewPaddingTop = -mRefreshHeaderViewHeight;
            mMaxWholeHeaderViewPaddingTop = (int) (mRefreshHeaderViewHeight * PULL_DISTANCE_SCALE);
            addView(headerView, 0);
        } else {
            throw new RuntimeException(SuperLayout.class.getSimpleName() + " addHeaderView" + "必须继承HeaderViewInterface");
        }

    }

    // 添加底部
    public void addfooterView(View footerView) {
        if (footerView instanceof FooterViewInterface) {
            if (this.footerView != null) {
                removeView(this.footerView);
            }
            this.footerView = footerView;
            footerView.measure(0,0);
            footerViewInterface = (FooterViewInterface) footerView;
            mRefreshFooterViewHeight = footerView.getMeasuredHeight();
            mMinWholeFooterViewPaddingBottom = -mRefreshFooterViewHeight;
            mMaxWholeFooterViewPaddingBottom = (int) (mRefreshFooterViewHeight * PULL_DISTANCE_SCALE);
        } else {
            throw new RuntimeException(SuperLayout.class.getSimpleName() + " addfooterView" + "必须继承FooterViewInterface");
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        int finalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int paddingLeft = getPaddingLeft();
                final int paddingRight = getPaddingRight();
                final int paddingTop = getPaddingTop();
                final int paddingBottom = getPaddingBottom();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                int childWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - lp.leftMargin - lp.rightMargin - paddingLeft - paddingRight, MeasureSpec.EXACTLY);
                int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                        paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin, lp.height);
                child.measure(childWidthSpec, childHeightSpec);
                finalHeight += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
        }
        setMeasuredDimension(width, finalHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t2, int r, int b) {

        int childCount = getChildCount();
        int top = getPaddingTop();
        int adHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams margins = (LayoutParams) child.getLayoutParams();
            int topMargin = margins.topMargin;
            int bottomMargin = margins.bottomMargin;
            int leftMargin = margins.leftMargin;
            int rightMargin = margins.rightMargin;
            l = leftMargin + getPaddingLeft();
            top += topMargin;
            r = child.getMeasuredWidth();
            if (child.getVisibility() != View.GONE) {
                if (i == 0&&headerView!=null) {
                    adHeight = child.getMeasuredHeight() - headerView.getMeasuredHeight();
                    child.layout(l, top - headerView.getMeasuredHeight(), l + r, top + adHeight);
                    top += adHeight;
                } else if (i == 1&&headerView!=null) {
                    int childHeight = child.getMeasuredHeight() - adHeight;
                    int bottom = childHeight + top;
                    child.layout(l, top, l + r, bottom);
                    top += childHeight + bottomMargin;
                } else {
                    int bottom = child.getMeasuredHeight() + top;
                    child.layout(l, top, l + r, bottom);
                    top += child.getMeasuredHeight();
                }
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new RuntimeException(SuperLayout.class.getSimpleName() + "必须有且只有一个子控件");
        }
        mContentView = getChildAt(0);
        if(mContentView instanceof ContentViewInterface){
            contentViewInterface= (ContentViewInterface) mContentView;
        }else{
            throw new RuntimeException(SuperLayout.class.getSimpleName() + " ContentView" + "必须继承ContentViewInterface");
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

    private float mInterceptTouchDownX = -1;
    private float mInterceptTouchDownY = -1;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e(TAG+"TouchEvent", "onInterceptTouchEvent");
      boolean onIntercept=  super.onInterceptTouchEvent(event);
        if(isAnimating){
            event.setAction(MotionEvent.ACTION_CANCEL);
            super.onInterceptTouchEvent(event);
            mInterceptTouchDownX = -1;
            mInterceptTouchDownY = -1;
            mWholeHeaderDownY = -1;
            mRefreshDownY = -1;

            return false;
        }else{
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mInterceptTouchDownX = event.getRawX();
                    mInterceptTouchDownY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mInterceptTouchDownX == -1) {
                        mInterceptTouchDownX = (int) event.getRawX();
                    }
                    if (mInterceptTouchDownY == -1) {
                        mInterceptTouchDownY = (int) event.getRawY();
                    }

                    int interceptTouchMoveDistanceY = (int) (event.getRawY() - mInterceptTouchDownY);
                    // 可以没有上拉加载更多，但是必须有下拉刷新，否则就不拦截事件
                    if (Math.abs(event.getRawX() - mInterceptTouchDownX) < Math.abs(interceptTouchMoveDistanceY)
                            && (footerView != null || headerView != null)&&!isAnimating
                            ) {
                        if (shouldHandleLoadingMore()) {
                            Log.e(TAG, "shouldHandleLoadingMore");
                        }
                        if ((interceptTouchMoveDistanceY > mTouchSlop && shouldHandleRefresh())// 继续向下滑动
                                || (interceptTouchMoveDistanceY < -mTouchSlop && shouldHandleLoadingMore())// 继续向上滑动
                                ) {
//                        if (headerViewInterface != null) headerViewInterface.onPullDownLoading();
//                        if (footerViewInterface != null) footerViewInterface.onPullUpLoading();

                            event.setAction(MotionEvent.ACTION_CANCEL);
                            super.onInterceptTouchEvent(event);
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // 重置
                    mInterceptTouchDownX = -1;
                    mInterceptTouchDownY = -1;
                    break;
            }
        }

        return onIntercept;
    }

    /**
     * 是否满足处理刷新的条件
     *
     * @return
     */
    private boolean shouldHandleRefresh() {
        if (headerView == null||contentViewInterface==null) return false;
        return contentViewInterface.isToTop();
    }

    /**
     * 是否满足处理刷新的条件
     *
     * @return
     */
    private boolean shouldHandleLoadingMore() {
        if (footerView == null||contentViewInterface==null) return false;
        return contentViewInterface.isToBottom();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG+"TouchEvent", "dispatchTouchEvent");
        if(isAnimating)return false;
        if (!isWholeHeaderViewCompleteInvisible() || !isWholeFooterViewCompleteInvisible()&&!isAnimating) {
            super.dispatchTouchEvent(ev);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 手指按下时，y轴方向的偏移量
     */
    private int mWholeHeaderDownY = -1;
    /**
     * 记录开始下拉刷新时的downY
     */
    private int mRefreshDownY = -1;
    /**
     * 下拉刷新是否可用
     */
    private boolean mPullDownRefreshEnable = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG+"TouchEvent", "onTouchEvent");
        if (null != headerView||null!=footerView) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mWholeHeaderDownY = (int) event.getY();

                    if (!isAnimating&&isWholeHeaderViewCompleteInvisible() || isWholeFooterViewCompleteInvisible()) {
                        mRefreshDownY = (int) event.getY();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isAnimating&&handleActionMove(event)) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (!isAnimating&&handleActionUpOrCancel(event)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 处理手指抬起事件
     *
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionUpOrCancel(MotionEvent event) {
        boolean isReturnTrue = false;
        // 如果当前头部刷新控件没有完全隐藏，则需要返回true，自己消耗ACTION_UP事件
        if (headerView!=null&&headerView.getTranslationY() != 0) {
            isReturnTrue = true;
        }
        // 处于下拉刷新状态，松手时隐藏下拉刷新控件
        if ((headerView != null && headerView.getTranslationY() > 0 && headerView.getTranslationY() < Math.abs(mMaxWholeHeaderViewPaddingTop))) {
            isAnimating=true;
            hiddenRefreshHeaderView();
        }
        if ((headerView != null && headerView.getTranslationY() > 0 && headerView.getTranslationY() >= Math.abs(mMaxWholeHeaderViewPaddingTop))) {
            // 处于松开进入刷新状态，松手时完全显示下拉刷新控件，进入正在刷新状态
            isAnimating=true;
            changeRefreshHeaderViewToZero();
        }
        if (footerView!=null&&footerView.getTranslationY() != 0) {
            isReturnTrue = true;
        }
        // 处于下拉刷新状态，松手时隐藏下拉刷新控件
        if ((footerView != null && footerView.getTranslationY() < 0 && footerView.getTranslationY() > -Math.abs(mMaxWholeFooterViewPaddingBottom))) {
            isAnimating=true;
            hiddenRefreshFooterView();
        }
        if ((footerView != null && footerView.getTranslationY() < 0 && footerView.getTranslationY() <= -Math.abs(mMaxWholeFooterViewPaddingBottom))) {
            // 处于松开进入刷新状态，松手时完全显示下拉刷新控件，进入正在刷新状态
            isAnimating=true;
            changeRefreshFooterViewToZero();
        }
        mWholeHeaderDownY = -1;
        mRefreshDownY = -1;
        return isReturnTrue;
    }

    /**
     * 结束下拉刷新
     */
    public void endRefreshing() {
        if (mContentView != null && mContentView.getTranslationY() > 0)
            if (headerViewInterface != null) headerViewInterface.onLoadEnd();
        if (mContentView != null && mContentView.getTranslationY() < 0)
            if (footerViewInterface != null) footerViewInterface.onLoadEnd();
        hiddenRefreshHeaderView();
        hiddenRefreshFooterView();
    }

    /**
     * 头部控件移动动画时常
     */
    private int mTopAnimDuration = 500;

    /**
     * 隐藏下拉刷新控件，带动画
     */
    private void hiddenRefreshHeaderView() {
        if(headerView==null)return;
        final int from = (int) headerView.getTranslationY();
        if (from <= 0) return;
        int to = 0;
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(mTopAnimDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int refreshDiffY = (int) animation.getAnimatedValue();
                if (headerViewInterface != null)
                    headerViewInterface.onPullDownResumeProgressDiffY(refreshDiffY);
                onMove(refreshDiffY);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isAnimating=false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void hiddenRefreshFooterView() {
        if(footerView==null)return;
        int from = (int) footerView.getTranslationY();
        if (from >= 0) return;
        int to = 0;
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(mTopAnimDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int refreshDiffY = (int) animation.getAnimatedValue();
                if (footerViewInterface != null)
                    footerViewInterface.onPullUpResumeProgressDiffY(refreshDiffY);
                onMove(refreshDiffY);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isAnimating=false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    /**
     * 设置下拉刷新控件的paddingTop到0，带动画
     */
    private void changeRefreshHeaderViewToZero() {
        int from = (int) headerView.getTranslationY();
        if (from <= 0) return;
        int to = from > 0 ? Math.abs(mMinWholeHeaderViewPaddingTop) : -Math.abs(mMinWholeHeaderViewPaddingTop);
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(mTopAnimDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int refreshDiffY = (int) animation.getAnimatedValue();
                if (headerViewInterface != null) headerViewInterface.onLoading();
                if (headerViewInterface != null)
                    headerViewInterface.onPullDownResumeProgressDiffY(refreshDiffY);
                onMove(refreshDiffY);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                endRefreshing();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void changeRefreshFooterViewToZero() {
        int from = (int) footerView.getTranslationY();
        if (from >= 0) return;
        int to = from > 0 ? Math.abs(mMinWholeFooterViewPaddingBottom) : -Math.abs(mMinWholeFooterViewPaddingBottom);
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(mTopAnimDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int refreshDiffY = (int) animation.getAnimatedValue();
                if (footerViewInterface != null) footerViewInterface.onLoading();
                if (footerViewInterface != null)
                    footerViewInterface.onPullUpResumeProgressDiffY(refreshDiffY);
                onMove(refreshDiffY);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                endRefreshing();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值
     */
    private static final float PULL_DISTANCE_SCALE = 1.8f;
    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值，默认1.8f
     */
    private float mPullDistanceScale = PULL_DISTANCE_SCALE;

    /**
     * 整个头部控件最小的paddingTop
     */
    private int mMinWholeHeaderViewPaddingTop, mMinWholeFooterViewPaddingBottom;
    /**
     * 整个头部控件最大的paddingTop
     */
    private int mMaxWholeHeaderViewPaddingTop, mMaxWholeFooterViewPaddingBottom;

    private void onMove(int refreshDiffY) {
        if (headerView == null && footerView == null) return;
        mContentView.setTranslationY(refreshDiffY);
        if (headerView != null) headerView.setTranslationY(refreshDiffY);
        if (footerView != null) footerView.setTranslationY(refreshDiffY);
    }

    /**
     * 处理手指滑动事件
     *
     * @param event
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionMove(MotionEvent event) {
        if (mRefreshDownY == -1) {
            mRefreshDownY = (int) event.getY();
        }
        int refreshDiffY = (int) event.getY() - mRefreshDownY;
        refreshDiffY = (int) (refreshDiffY / mPullDistanceScale);

        // 如果是向下拉，并且当前可见的第一个条目的索引等于0，才处理整个头部控件的padding
        if (refreshDiffY > 0 && shouldHandleRefresh()
                ) {
            if (refreshDiffY > Math.abs(mMaxWholeHeaderViewPaddingTop)) {
                if (headerViewInterface != null) headerViewInterface.onLoosenLoad();
            } else {
                if (headerViewInterface != null) headerViewInterface.onPullDownLoading();
            }
            if (headerViewInterface != null)
                headerViewInterface.onPullDownProgressDiffY(refreshDiffY);
            onMove(refreshDiffY);
            return true;
        }else{
            onMove(0);
        }
        if (refreshDiffY < 0 && shouldHandleLoadingMore()) {
            if (Math.abs(refreshDiffY) > Math.abs(mMaxWholeFooterViewPaddingBottom)) {
                if (footerViewInterface != null) footerViewInterface.onLoosenLoad();
            } else {
                if (footerViewInterface != null) footerViewInterface.onPullUpLoading();
            }
            if (footerViewInterface != null)
                footerViewInterface.onPullUpProgressDiffY(refreshDiffY);

            onMove(refreshDiffY);
            return true;
        }else{
            onMove(0);
        }
        return false;
    }

    /**
     * 整个头部控件是否已经完全隐藏
     *
     * @return true表示完全隐藏，false表示没有完全隐藏
     */
    private boolean isWholeHeaderViewCompleteInvisible() {
        if (headerView != null) {
            // 0表示x，1表示y
            int[] location = new int[2];
            getLocationOnScreen(location);
            int mOnScreenY = location[1];

            headerView.getLocationOnScreen(location);
            int wholeHeaderViewOnScreenY = location[1];
            if (wholeHeaderViewOnScreenY + headerView.getMeasuredHeight() <= mOnScreenY) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isWholeFooterViewCompleteInvisible() {
        if (footerView != null) {
            // 0表示x，1表示y
            int[] location = new int[2];
            getLocationOnScreen(location);
            int mOnScreenY = location[1];

            footerView.getLocationOnScreen(location);
            int wholeHeaderViewOnScreenY = location[1];
            if (wholeHeaderViewOnScreenY + footerView.getMeasuredHeight() >= mOnScreenY) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
