package com.sheaffer.myapplication;

import android.app.Application;

import com.sheaffer.myapplication.utils.ResourceProvider;

public class RpnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ResourceProvider.getInstance(this);
    }
}
