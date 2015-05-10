package com.sweetlab.sweetspot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.view.AspectImageView;

public class CollectionViewHolder extends RecyclerView.ViewHolder {

    private final View mPhoto;
    private final AspectImageView mImageView;
    private final TextView mDateView;

    public CollectionViewHolder(View photo) {
        super(photo);
        mPhoto = photo;
        mImageView = (AspectImageView) mPhoto.findViewById(R.id.photo_collection_imageview);
        mDateView = (TextView) mPhoto.findViewById(R.id.date);
    }

    public View getMainView() {
        return mPhoto;
    }

    public AspectImageView getImageView() {
        return mImageView;
    }

    public TextView getDateView() {
        return mDateView;
    }
}
