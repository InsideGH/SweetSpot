package com.sweetlab.sweetspot.adapter;

import com.sweetlab.sweetspot.photometa.LocalPhoto;

public class PhotoClick {
    private final int mAdapterPosition;
    private final LocalPhoto mPhotoMeta;
    private final PhotoHolder mPhotoHolder;

    public PhotoClick(int adapterPosition, LocalPhoto photoMeta, PhotoHolder holder) {
        mAdapterPosition = adapterPosition;
        mPhotoMeta = photoMeta;
        mPhotoHolder = holder;
    }

    public int getAdapterPosition() {
        return mAdapterPosition;
    }

    public LocalPhoto getPhotoMeta() {
        return mPhotoMeta;
    }

    public PhotoHolder getPhotoHolder() {
        return mPhotoHolder;
    }

    @Override
    public String toString() {
        return "Position = " + mAdapterPosition + " meta = " + mPhotoMeta;
    }
}
