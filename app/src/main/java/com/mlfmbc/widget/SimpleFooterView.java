package com.mlfmbc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.mlfmbc.R;

/**
 * Created by chang on 2017/10/11.
 */

public class SimpleFooterView extends LinearLayout implements FooterViewInterface{
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
        measure(0,0);
        Log.e(TAG,getMeasuredHeight()+"");
        setPadding(0, 0, 0,-getMeasuredHeight());
        Log.e(TAG,getMeasuredHeight()+"");
    }
}
