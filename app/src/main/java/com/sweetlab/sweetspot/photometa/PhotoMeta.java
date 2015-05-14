package com.sweetlab.sweetspot.photometa;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Meta data for a photo.
 */
public class PhotoMeta implements Serializable {
    /**
     * The format for the readable date.
     */
    public static final String READABLE_DATE_FORMAT = "yyyy-MM-dd  EEEE";

    /**
     * The original width, not taking rotation into count.
     */
    private final int mWidth;

    /**
     * The original height, not taking rotation into count.
     */
    private final int mHeight;

    /**
     * The absolute path to the file.
     */
    private final String mUrl;

    /**
     * The orientation.
     */
    private final int mOrientation;

    /**
     * Date taken in milliseconds from 1970.
     */
    private final long mDateTakenMs;

    /**
     * A readable string of the date.
     */
    private final String mReadableDateTaken;

    /**
     * The aspect ratio, taking rotation into count.
     */
    private final float mAspectRatio;

    /**
     * Year of date taken.
     */
    private final Integer mYear;

    /**
     * Month of date taken.
     */
    private final Integer mMonth;

    /**
     * Day of date taken.
     */
    private final Integer mDay;

    /**
     * Create a immutable photo meta.
     *
     * @param path        The absolute path to file.
     * @param width       The width of the image, without rotation taken into count.
     * @param height      The height of the image, without rotation taken into count.
     * @param orientation The orientation of the image.
     * @param dateTaken   Date taken in milliseconds from 1970.
     */
    public PhotoMeta(String path, int width, int height, int orientation, long dateTaken) {
        mUrl = path;
        mWidth = width;
        mHeight = height;
        mOrientation = orientation;
        mAspectRatio = getWidth() / (float) getHeight();
        mDateTakenMs = dateTaken;

        Date date = new Date(dateTaken);
        mReadableDateTaken = new SimpleDateFormat(READABLE_DATE_FORMAT).format(date);
        String[] yearMonthDay = new SimpleDateFormat("yyyy-MM-dd").format(date).split("-");

        mYear = Integer.valueOf(yearMonthDay[0]);
        mMonth = Integer.valueOf(yearMonthDay[1]);
        mDay = Integer.valueOf(yearMonthDay[2]);
    }

    /**
     * Get the absolute path of the photo.
     *
     * @return The path.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Get the width of the photo, not taking rotation into count.
     *
     * @return The width.
     */
    @SuppressWarnings("unused")
    public int getRawWidth() {
        return mWidth;
    }

    /**
     * Get the height of the photo, not taking rotation into count.
     *
     * @return The height.
     */
    @SuppressWarnings("unused")
    public int getRawHeight() {
        return mHeight;
    }

    /**
     * Get the width of the photo, taking rotation into count.
     *
     * @return The width.
     */
    public int getWidth() {
        return isLandscape() ? mWidth : mHeight;
    }

    /**
     * Get the height of the photo, taking rotation into count.
     *
     * @return The height.
     */
    public int getHeight() {
        return isLandscape() ? mHeight : mWidth;
    }

    /**
     * Get the rotation of the photo.
     *
     * @return The rotation.
     */
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Get if photo is a landscape orientated, takes rotation into account.
     *
     * @return True if landscape.
     */
    public boolean isLandscape() {
        return (mOrientation == 0 || mOrientation == 180);
    }

    /**
     * Get if photo is a portrait orientated, takes rotation into account.
     *
     * @return True if portrait.
     */

    public boolean isPortrait() {
        return (mOrientation == 90 || mOrientation == 270);
    }

    /**
     * Get the aspect ratio, takes rotation into account.
     *
     * @return The aspect ratio.
     */
    public float getAspectRatio() {
        return mAspectRatio;
    }

    /**
     * Get the date taken in milliseconds from 1970.
     *
     * @return The milliseconds.
     */
    @SuppressWarnings("unused")
    public long getDateTakenMs() {
        return mDateTakenMs;
    }

    /**
     * Get a readable date.
     *
     * @return Date
     */
    public String getReadableDateTaken() {
        return mReadableDateTaken;
    }

    /**
     * Get the date taken year.
     *
     * @return The year.
     */
    @SuppressWarnings("unused")
    public int getYear() {
        return mYear;
    }

    /**
     * Get the date taken month.
     *
     * @return The month.
     */
    @SuppressWarnings("unused")
    public int getMonth() {
        return mMonth;
    }

    /**
     * Get the date taken day.
     *
     * @return The day.
     */
    public int getDay() {
        return mDay;
    }

    @Override
    public String toString() {
        return mUrl + " w " + mWidth + " h = " + mHeight;
    }
}
