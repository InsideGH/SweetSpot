package com.sweetlab.sweetspot.adapter;

import com.sweetlab.sweetspot.photometa.PhotoMeta;

public class CollectionItemClick {
    private final int mAdapterPosition;
    private final PhotoMeta mPhotoMeta;
    private final CollectionViewHolder mCollectionViewHolder;

    public CollectionItemClick(int adapterPosition, PhotoMeta photoMeta, CollectionViewHolder holder) {
        mAdapterPosition = adapterPosition;
        mPhotoMeta = photoMeta;
        mCollectionViewHolder = holder;
    }

    public int getAdapterPosition() {
        return mAdapterPosition;
    }

    public PhotoMeta getPhotoMeta() {
        return mPhotoMeta;
    }

    public CollectionViewHolder getPhotoHolder() {
        return mCollectionViewHolder;
    }

    @Override
    public String toString() {
        return "Position = " + mAdapterPosition + " meta = " + mPhotoMeta;
    }
}
