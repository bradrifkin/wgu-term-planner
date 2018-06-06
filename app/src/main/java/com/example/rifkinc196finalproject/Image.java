package com.example.rifkinc196finalproject;

import android.net.Uri;

public class Image {
    public long timeStamp;
    private long imageId;
    public Uri parentUri;

    public Image(long imageId) {
        this.imageId = imageId;
    }
}
