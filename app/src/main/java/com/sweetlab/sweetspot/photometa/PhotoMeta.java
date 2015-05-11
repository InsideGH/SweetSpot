package com.sweetlab.sweetspot.photometa;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoMeta implements Serializable {
    private final int mWidth;
    private final int mHeight;
    private final String mUrl;
    private final int mOrientation;
    private final long mDateTakenMs;
    private final String mReadableDateTaken;
    private final float mAspectRatio;
    private final Integer mYear;
    private final Integer mMonth;
    private final Integer mDay;

    public PhotoMeta(String path, int width, int height, int orientation, long dateTaken) {
        mUrl = path;
        mWidth = width;
        mHeight = height;
        mOrientation = orientation;
        mAspectRatio = getWidth() / (float) getHeight();
        mDateTakenMs = dateTaken;

        Date date = new Date(dateTaken);
        mReadableDateTaken = new SimpleDateFormat("yyyy-MM-dd  EEEE").format(date);
        String[] yearMonthDay = new SimpleDateFormat("yyyy-MM-dd").format(date).split("-");

        mYear = Integer.valueOf(yearMonthDay[0]);
        mMonth = Integer.valueOf(yearMonthDay[1]);
        mDay = Integer.valueOf(yearMonthDay[2]);
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

    public long getDateTakenMs() {
        return mDateTakenMs;
    }

    public String getReadableDateTaken() {
        return mReadableDateTaken;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    @Override
    public String toString() {
        return "w " + mWidth + " h = " + mHeight + " orientation = " + mOrientation + " url = " + mUrl;
    }
}
