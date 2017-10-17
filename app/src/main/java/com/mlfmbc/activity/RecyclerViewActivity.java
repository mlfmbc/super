package com.mlfmbc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mlfmbc.R;
import com.mlfmbc.widget.SimpleFooterView;
import com.mlfmbc.widget.SimpleHeaderView;
import com.mlfmbc.widget.SuperLayout;

/**
 * Created by chang on 2017/10/17.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    private SuperLayout superLayout;
    public static void onStart(Context context){
        Intent intent=new Intent(context,RecyclerViewActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        superLayout= (SuperLayout) findViewById(R.id.superLayout);
        superLayout.addHeaderView(new SimpleHeaderView(this));
        superLayout.addfooterView(new SimpleFooterView(this));
    }
}
