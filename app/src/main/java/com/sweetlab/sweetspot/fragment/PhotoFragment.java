package com.sweetlab.sweetspot.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sweetlab.diskpicasso.CacheEntry;
import com.sweetlab.diskpicasso.DiskPicasso;
import com.sweetlab.diskpicasso.SinglePicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

import java.io.File;
import java.util.List;

/**
 * A fragment showing a single photo.
 */
public class PhotoFragment extends Fragment {
    /**
     * The decoding quality.
     */
    private static final Bitmap.Config JPEG_CONFIG = Bitmap.Config.ARGB_8888;

    /**
     * Image view with the image.
     */
    private ImageView mImageView;

    /**
     * Photo meta data.
     */
    private PhotoMeta mPhotoMeta;

    /**
     * Pager width.
     */
    private int mWidth;

    /**
     * Pager height.
     */
    private int mHeight;

    public static PhotoFragment createInstance(PhotoMeta meta, int width, int height) {
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(BundleKeys.PHOTO_META_KEY, meta);
        arguments.putInt(BundleKeys.WIDTH_KEY, width);
        arguments.putInt(BundleKeys.HEIGHT_KEY, height);
        photoFragment.setArguments(arguments);
        return photoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_single_view, container, false);
        mImageView = (ImageView) root.findViewById(R.id.photo_single_imageview);

        Bundle arguments = getArguments();
        mPhotoMeta = (PhotoMeta) arguments.getSerializable(BundleKeys.PHOTO_META_KEY);
        mWidth = arguments.getInt(BundleKeys.WIDTH_KEY);
        mHeight = arguments.getInt(BundleKeys.HEIGHT_KEY);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPhoto();
    }

    /**
     * Load image into image view. If all parameters are know, disk caching will be used. Otherwise, plain picasso
     * is used.
     */
    private void loadPhoto() {
        int imageWidth = mPhotoMeta.getWidth();
        int imageHeight = mPhotoMeta.getHeight();
        if (imageWidth == 0 || imageHeight == 0 || mWidth == 0 || mHeight == 0) {
            SinglePicasso.getPicasso().load(new File(mPhotoMeta.getUrl())).fit().centerInside().into(mImageView);
            Log.d("Peter100", "PhotoFragment.loadPhoto loading with single picasso");
        } else {
            float factor = calcScaleFactor(imageWidth, imageHeight);
            int resizeX = (int) (factor * mPhotoMeta.getRawWidth());
            final DiskPicasso instance = DiskPicasso.getInstance();
            List<CacheEntry> cacheEntries = instance.getCacheEntries(mPhotoMeta.getUrl());
            CacheEntry match = DiskPicasso.findMatch(cacheEntries, resizeX, 0, JPEG_CONFIG);
            if (match != null) {
                Log.d("Peter100", "PhotoFragment.loadPhoto cached " + match);
                SinglePicasso.getPicasso().load(match.getCacheFile()).config(JPEG_CONFIG).into(mImageView);
            } else {
                Log.d("Peter100", "PhotoFragment.loadPhoto not cached " + mPhotoMeta);
                instance.loadWithCacheWrite(mPhotoMeta.getUrl(), JPEG_CONFIG).resize(resizeX, 0).into(mImageView);
            }
        }
    }

    /**
     * Calculate the minimum scale factor to scale down just about enough.
     *
     * @param imageWidth  Image width, orientation taken into account.
     * @param imageHeight Image height, orientation taken into account.
     * @return The minimum scale factor.
     */
    private float calcScaleFactor(int imageWidth, int imageHeight) {
        float xFactor = 1.0f;
        float yFactor = 1.0f;

        if (imageWidth > mWidth) {
            xFactor = mWidth / (float) imageWidth;
        }
        if (imageHeight > mHeight) {
            yFactor = mHeight / (float) imageHeight;
        }
        return Math.min(xFactor, yFactor);
    }
}
