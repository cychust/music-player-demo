package com.example.cyc.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;


import com.example.cyc.myapplication.R;
//import com.umeng.analytics.MobclickAgent;


/**
 * Created by cyc on 17-9-7.
 */

public class WelcomeActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,MusicListActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        },1500);
    }
}
