package com.sweetlab.sweetspot.view;

import android.graphics.Bitmap;

/**
 * View related helper.
 */
public class ViewHelper {

    /**
     * Copy the bitmap from the ImageView. If the cache is not enabled, this method will
     * enable it and before returning disabled it.
     *
     * @param imageView The ImageView to copy bitmap from.
     * @param isMutable True if returned bitmap should be mutable.
     * @return The bitmap.
     */
    public static Bitmap copyBitmap(AspectImageView imageView, boolean isMutable) {
        Bitmap copy;
        if (imageView.isDrawingCacheEnabled()) {
            Bitmap bitmap = imageView.getDrawingCache();
            copy = bitmap.copy(bitmap.getConfig(), isMutable);
        } else {
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageView.getDrawingCache();
            copy = bitmap.copy(bitmap.getConfig(), isMutable);
            imageView.setDrawingCacheEnabled(false);
        }
        return copy;
    }
}
