package com.sweetlab.sweetspot.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import com.sweetlab.sweetspot.modifiers.CollectionModifier;
import com.sweetlab.sweetspot.photometa.MetaHelper;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * A loader that creates a list of items, i.e a collection of items.
 * <p/>
 * The collection contains both photos and day date dividers.
 */
public class CollectionLoader extends AsyncTaskLoader<Collection> {

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
     * A collection observer.
     */
    private final ForceLoadContentObserver mObserver;

    /**
     * List holding collection items.
     */
    private Collection mCollection;

    /**
     * RxJava collection modifier. The modifier will be passed the whole photo collection.
     */
    private final CollectionModifier mModifier;

    /**
     * TODO : fix content change listener.
     *
     * @param context  preferably Android application context.
     * @param modifier The collection modifier.
     */
    public CollectionLoader(Context context, CollectionModifier modifier) {
        super(context);
        mModifier = modifier;
        mObserver = new ForceLoadContentObserver();
    }

    @Override
    public Collection loadInBackground() {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(INTERNAL_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);

        if (cursor != null) {
            try {
                final List<CollectionItem> list = new ArrayList<>();
                List<CollectionItem> photoList = createPhotoList(cursor);

                Observable.from(photoList).flatMap(mModifier).subscribe(new Action1<CollectionItem>() {
                    @Override
                    public void call(CollectionItem collectionItem) {
                        list.add(collectionItem);
                    }
                });

                cursor.registerContentObserver(mObserver);
                return new Collection(list, cursor);

            } catch (RuntimeException ex) {
                cursor.close();
            }
        }
        return new Collection(new ArrayList<CollectionItem>(0), cursor);
    }

    @Override
    public void deliverResult(Collection collection) {
        if (isReset()) {
            if (collection != null) {
                onReleaseResources(collection);
            }
        }

        Collection oldCollection = mCollection;
        mCollection = collection;

        if (isStarted()) {
            super.deliverResult(collection);
        }

        if (oldCollection != null && oldCollection != mCollection) {
            onReleaseResources(oldCollection);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCollection != null) {
            deliverResult(mCollection);
        }
        if (takeContentChanged() || mCollection == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Collection collection) {
        if (collection != null) {
            onReleaseResources(collection);
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        onReleaseResources(mCollection);
        mCollection = null;
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
     * Release resources.
     *
     * @param collection
     */
    private void onReleaseResources(Collection collection) {
        if (collection != null) {
            Cursor cursor = collection.getCursor();
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}