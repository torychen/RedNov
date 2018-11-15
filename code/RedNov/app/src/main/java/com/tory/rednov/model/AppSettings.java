package com.tory.rednov.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tory.rednov.controller.Listener;

public class AppSettings {
    private final static String KEY_DEBUG_FLAG = "KEY_DEBUG_FLAG";

    private Listener listener;


    private boolean debugFlag;
    private Context context;

    public AppSettings (Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public  boolean getDebugFlag () {
        return debugFlag;
    }

    public void  setDebugFlag (boolean debugFlag) {
        this.debugFlag = debugFlag;
    }

    public void loadDebugFlag() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        debugFlag = sp.getBoolean(KEY_DEBUG_FLAG, false);
    }

    public void saveDebugFlag() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_DEBUG_FLAG, debugFlag);
        editor.commit();
    }


    public void informListener() {
        listener.update(this);
    }


}
