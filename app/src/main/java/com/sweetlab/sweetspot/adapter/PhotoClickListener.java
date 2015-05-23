package com.sweetlab.sweetspot.adapter;

import android.view.View;

import com.sweetlab.sweetspot.photometa.PhotoMeta;

import rx.subjects.PublishSubject;

/**
 * Handles photo clicks.
 */
public class PhotoClickListener implements View.OnClickListener {
    private final PublishSubject<CollectionItemClick> mSubject;
    private CollectionItemClick mClickInfo;

    /**
     * Constructor.
     *
     * @param subject Subject to inform when clicked.
     */
    public PhotoClickListener(PublishSubject<CollectionItemClick> subject) {
        mSubject = subject;
    }

    /**
     * Configure the click.
     *
     * @param adapterPosition    Adapter position.
     * @param unmodifiedPosition Unmodified adapter position.
     * @param photoMeta          Photo meta data.
     * @param holder             View holder.
     */
    public void configure(int adapterPosition, int unmodifiedPosition, PhotoMeta photoMeta, PhotoViewHolder holder) {
        mClickInfo = new CollectionItemClick(adapterPosition, unmodifiedPosition, photoMeta, holder);
    }

    @Override
    public void onClick(View v) {
        mSubject.onNext(mClickInfo);
    }
}
