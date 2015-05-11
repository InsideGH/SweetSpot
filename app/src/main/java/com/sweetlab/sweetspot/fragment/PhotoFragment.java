package com.sweetlab.sweetspot.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
