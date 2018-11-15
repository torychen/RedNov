package com.tory.rednov.view;

public class IPCamItem {
    private String ip;
    private int imageId;

    public IPCamItem(String ip, int imageId) {
        this.ip = ip;
        this.imageId = imageId;
    }

    public String getIp()
    {
        return ip;
    }

    public int getImageId() {
        return imageId;
    }
}
