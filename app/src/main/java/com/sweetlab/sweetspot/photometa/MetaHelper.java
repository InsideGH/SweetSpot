package com.sweetlab.sweetspot.photometa;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Helper for meta related information.
 */
public class MetaHelper {

    /**
     * Create photo meta data from a correctly positioned cursor.
     *
     * @param cursor Correctly positioned cursor.
     * @return Local photo meta data.
     */
    public static PhotoMeta createPhotoMeta(Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
        final String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
        final int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.WIDTH));
        final int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.HEIGHT));
        final int orientation = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION));
        final long dateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN));

        String fileKey = path + "_" + id;
        return new PhotoMeta(path, fileKey, width, height, orientation, dateTaken);
    }
}
