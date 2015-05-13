package com.sweetlab.sweetspot.adapter;

/**
 * View boundaries.
 */
public class BoundedLayout {
    /**
     * View direction bounding.
     */
    public enum BoundedDirection {
        /**
         * The view is horizontally bounded, i.e vertical scrolling.
         */
        HORIZONTAL,
        /**
         * The view is vertically bounded, i.e horizontal scrolling.
         */
        VERTICAL,
    }

    /**
     * The bounding direction.
     */
    private BoundedDirection mDirection;

    /**
     * The bounded value in pixels.
     */
    private int mValue;

    /**
     * Constructor.
     *
     * @param direction Bounded direction.
     * @param value     Bounded size in pixels.
     */
    public BoundedLayout(BoundedDirection direction, int value) {
        mDirection = direction;
        mValue = value;
    }

    /**
     * Get the bounded direction, i.e which way the view is limited.
     *
     * @return The direction.
     */
    public BoundedDirection getBoundedDirection() {
        return mDirection;
    }

    /**
     * Get the amount of bounded in pixels.
     *
     * @return Limit in pixels.
     */
    public int getBoundedValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "direction = " + mDirection + " value = " + mValue;
    }
}
