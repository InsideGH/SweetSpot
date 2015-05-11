package com.sweetlab.sweetspot.loader;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sweetlab.sweetspot.photometa.MetaHelper;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * A loader that creates a list of items, i.e a collection of items.
 * <p/>
 * The collection contains both photos and day date dividers.
 */
public class CollectionLoader extends AsyncTaskLoader<List<CollectionItem>> {

    /**
     * Uri for local images.
     */
    private static final Uri INTERNAL_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    /**
     * The columns we want.
     */
    private static final String[] PROJECTION = {MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT};

    /**
     * No specific selection.
     */
    private static final String SELECTION = null;

    /**
     * No specific selection arguments.
     */
    private static String[] SELECTION_ARGS = null;

    /**
     * Sort order so that user sees newest image first.
     */
    private static final String SORT_ORDER = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

    /**
     * List holding collection items.
     */
    private List<CollectionItem> mDataList;

    /**
     * TODO : fix content change listener.
     *
     * @param context preferably Android application context.
     */
    public CollectionLoader(Context context) {
        super(context);
    }

    @Override
    public List<CollectionItem> loadInBackground() {
        Cursor cursor = getPhotoCursor();
        if (cursor != null) {
            try {
                final List<CollectionItem> list = new ArrayList<>();
                List<CollectionItem> photoList = createPhotoList(cursor);
                Observable.from(photoList).flatMap(new DayDateInsert()).subscribe(new Action1<CollectionItem>() {
                    @Override
                    public void call(CollectionItem collectionItem) {
                        list.add(collectionItem);
                    }
                });
                return list;
            } finally {
                cursor.close();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void deliverResult(List<CollectionItem> data) {
        mDataList = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mDataList != null) {
            deliverResult(mDataList);
        } else {
            forceLoad();
        }
    }

    /**
     * Scan through the cursor and create a list of meta data.
     *
     * @param cursor Cursor, position will change.
     * @return A list of Photo meta data.
     */
    private List<CollectionItem> createPhotoList(Cursor cursor) {
        List<CollectionItem> list = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            PhotoMeta photoMeta = MetaHelper.createPhotoMeta(cursor);
            CollectionItem collectionItem = new CollectionItem(CollectionItem.TYPE_PHOTO);
            collectionItem.setObject(photoMeta);
            list.add(collectionItem);
        }
        return list;
    }

    /**
     * Must be called from background thread.
     *
     * @return Cursor with local image entries.
     */
    private Cursor getPhotoCursor() {
        ContentResolver resolver = getContext().getContentResolver();
        return resolver.query(INTERNAL_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
    }
}
