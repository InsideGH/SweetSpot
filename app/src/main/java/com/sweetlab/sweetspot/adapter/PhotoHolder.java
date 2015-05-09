package com.sweetlab.sweetspot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.view.AspectImageView;

public class PhotoHolder extends RecyclerView.ViewHolder {

    private final View mPhoto;
    private final AspectImageView mImageView;

    public PhotoHolder(View photo) {
        super(photo);
        mPhoto = photo;
        mImageView = (AspectImageView) mPhoto.findViewById(R.id.photo_collection_imageview);
    }

    public View getMainView() {
        return mPhoto;
    }

    public AspectImageView getImageView() {
        return mImageView;
    }
}
