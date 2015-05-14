package com.sweetlab.sweetspot.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
     * Create a photo fragment.
     *
     * @param meta          Photo meta.
     * @param preLoadBitmap Any bitmap to preload or null.
     * @return A photo fragment.
     */
    public static PhotoFragment createInstance(PhotoMeta meta, Bitmap preLoadBitmap) {
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(BundleKeys.PHOTO_META_KEY, meta);
        arguments.putParcelable(BundleKeys.BITMAP_KEY, preLoadBitmap);
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
     * No placeholder tells Picasso to not clear the ImageView prior to loading.
     */
    private void loadPhoto() {
        if (mPreloadBitmap != null) {
            mImageView.setImageBitmap(mPreloadBitmap);
        }
        SinglePicasso.getPicasso().load(new File(mPhotoMeta.getUrl())).fit().centerInside().noPlaceholder().noFade().into(mImageView);
    }
}
