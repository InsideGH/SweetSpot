package com.sweetlab.sweetspot.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.adapter.CollectionAdapter;
import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.loader.CollectionItem;
import com.sweetlab.sweetspot.loader.CollectionLoader;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.view.AspectImageView;
import com.sweetlab.sweetspot.view.ViewHelper;

import java.util.List;

import rx.Observer;

/**
 * Present a collection of photos with date dividers in a mondrian style.
 */
public class CollectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<CollectionItem>> {
    /**
     * Set two columns.
     */
    private static final int SPAN_COUNT = 2;

    /**
     * Vertical scrolling.
     */
    private static final int ORIENTATION = StaggeredGridLayoutManager.VERTICAL;

    /**
     * The recycler view.
     */
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_collection_view, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.photo_collection_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, ORIENTATION);
        layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LoaderConstants.PHOTO_COLLECTION, null, this);
    }

    @Override
    public Loader<List<CollectionItem>> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<CollectionItem>> loader, List<CollectionItem> list) {
        CollectionAdapter adapter = new CollectionAdapter(list);
        adapter.subscribeForClicks(new PhotoClickObserver());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<CollectionItem>> loader) {
    }

    /**
     * TODO change the single photo fragment to a viewpager.
     *
     * Photo click observer. This will start a fragment showing the photo in fullscreen.
     * The bitmap from the collection item will be transferred to the photo fragment
     * to get a instant transaction feeling.
     */
    private class PhotoClickObserver implements Observer<CollectionItemClick> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(CollectionItemClick collectionItemClick) {
            PhotoFragment photoFragment = new PhotoFragment();
            AspectImageView imageView = collectionItemClick.getPhotoHolder().getImageView();

            setExitTransition(new Explode());
            photoFragment.setReturnTransition(new Fade());

            Bundle arguments = new Bundle();
            arguments.putSerializable(BundleKeys.PHOTO_META_KEY, collectionItemClick.getPhotoMeta());
            arguments.putParcelable(BundleKeys.BITMAP_KEY, ViewHelper.copyBitmap(imageView, false));
            photoFragment.setArguments(arguments);

            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_activity_container, photoFragment);
            transaction.addToBackStack(PhotoFragment.class.getSimpleName());
            transaction.commit();
        }
    }
}
