package com.lcgsen.utils;

import android.app.Application;

public class App extends Application {

    public final static String TAG = "x024";
    public final static String ERROR_FILENAME = "x024_error.log";
    public final static String XPOSED_FILENAME = "sena_xposed.log";

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHanlder.getInstance().init(this);

    }
}
