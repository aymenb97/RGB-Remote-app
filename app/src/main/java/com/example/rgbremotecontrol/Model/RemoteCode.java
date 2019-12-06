package com.example.rgbremotecontrol.Model;

public class RemoteCode {
    private int[] remotePattern;
    private  String buttonName;
    private  int buttonIndex;

    public int[] getRemotePattern() {
        return remotePattern;
    }

    public void setRemotePattern(int[] remotePattern) {
        this.remotePattern = remotePattern;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public int getButtonIndex() {
        return buttonIndex;
    }

    public void setButtonIndex(int buttonIndex) {
        this.buttonIndex = buttonIndex;
    }

    public RemoteCode(int[] remotePattern, String buttonName, int buttonIndex) {
        this.remotePattern = remotePattern;
        this.buttonName = buttonName;
        this.buttonIndex = buttonIndex;
    }
}
