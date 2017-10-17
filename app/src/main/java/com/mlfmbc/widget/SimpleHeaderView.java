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

public class SimpleHeaderView extends LinearLayout implements HeaderViewInterface{
    private static final String TAG = "SimpleHeaderView";
private TextView title;
    public SimpleHeaderView(Context context) {
        this(context,null);
    }
    public SimpleHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(LayoutInflater.from(getContext()).inflate(R.layout.simple_header,null),new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        title= (TextView) findViewById(R.id.title);
    }

    @Override
    public void onPullDownLoading() {
        title.setText("下拉刷新更多");

    }

    @Override
    public void onLoosenLoad() {
        title.setText("松手刷新");

    }

    @Override
    public void onLoading() {
        title.setText("刷新中...");

    }

    @Override
    public void onLoadEnd() {
        title.setText("刷新结束");
    }

    @Override
    public void onPullDownProgressDiffY(int refreshDiffY) {
//        Log.e(TAG+"上拉刷新",refreshDiffY+"");
    }

    @Override
    public void onPullDownResumeProgressDiffY(int refreshDiffY) {
//        Log.e(TAG+"松手回弹",refreshDiffY+"");
    }


}
