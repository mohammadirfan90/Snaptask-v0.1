package com.example.snaptask_v01;

import android.app.Application;
import com.example.snaptask_v01.utils.AppSessionManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize AppSessionManager globally
        AppSessionManager.init(getApplicationContext());
    }
}
