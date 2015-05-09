package com.sweetlab.sweetspot.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sweetlab.diskpicasso.SinglePicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.photometa.LocalPhoto;

import java.io.File;

public class PhotoFragment extends Fragment {

    private ImageView mImageView;
    private LocalPhoto mPhotoMeta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_single_view, container, false);
        mImageView = (ImageView) root.findViewById(R.id.photo_single_imageview);

        Bundle arguments = getArguments();
        mPhotoMeta = (LocalPhoto) arguments.getSerializable(BundleKeys.PHOTO_KEY);
        Log.d("Peter100", "PhotoFragment.onCreateView " + mPhotoMeta);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPhoto();
    }

    private void loadPhoto() {
        SinglePicasso.getPicasso().load(new File(mPhotoMeta.getUrl())).fit().centerInside().into(mImageView);
    }
}
