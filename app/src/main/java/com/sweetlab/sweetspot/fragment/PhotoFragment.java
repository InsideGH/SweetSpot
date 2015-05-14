package com.sweetlab.sweetspot.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;
import com.sweetlab.diskpicasso.SinglePicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

import java.io.File;

/**
 * A fragment just showing a single photo.
 */
public class PhotoFragment extends Fragment {

    /**
     * Image view with the image.
     */
    private ImageView mImageView;

    /**
     * Photo meta data.
     */
    private PhotoMeta mPhotoMeta;

    /**
     * Preload bitmap initially set.
     */
    private Bitmap mPreloadBitmap;

    /**
     * If placeholder should be used when loading with picasso.
     */
    private boolean mUsePlaceHolder;

    /**
     * If fade should be used when loading with picasso.
     */
    private boolean mUseFade;

    /**
     * Create a photo fragment.
     *
     * @param meta          Photo meta.
     * @param preLoadBitmap Any bitmap to preload or null.
     * @return A photo fragment.
     */
    public static PhotoFragment createInstance(PhotoMeta meta, Bitmap preLoadBitmap, boolean usePlaceHolder, boolean useFade) {
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(BundleKeys.PHOTO_META_KEY, meta);
        arguments.putParcelable(BundleKeys.BITMAP_KEY, preLoadBitmap);
        arguments.putBoolean(BundleKeys.USE_PLACE_HOLDER, usePlaceHolder);
        arguments.putBoolean(BundleKeys.USE_FADE, useFade);
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
        mPreloadBitmap = arguments.getParcelable(BundleKeys.BITMAP_KEY);
        mUsePlaceHolder = arguments.getBoolean(BundleKeys.USE_PLACE_HOLDER);
        mUseFade = arguments.getBoolean(BundleKeys.USE_FADE);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPhoto();
    }

    /**
     * If preload bitmap exists, load that first. Then load the real image.
     * <p/>
     * Note, disk caching is not used on purpose.
     */
    private void loadPhoto() {
        if (mPreloadBitmap != null) {
            mImageView.setImageBitmap(mPreloadBitmap);
        }
        RequestCreator load = SinglePicasso.getPicasso().load(new File(mPhotoMeta.getUrl()));
        if (!mUsePlaceHolder) {
            load.noPlaceholder();
        }
        if (!mUseFade) {
            load.noFade();
        }
        load.fit().centerInside().into(mImageView);
    }
}
