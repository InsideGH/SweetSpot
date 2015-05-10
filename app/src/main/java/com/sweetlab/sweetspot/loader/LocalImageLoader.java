package com.sweetlab.sweetspot.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.MediaStore;

public class LocalImageLoader extends CursorLoader {
    private static final Uri INTERNAL_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private static final String[] PROJECTION = {MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT};

    private static final String SELECTION = null;

    private static String[] SELECTION_ARGS = null;

    private static final String SORT_ORDER = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

    public LocalImageLoader(Context context) {
        super(context);
        setUri(INTERNAL_URI);
        setProjection(PROJECTION);
        setSelection(SELECTION);
        setSelectionArgs(SELECTION_ARGS);
        setSortOrder(SORT_ORDER);
    }
}
