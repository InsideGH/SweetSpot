package com.sweetlab.sweetspot.view;

import android.graphics.Bitmap;

public class ViewHelper {

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
