package com.sweetlab.sweetspot.photometa;

/**
 * Meta data for a date divider.
 */
public class DateMeta {
    /**
     * Readable date.
     */
    private final String mDate;

    /**
     * Constructor. Use toString to getExact date.
     *
     * @param date The readable date.
     */
    public DateMeta(String date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return mDate;
    }
}
