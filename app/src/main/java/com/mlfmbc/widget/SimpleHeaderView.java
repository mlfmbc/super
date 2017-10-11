package com.mlfmbc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.mlfmbc.R;

/**
 * Created by chang on 2017/10/11.
 */

public class SimpleHeaderView extends LinearLayout implements HeaderViewInterface{

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
        measure(0,0);
        setPadding(0, -getMeasuredHeight(), 0, 0);
    }

}
