package com.sheaffer.myapplication.utils;

import android.content.Context;

public class ResourceProvider {
    public static volatile ResourceProvider instance;
    private final Context context;

    public ResourceProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ResourceProvider getInstance(Context context) {
        if (instance == null) {
            synchronized (ResourceProvider.class) {
                if (instance == null) {
                    instance = new ResourceProvider(context);
                }
            }
        }
        return instance;
    }

    public String getString(int resId) {
        return context.getString(resId);
    }

    public String getString(int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }
}
