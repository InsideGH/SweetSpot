package com.sweetlab.sweetspot.fragment;

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
import com.sweetlab.sweetspot.modifiers.CollectionModifier;

import java.util.List;

import rx.Observer;

/**
 * Present a collection of photos with date dividers in a mondrian style.
 */
public abstract class RecyclerViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<CollectionItem>> {
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
    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_collection_view, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.photo_collection_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpan(), getOrientation());
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(getLoadedId(), null, this);
    }

    @Override
    public Loader<List<CollectionItem>> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getActivity().getApplicationContext(), getModifier());
    }

    @Override
    public void onLoadFinished(Loader<List<CollectionItem>> loader, List<CollectionItem> list) {
        CollectionAdapter adapter = new CollectionAdapter(list, getOrientation(), getSpan());
        adapter.subscribeForClicks(getClickObserver());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<CollectionItem>> loader) {
    }

    /**
     * Get the span for the recycler view.
     *
     * @return The span.
     */
    protected abstract int getSpan();

    /**
     * Get the orientation for the recycler view.
     *
     * @return The orientation.
     */
    protected abstract int getOrientation();

    /**
     * Get the collection modifier to be used in the adapter.
     *
     * @return The collection modifier.
     */
    protected abstract CollectionModifier getModifier();

    /**
     * Get the loader id to be used.
     *
     * @return The loader id.
     */
    protected abstract int getLoadedId();

    /**
     * Get the click observer.
     *
     * @return The click observer.
     */
    protected abstract Observer<? super CollectionItemClick> getClickObserver();
}
