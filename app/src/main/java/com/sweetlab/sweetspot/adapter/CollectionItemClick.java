package com.sweetlab.sweetspot.adapter;

import com.sweetlab.sweetspot.photometa.PhotoMeta;

/**
 * Information about a collection item click.
 */
public class CollectionItemClick {
    /**
     * The adapter position that was clicked
     */
    private final int mAdapterPosition;

    /**
     * Photo meta data of the clicked item.
     */
    private final PhotoMeta mPhotoMeta;

    /**
     * The view holder for the clicked item.
     */
    private final PhotoViewHolder mPhotoViewHolder;

    private final int mUnmodifiedPosition;

    /**
     * Constructor. All parameters as for the clicked item.
     *  @param adapterPosition Adapter position.
     * @param unmodifiedPosition
     * @param photoMeta       Photo meta data.
     * @param holder          View holder.
     */
    public CollectionItemClick(int adapterPosition, int unmodifiedPosition, PhotoMeta photoMeta, PhotoViewHolder holder) {
        mAdapterPosition = adapterPosition;
        mUnmodifiedPosition = unmodifiedPosition;
        mPhotoMeta = photoMeta;
        mPhotoViewHolder = holder;
    }

    /**
     * Get the adapter position of the clicked item.
     *
     * @return The adapter position.
     */
    @SuppressWarnings("unused")
    public int getAdapterPosition() {
        return mAdapterPosition;
    }

    public int getUnmodifiedPosition() {
        return mUnmodifiedPosition;
    }

    /**
     * Get the photo meta data of the clicked item.
     *
     * @return The photo meta data.
     */
    public PhotoMeta getPhotoMeta() {
        return mPhotoMeta;
    }

    /**
     * Get the view holder for the clicked item.
     *
     * @return The view holder.
     */
    public PhotoViewHolder getPhotoHolder() {
        return mPhotoViewHolder;
    }

    @Override
    public String toString() {
        return "Position = " + mAdapterPosition + " meta = " + mPhotoMeta;
    }
}
