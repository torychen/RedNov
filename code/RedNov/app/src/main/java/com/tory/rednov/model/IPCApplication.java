package com.tory.rednov.model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class IPCApplication extends Application {
    private static AppSettings appSettings;
    //private static List<IPCam> ipCamList;

    public static AppSettings getAppSettings() {
        return appSettings;
    }

    /*
    public static List<IPCam> getIpCam() {
        return ipCamList;
    }*/

    @Override
    public void onCreate() {
        appSettings = new AppSettings();
        //ipCamList = new ArrayList<>();
        super.onCreate();
    }
}
