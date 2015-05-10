package com.sweetlab.sweetspot.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.adapter.LocalImageAdapter;
import com.sweetlab.sweetspot.adapter.PhotoClick;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.loader.LocalImageLoader;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.view.AspectImageView;
import com.sweetlab.sweetspot.view.ViewHelper;

import rx.Observer;

public class PhotoCollectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SPAN_COUNT = 2;
    private static final int ORIENTATION = StaggeredGridLayoutManager.VERTICAL;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private LocalImageAdapter mImageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_collection_view, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.photo_collection_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, ORIENTATION);
        mLayoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        mRecyclerView.setLayoutManager(mLayoutManager);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LoaderConstants.LOCAL_IMAGE, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LocalImageLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mImageAdapter = new LocalImageAdapter(cursor);
        mImageAdapter.registerForClicks(new PhotoClickObserver());
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Photo click observer.
     */
    private class PhotoClickObserver implements Observer<PhotoClick> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(PhotoClick photoClick) {
            PhotoFragment photoFragment = new PhotoFragment();
            AspectImageView imageView = photoClick.getPhotoHolder().getImageView();

            Bundle arguments = new Bundle();
            arguments.putSerializable(BundleKeys.PHOTO_META_KEY, photoClick.getPhotoMeta());
            arguments.putParcelable(BundleKeys.BITMAP_KEY, ViewHelper.copyBitmap(imageView, false));
            photoFragment.setArguments(arguments);

            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_activity_container, photoFragment);
            transaction.addToBackStack(PhotoFragment.class.getSimpleName());
            transaction.commit();
        }
    }
}
