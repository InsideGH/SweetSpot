package com.sweetlab.sweetspot;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sweetlab.sweetspot.adapter.CollectionAdapter;
import com.sweetlab.sweetspot.adapter.ViewPagerAdapter;
import com.sweetlab.sweetspot.loader.Collection;
import com.sweetlab.sweetspot.loader.CollectionLoader;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.modifiers.CollectionModifier;
import com.sweetlab.sweetspot.modifiers.NoModifier;
import com.sweetlab.sweetspot.view.CarouselRecyclerView;
import com.sweetlab.sweetspot.view.ViewHelper;
import com.sweetlab.sweetspot.view.ViewPagerView;

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
    private StaggeredGridLayoutManager mCarouselLayoutManager;

    private int mStartPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_main);
        mPagerView = (ViewPagerView) findViewById(R.id.viewpager_view);

        mCarouselView = (CarouselRecyclerView) findViewById(R.id.carousel_view);
        mCarouselView.setHasFixedSize(true);
        mCarouselSpan = getResources().getInteger(R.integer.carousel_span);
        mCarouselLayoutManager = new StaggeredGridLayoutManager(mCarouselSpan, RECYCLER_VIEW_ORIENTATION);
        mCarouselLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mCarouselView.setLayoutManager(mCarouselLayoutManager);
        mCarouselSpan = getResources().getInteger(R.integer.carousel_span);

        mStartPosition = getIntent().getIntExtra(BundleKeys.UNMODIFIED_POSITION, 0);

        getSupportLoaderManager().initLoader(LoaderConstants.VIEWPAGER, null, this);
    }

    @Override
    public Loader<Collection> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getApplicationContext(), NO_MODIFIER);
    }

    @Override
    public void onLoadFinished(Loader<Collection> loader, Collection list) {
        mCarouselAdapter = new CollectionAdapter(list, RECYCLER_VIEW_ORIENTATION, mCarouselSpan);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), list);

        ViewHelper.runOnLayout(mCarouselView, new Runnable() {
            @Override
            public void run() {
                Log.d("Peter100", "FullscreenActivity.run mCarouselView " + mCarouselView.getWidth() + " " + mCarouselView.getHeight());
                mCarouselView.setAdapter(mCarouselAdapter);
                mCarouselView.scrollToPosition(mStartPosition);
            }
        });

        ViewHelper.runOnLayout(mPagerView, new Runnable() {
            @Override
            public void run() {
                Log.d("Peter100", "FullscreenActivity.run mPagerView " + mPagerView.getWidth() + " " + mPagerView.getHeight());
                mViewPagerAdapter.setDimensions(mPagerView.getWidth(), mPagerView.getHeight());
                mPagerView.setAdapter(mViewPagerAdapter);
                mPagerView.setOnTouchListener(new OnTouchListener());
                mPagerView.setOnPageChangeListener(new OnPageListener());
                mPagerView.setCurrentItem(mStartPosition);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Collection> loader) {
    }

    /**
     * The viewpager onTouch listener.
     */
    private class OnTouchListener implements View.OnTouchListener {

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
                int duration = getResources().getInteger(R.integer.carousel_toggle_duration);
                if (mCarouselView.getAlpha() == 1) {
                    mCarouselView.animate().alpha(0).setDuration(duration);
                } else {
                    mCarouselView.animate().alpha(1).setDuration(duration);
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

    private class OnPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCarouselView.scrollToPosition(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
