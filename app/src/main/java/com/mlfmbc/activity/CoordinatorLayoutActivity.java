package com.mlfmbc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.mlfmbc.R;
import com.mlfmbc.widget.SimpleFooterView;
import com.mlfmbc.widget.SimpleHeaderView;
import com.mlfmbc.widget.SuperLayout;

/**
 * Created by chang on 2017/10/17.
 */

public class CoordinatorLayoutActivity  extends AppCompatActivity {
    private SuperLayout superLayout,superLayout1;
    public static void onStart(Context context){
        Intent intent=new Intent(context,CoordinatorLayoutActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinatorlayout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        superLayout= (SuperLayout) findViewById(R.id.superLayout);
        superLayout.addHeaderView(new SimpleHeaderView(this));
        superLayout.addfooterView(new SimpleFooterView(this));

        superLayout1= (SuperLayout) findViewById(R.id.superLayout1);
//        superLayout1.addHeaderView(new SimpleHeaderView(this));
        superLayout1.addfooterView(new SimpleFooterView(this));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
