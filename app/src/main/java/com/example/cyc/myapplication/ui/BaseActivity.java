package com.example.cyc.myapplication.ui;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by cyc on 17-9-7.
 */

public class BaseActivity extends AppCompatActivity {




    @Override
    protected void onResume() {
        super.onResume();
 //       MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
   //     MobclickAgent.onPause(this);
    }
}