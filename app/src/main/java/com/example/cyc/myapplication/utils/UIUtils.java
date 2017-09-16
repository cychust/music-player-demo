package com.example.cyc.myapplication.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;

import com.example.cyc.myapplication.R;

/**
 * Created by cyc on 17-9-7.
 */

public class UIUtils {
    public static void setSystemBarTintColor(Activity activity){
        if(SystemBarTintManager.isKitKat()){
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintDrawable(new ColorDrawable(activity.getResources().
                    getColor(R.color.syetem_bar_color)));
        }
    }
}
