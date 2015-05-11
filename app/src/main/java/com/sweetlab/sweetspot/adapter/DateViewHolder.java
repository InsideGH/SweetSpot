package com.sweetlab.sweetspot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sweetlab.sweetspot.R;

/**
 * Holds views for a date divider.
 */
public class DateViewHolder extends RecyclerView.ViewHolder {
    private final TextView mDateView;

    /**
     * Constructor.
     *
     * @param photo The root.
     */
    public DateViewHolder(View photo) {
        super(photo);
        mDateView = (TextView) photo.findViewById(R.id.date);
    }

    /**
     * Get the date TextView to set date on.
     *
     * @return The TextView.
     */
    public TextView getDateTextView() {
        return mDateView;
    }
}
