package com.mlfmbc.widget.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mlfmbc.widget.ContentViewInterface;

/**
 * Created by chang on 2017/10/16.
 */

public class SuperRecyclerView extends RecyclerView implements ContentViewInterface{

    public SuperRecyclerView(Context context) {
        this(context,null,0);
    }

    public SuperRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isToTop() {
        return !canScrollVertically(-1);
    }

    @Override
    public boolean isToBottom() {
        return !canScrollVertically(1);
    }
}
