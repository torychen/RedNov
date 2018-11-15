package com.tory.rednov.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tory.rednov.controller.Listener;

public class AppSettings {
    private boolean debugFlag;

    public  boolean getDebugFlag () {
        return debugFlag;
    }

    public void  setDebugFlag (boolean debugFlag) {
        this.debugFlag = debugFlag;
    }

}
