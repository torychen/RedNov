package com.tory.rednov.model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class IPCApplication extends Application {
    private static AppSettings appSettings;
    private static List<IPCamera> ipCameraList;

    public static AppSettings getAppSettings() {
        return appSettings;
    }

    public static List<IPCamera> getIpCamera() {
        return ipCameraList;
    }

    @Override
    public void onCreate() {
        appSettings = new AppSettings();
        ipCameraList = new ArrayList<>();
        super.onCreate();
    }
}
