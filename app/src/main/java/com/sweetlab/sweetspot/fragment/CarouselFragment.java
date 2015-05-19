package com.sweetlab.sweetspot.fragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.modifiers.CollectionModifier;
import com.sweetlab.sweetspot.modifiers.NoModifier;

import rx.Observer;

/**
 * This is a horizontal carousel of images.
 */
public class CarouselFragment extends RecyclerViewFragment {

    /**
     * The carousel fragment listener.
     */
    public interface CarouselListener {
        /**
         * Called when a item in the collection is clicked.
         *
         * @param collectionItemClick Information about the click.
         */
        void onCarouselItemClicked(CollectionItemClick collectionItemClick);
    }

    /**
     * Horizontal scrolling.
     */
    private static final int RECYCLER_VIEW_ORIENTATION = StaggeredGridLayoutManager.HORIZONTAL;

    /**
     * Just photos.
     */
    private static final CollectionModifier NO_MODIFIER = new NoModifier();

    /**
     * The activity listening.
     */
    private CarouselListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CarouselListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException("Activity " + activity.toString() + " must implement " + CarouselListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected int getSpan() {
        return getActivity().getResources().getInteger(R.integer.carousel_span);
    }

    @Override
    protected CollectionModifier getModifier() {
        return NO_MODIFIER;
    }

    @Override
    protected int getOrientation() {
        return RECYCLER_VIEW_ORIENTATION;
    }

    @Override
    protected int getLoadedId() {
        return LoaderConstants.CAROUSEL;
    }

    @Override
    protected Observer<? super CollectionItemClick> getClickObserver() {
        return new CarouselClickObserver();
    }

    /**
     * Set the position of the carousel.
     *
     * @param position Adapter position.
     */
    public void setPosition(int position) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter != null && adapter.getItemCount() > 0) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    /**
     * The click observer.
     */
    private class CarouselClickObserver implements Observer<CollectionItemClick> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(CollectionItemClick collectionItemClick) {
            mListener.onCarouselItemClicked(collectionItemClick);
        }
    }
}
