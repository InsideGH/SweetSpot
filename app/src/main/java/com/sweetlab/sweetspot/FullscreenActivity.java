package com.sweetlab.sweetspot;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.sweetlab.sweetspot.adapter.CollectionAdapter;
import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.adapter.ViewPagerAdapter;
import com.sweetlab.sweetspot.loader.Collection;
import com.sweetlab.sweetspot.loader.CollectionLoader;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.modifiers.CollectionModifier;
import com.sweetlab.sweetspot.modifiers.NoModifier;
import com.sweetlab.sweetspot.view.CarouselLayoutManager;
import com.sweetlab.sweetspot.view.CarouselRecyclerView;
import com.sweetlab.sweetspot.view.ImageViewTint;
import com.sweetlab.sweetspot.view.ViewHelper;
import com.sweetlab.sweetspot.view.ViewPagerView;

import rx.Observer;

public class FullscreenActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Collection> {

    /**
     * Horizontal scrolling.
     */
    private static final int RECYCLER_VIEW_ORIENTATION = StaggeredGridLayoutManager.HORIZONTAL;

    /**
     * Just photos.
     */
    private static final CollectionModifier NO_MODIFIER = new NoModifier();

    /**
     * The view pager.
     */
    private ViewPagerView mPagerView;

    /**
     * The carousel.
     */
    private CarouselRecyclerView mCarouselView;

    /**
     * The carousel adapter.
     */
    private CollectionAdapter mCarouselAdapter;

    /**
     * The view pager adapter.
     */
    private ViewPagerAdapter mViewPagerAdapter;

    /**
     * The span of the carousel.
     */
    private int mCarouselSpan;

    /**
     * Carousel layout manager.
     */
    private CarouselLayoutManager mCarouselLayoutManager;

    /**
     * The current photo position.
     */
    private int mCurrentPosition;

    /**
     * Carousel item tint duration.
     */
    private int mTintDuration;

    /**
     * Carousel item tint color.
     */
    private int mTintColor;

    /**
     * The duration of hide/show carousel.
     */
    private int mToggleDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_main);

        mTintDuration = getResources().getInteger(R.integer.carousel_tint_duration);
        mTintColor = getResources().getColor(R.color.collection_item_tint_color);
        mToggleDuration = getResources().getInteger(R.integer.carousel_toggle_duration);

        mCurrentPosition = getIntent().getIntExtra(BundleKeys.UNMODIFIED_POSITION, 0);

        mPagerView = (ViewPagerView) findViewById(R.id.viewpager_view);

        mCarouselView = (CarouselRecyclerView) findViewById(R.id.carousel_view);
        mCarouselView.setHasFixedSize(true);
        mCarouselSpan = getResources().getInteger(R.integer.carousel_span);
        mCarouselLayoutManager = new CarouselLayoutManager(getApplicationContext());
        mCarouselView.setLayoutManager(mCarouselLayoutManager);
        mCarouselSpan = getResources().getInteger(R.integer.carousel_span);
        mCarouselView.setOnScrollListener(new CarouselOnScrollListener());

        getSupportLoaderManager().initLoader(LoaderConstants.VIEWPAGER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BundleKeys.UNMODIFIED_POSITION, mCurrentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPosition = savedInstanceState.getInt(BundleKeys.UNMODIFIED_POSITION);
    }

    @Override
    public Loader<Collection> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getApplicationContext(), NO_MODIFIER);
    }

    @Override
    public void onLoadFinished(Loader<Collection> loader, Collection list) {
        if (mCarouselAdapter == null) {
            mCarouselAdapter = new CollectionAdapter(list, RECYCLER_VIEW_ORIENTATION, mCarouselSpan);
        } else {
            mCarouselAdapter.setCollection(list);
        }

        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        } else {
            mViewPagerAdapter.setCollection(list);
        }

        ViewHelper.runOnLayout(mCarouselView, new Runnable() {
            @Override
            public void run() {
                if (mCarouselView.getAdapter() == null) {
                    mCarouselView.setAdapter(mCarouselAdapter);
                }
                mCarouselView.scrollToPosition(mCurrentPosition);
                mCarouselAdapter.subscribeForClicks(new CarouselClickObserver());
            }
        });

        ViewHelper.runOnLayout(mPagerView, new Runnable() {
            @Override
            public void run() {
                mViewPagerAdapter.setDimensions(mPagerView.getWidth(), mPagerView.getHeight());
                if (mPagerView.getAdapter() == null) {
                    mPagerView.setAdapter(mViewPagerAdapter);
                }
                mPagerView.setOnTouchListener(new ViewPagerOnTouchListener());
                mPagerView.setOnPageChangeListener(new ViewPagerOnPageListener());
                mPagerView.setCurrentItem(mCurrentPosition);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Collection> loader) {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    /**
     * Clear tint from all visible items in the carousel.
     */
    private void clearTintFromCarousel() {
        int firstPos = mCarouselLayoutManager.findFirstVisibleItemPosition();
        int lastPos = mCarouselLayoutManager.findLastVisibleItemPosition();
        for (int i = firstPos; i <= lastPos; i++) {
            View root = mCarouselLayoutManager.findViewByPosition(i);
            if (root != null) {
                View view = root.findViewById(R.id.photo_collection_imageview);
                if (view != null) {
//                    Log.d("Peter100", "clear tint " + i);
                    ImageViewTint.animateToNoTint((ImageView) view, mTintDuration);
                }
            }
        }
    }

    /**
     * Tint the item a current position in the carousel.
     */
    private void tintCurrentCarouselItem() {
        View root = mCarouselLayoutManager.findViewByPosition(mCurrentPosition);
        if (root != null) {
            View view = root.findViewById(R.id.photo_collection_imageview);
            if (view != null) {
//                Log.d("Peter100", "tint " + mCurrentPosition);
                ImageViewTint.animateToTint(((ImageView) view), mTintColor, mTintDuration);
            }
        }
    }

    /**
     * Carousel on scroll listener.
     */
    private class CarouselOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                clearTintFromCarousel();
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                clearTintFromCarousel();
                tintCurrentCarouselItem();
            }
        }
    }

    /**
     * The carousel item click listener.
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
            mCurrentPosition = collectionItemClick.getAdapterPosition();
            mPagerView.setCurrentItem(mCurrentPosition);
        }
    }

    /**
     * The ViewPager onTouch listener.
     */
    private class ViewPagerOnTouchListener implements View.OnTouchListener {

        private GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Object isHidingObject = mCarouselView.getTag(R.id.TAG_VIEW_CAROUSEL_IS_HIDING);
                boolean isHiding = isHidingObject != null ? (boolean) isHidingObject : false;

                if (isHiding) {
                    mCarouselView.setTag(R.id.TAG_VIEW_CAROUSEL_IS_HIDING, false);
                    mCarouselView.animate().alpha(1).translationY(0).setDuration(mToggleDuration);
                } else {
                    mCarouselView.setTag(R.id.TAG_VIEW_CAROUSEL_IS_HIDING, true);
                    mCarouselView.animate().alpha(0).translationY(mCarouselView.getHeight()).setDuration(mToggleDuration);
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
        }
    }

    /**
     * The ViewPager pager listener.
     */
    private class ViewPagerOnPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position;
            mCarouselView.smoothScrollToPosition(mCurrentPosition);

            int firstPos = mCarouselLayoutManager.findFirstCompletelyVisibleItemPosition();
            int lastPos = mCarouselLayoutManager.findLastCompletelyVisibleItemPosition();
            if (mCurrentPosition >= firstPos && mCurrentPosition <= lastPos) {
                clearTintFromCarousel();
                tintCurrentCarouselItem();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}