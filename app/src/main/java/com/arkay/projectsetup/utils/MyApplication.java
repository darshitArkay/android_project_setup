package com.arkay.projectsetup.utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.arkay.projectsetup.broadcastReceiver.ConnectionReceiver;
import com.arkay.projectsetup.sharedPreference.Prefs;


public class MyApplication extends Application {

    public static MyApplication instance = null;
    public static Context context;

    public MyApplication() {
        try {
            instance = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        instance = this;
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        RegisterReceiver();

    }

    private void RegisterReceiver() {
        ConnectionReceiver connectionReceiver = new ConnectionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);
    }

    public void setConnectivityReceiverListener(ConnectionReceiver.ConnectivityReceiverListener listener) {
        ConnectionReceiver.connectivityReceiverListener = listener;
    }
}
