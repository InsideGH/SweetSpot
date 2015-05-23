package com.sweetlab.sweetspot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.view.AspectImageView;

/**
 * Holds views for a photo.
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private final AspectImageView mImageView;
    private final PhotoClickListener mClickListener;

    /**
     * Constructor.
     *
     * @param photo The photo root view.
     */
    public PhotoViewHolder(View photo, PhotoClickListener clickListener) {
        super(photo);
        mImageView = (AspectImageView) photo.findViewById(R.id.photo_collection_imageview);
        mImageView.setOnClickListener(clickListener);
        mClickListener = clickListener;
    }

    /**
     * Get the ImageView to place photos on.
     *
     * @return The ImageView.
     */
    public AspectImageView getImageView() {
        return mImageView;
    }

    /**
     * Get the click listener.
     *
     * @return The click listener.
     */
    public PhotoClickListener getClickListener() {
        return mClickListener;
    }
}
