package com.mlfmbc.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by chang on 2017/10/11.
 */

public class SimpleAppBarBehavior  extends AppBarLayout.Behavior {
    private static final String TAG = "SimpleAppBarBehavior";
    private int mLastMotionY;
    private int mTouchSlop = -1;
    private int mActivePointerId = -1;
    private int dy = 0;
    private Context context;
    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;
    public AppBarBehaviorDamping getAppBarBehaviorDamping() {
        return appBarBehaviorDamping;
    }

    public void setAppBarBehaviorDamping(AppBarBehaviorDamping appBarBehaviorDamping) {
        this.appBarBehaviorDamping = appBarBehaviorDamping;
    }

    private AppBarBehaviorDamping appBarBehaviorDamping;

    //    private WeakReference<View> mLastNestedScrollingChildRef;
    public SimpleAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setDragCallback(new DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
    }
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && dyUnconsumed == 0) {
            Log.e("上滑中。。。", "上滑中。。。");
        }
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            Log.e("到边界了还在上滑。。。", "到边界了还在上滑。。。");
            Log.e(TAG, "继续上滑-"+dyUnconsumed);
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            Log.e("下滑中。。。", "下滑中。。。");
        }
        if (dyConsumed == 0 && dyUnconsumed < 0) {
            Log.e("到边界了，还在下滑。。。", "到边界了，还在下滑。。。");
            Log.e(TAG, "继续下滑-"+dyUnconsumed);
        }
    }


    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
            velocityY = velocityY * -1;
        }
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        isPositive = dy > 0;
    }
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        boolean bb = super.onInterceptTouchEvent(parent, child, ev);
        if (bb) {//&& getTopAndBottomOffset() == 0
            mLastMotionY = (int) ev.getY();
            mActivePointerId = ev.getPointerId(0);
        }
        dy = 0;
        return bb;

    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "ACTION_MOVE-");
                if (getTopAndBottomOffset() == 0) {
                    final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                            mActivePointerId);
                    if (activePointerIndex != -1) {
                        final int y = (int) MotionEventCompat.getY(ev, activePointerIndex);
                        dy = mLastMotionY - y;
                        if (dy < 0) {
                            if (dy > 0) {
                                dy -= mTouchSlop;
                            } else {
                                dy += mTouchSlop;
                            }
                            if (dy <= 0) {
                            } else {
                                dy = 0;

                            }
                            onDamping();
                            return true;
                        } else {
                            dy = 0;
                            onDamping();
                        }
                    }
                } else {
                    mLastMotionY = (int) ev.getY();
                    mActivePointerId = ev.getPointerId(0);
                    dy = 0;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (dy != 0) {
                    if (appBarBehaviorDamping != null) {
                        onDamping();
//                        if (Math.abs(dy) <= UIUtils.dip2px(context, 150)) {
                        appBarBehaviorDamping.onDampingClose(Math.abs(dy));
//                        } else {
//                            appBarBehaviorDamping.onDampingClose(UIUtils.dip2px(context, 150));
//                        }

                    }
                    return true;
                }
                break;
        }

        return super.onTouchEvent(parent, child, ev);
    }

    private void onDamping() {
        if (appBarBehaviorDamping != null) {
//            if (Math.abs(dy) <= UIUtils.dip2px(context, 150)) {
            appBarBehaviorDamping.onDamping(Math.abs(dy));
//            } else {
//                appBarBehaviorDamping.onDamping(UIUtils.dip2px(context, 150));
//            }

        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        Log.e("CoordinatorLayout", "__" + parent.getPaddingTop());
        return super.onLayoutChild(parent, abl, layoutDirection);
    }

    interface AppBarBehaviorDamping {
        void onDamping(int dy);

        void onDampingClose(int dy);
    }
}
