package com.mlfmbc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mlfmbc.R;

/**
 * Created by chang on 2017/10/17.
 */

public class SuperMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermain);

    }
    public void onClick(View view){
 switch (view.getId()){
     case R.id.btn1:
         CoordinatorLayoutActivity.onStart(this);
         break;
     case R.id.btn2:
         NestedScrollViewActivity.onStart(this);
         break;
     case R.id.btn3:
         RecyclerViewActivity.onStart(this);
         break;
     case R.id.btn4:
         ScrollViewActivity.onStart(this);
         break;
     case R.id.btn5:
         CoordinatorLayoutActivity.onStart(this);
         break;
 }

    }
}
