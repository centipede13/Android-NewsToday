package com.example.android.newstoday;


import android.graphics.Bitmap;

public class News {
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private Bitmap mBitmap;

    public News(String title, String description, Bitmap bitmap, String url) {
        mTitle = title;
        mDescription = description;
        mBitmap = bitmap;
        mUrl = url;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

}