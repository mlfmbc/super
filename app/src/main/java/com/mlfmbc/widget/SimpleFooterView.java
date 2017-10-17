package com.mlfmbc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mlfmbc.R;

/**
 * Created by chang on 2017/10/11.
 */

public class SimpleFooterView extends LinearLayout implements FooterViewInterface{
    private TextView title;
    private static final String TAG = "SimpleFooterView";
    public SimpleFooterView(Context context) {
        this(context,null,0);
    }

    public SimpleFooterView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(LayoutInflater.from(getContext()).inflate(R.layout.simple_footer,null),new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);

        title= (TextView) findViewById(R.id.title);
        Log.e(TAG,getMeasuredHeight()+"");
//        setPadding(0, -getMeasuredHeight(), 0,0);
        Log.e(TAG,getMeasuredHeight()+"");
    }

    @Override
    public void onPullUpLoading() {
        title.setText("上拉加载更多");
    }

    @Override
    public void onLoosenLoad() {
        title.setText("松手加载更多");
    }

    @Override
    public void onLoading() {
        title.setText("加载中...");
    }

    @Override
    public void onLoadEnd() {
        title.setText("加载结束");
    }

    @Override
    public void onPullUpProgressDiffY(int refreshDiffY) {
//        Log.e(TAG+"下拉加载",refreshDiffY+"");
    }

    @Override
    public void onPullUpResumeProgressDiffY(int refreshDiffY) {
//        Log.e(TAG+"松手回弹",refreshDiffY+"");
    }
}
