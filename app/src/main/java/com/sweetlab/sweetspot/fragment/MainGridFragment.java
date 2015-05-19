package com.sweetlab.sweetspot.fragment;

import android.app.Activity;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.modifiers.CollectionModifier;
import com.sweetlab.sweetspot.modifiers.DayDividerModifier;

import rx.Observer;

/**
 * This is the main grid of photos with day dividers.
 */
public class MainGridFragment extends RecyclerViewFragment {

    /**
     * The Main Grid fragment listener.
     */
    public interface MainGridListener {
        /**
         * Called when a item in the collection is clicked.
         *
         * @param collectionItemClick Information about the click.
         */
        void onMainGridItemClicked(CollectionItemClick collectionItemClick);
    }

    /**
     * Vertical scrolling.
     */
    private static final int RECYCLER_VIEW_ORIENTATION = StaggeredGridLayoutManager.VERTICAL;

    /**
     * Photos and day dividers.
     */
    private static final CollectionModifier INSERT_DAY_DIVIDERS = new DayDividerModifier();

    /**
     * The activity listening.
     */
    private MainGridListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainGridListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException("Activity " + activity.toString() + " must implement " + MainGridListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected int getSpan() {
        return getActivity().getResources().getInteger(R.integer.main_grid_span);
    }

    @Override
    protected CollectionModifier getModifier() {
        return INSERT_DAY_DIVIDERS;
    }

    @Override
    protected int getOrientation() {
        return RECYCLER_VIEW_ORIENTATION;
    }

    @Override
    protected int getLoadedId() {
        return LoaderConstants.MAIN_GRID;
    }

    @Override
    protected Observer<? super CollectionItemClick> getClickObserver() {
        return new MainGridClickObserver();
    }

    /**
     * The click observer.
     */
    private class MainGridClickObserver implements Observer<CollectionItemClick> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(CollectionItemClick collectionItemClick) {
            mListener.onMainGridItemClicked(collectionItemClick);
        }
    }
}
