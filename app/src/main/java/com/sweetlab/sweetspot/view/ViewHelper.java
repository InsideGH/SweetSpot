package com.sweetlab.sweetspot.view;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;

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

    /**
     * Using a global layout listener on the view and runs the runnable when the view have received it's
     * dimensions. If the view already have dimensions, the runnable is run directly.
     *
     * @param view     View to observe.
     * @param runnable Runnable to run.
     */
    public static void runOnLayout(final View view, final Runnable runnable) {
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    runnable.run();
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            runnable.run();
        }
    }
}
