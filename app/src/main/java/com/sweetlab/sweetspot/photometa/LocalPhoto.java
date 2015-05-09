package com.sweetlab.sweetspot.photometa;

import java.io.Serializable;

public class LocalPhoto implements Serializable {
    private final int mWidth;
    private final int mHeight;
    private final String mUrl;
    private final int mOrientation;
    private float mAspectRatio;

    public LocalPhoto(String path, int width, int height, int orientation) {
        mUrl = path;
        mWidth = width;
        mHeight = height;
        mOrientation = orientation;
        mAspectRatio = getWidth() / (float) getHeight();
    }

    public String getUrl() {
        return mUrl;
    }

    public int getRawWidth() {
        return mWidth;
    }

    public int getRawHeight() {
        return mHeight;
    }

    public int getWidth() {
        return isLandscape() ? mWidth : mHeight;
    }

    public int getHeight() {
        return isLandscape() ? mHeight : mWidth;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public boolean isLandscape() {
        return (mOrientation == 0 || mOrientation == 180);
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    @Override
    public String toString() {
        return "w " + mWidth + " h = " + mHeight + " orientation = " + mOrientation + " url = " + mUrl;
    }
}
