package com.tory.rednov.view;

public class IPCamItem {
    private String ip;
    private int imageId;
    private int position;//map to the position in devices list.

    public IPCamItem(String ip, int imageId, int pos) {
        this.ip = ip;
        this.imageId = imageId;
        this.position = pos;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }

    public int getImageId() {
        return imageId;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
