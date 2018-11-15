package com.tory.rednov.model;

import android.app.Application;

public class IPCApplication extends Application {
    private static AppSettings appSettings;
    private static IPCamera ipCamera;

    public static AppSettings getAppSettings() {
        return appSettings;
    }

    public static IPCamera getIpCamera() {
        return ipCamera;
    }

    @Override
    public void onCreate() {
        appSettings = new AppSettings();
        ipCamera = new IPCamera();
        super.onCreate();
    }
}
