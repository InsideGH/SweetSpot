package com.sweetlab.sweetspot.adapter;

import com.sweetlab.sweetspot.photometa.LocalPhoto;

public class PhotoClick {
    private final int mAdapterPosition;
    private final LocalPhoto mPhotoMeta;

    public PhotoClick(int adapterPosition, LocalPhoto photoMeta) {
        mAdapterPosition = adapterPosition;
        mPhotoMeta = photoMeta;
    }

    public int getAdapterPosition() {
        return mAdapterPosition;
    }

    public LocalPhoto getPhotoMeta() {
        return mPhotoMeta;
    }

    @Override
    public String toString() {
        return "Position = " + mAdapterPosition + " meta = " + mPhotoMeta;
    }
}
