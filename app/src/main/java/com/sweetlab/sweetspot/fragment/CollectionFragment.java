package com.sweetlab.sweetspot.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.sweetlab.sweetspot.modifiers.ModifierFactory;
import com.sweetlab.sweetspot.modifiers.ModifierType;

import java.util.List;

import rx.Observer;

/**
 * Present a collection of photos with date dividers in a mondrian style.
 */
public class CollectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<CollectionItem>> {
    /**
     * Set two columns.
     */
    private static final int DEFAULT_SPAN = 2;

    /**
     * Vertical scrolling.
     */
    private static final int DEFAULT_ORIENTATION = StaggeredGridLayoutManager.VERTICAL;

    /**
     * The recycler view.
     */
    private RecyclerView mRecyclerView;

    /**
     * The collection modifier that is passed to this fragment.
     */
    private ModifierType mModifierType;

    /**
     * The span of the recycler view.
     */
    private int mSpan;
    /**
     * The orientation of the recycler view.
     */
    private int mOrientation;

    /**
     * Fragment listener.
     */
    private CollectionFragmentListener mFragmentListener;

    /**
     * Create a collection fragment.
     *
     * @param span         The span of the recycler view.
     * @param orientation  The orientation of the recycler view.
     * @param modifierType The collection modifier.
     * @return A collection fragment.
     */
    public static CollectionFragment createInstance(int span, int orientation, ModifierType modifierType) {
        Bundle arguments = new Bundle();
        arguments.putInt(BundleKeys.COLLECTION_SPAN_COUNT, span);
        arguments.putInt(BundleKeys.COLLECTION_ORIENTATION, orientation);
        arguments.putSerializable(BundleKeys.COLLECTION_MODIFIER, modifierType);

        CollectionFragment collectionFragment = new CollectionFragment();
        collectionFragment.setArguments(arguments);
        return collectionFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_collection_view, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.photo_collection_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        Bundle arguments = getArguments();
        mSpan = arguments.getInt(BundleKeys.COLLECTION_SPAN_COUNT, DEFAULT_SPAN);
        mModifierType = (ModifierType) arguments.getSerializable(BundleKeys.COLLECTION_MODIFIER);
        mOrientation = arguments.getInt(BundleKeys.COLLECTION_ORIENTATION, DEFAULT_ORIENTATION);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mSpan, mOrientation);
        layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentListener = (CollectionFragmentListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException("Activity " + activity.toString() + " must implement " + CollectionFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LoaderConstants.PHOTO_COLLECTION, null, this);
    }

    @Override
    public Loader<List<CollectionItem>> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getActivity().getApplicationContext(), ModifierFactory.create(mModifierType));
    }

    @Override
    public void onLoadFinished(Loader<List<CollectionItem>> loader, List<CollectionItem> list) {
        CollectionAdapter adapter = new CollectionAdapter(list, mOrientation, mSpan);
        adapter.subscribeForClicks(new PhotoClickObserver());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<CollectionItem>> loader) {

    }

    /**
     * TODO change the single photo fragment to a viewpager.
     * <p/>
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
            mFragmentListener.onItemClicked(collectionItemClick);
        }
    }
}
