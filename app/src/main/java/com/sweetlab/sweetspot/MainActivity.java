package com.sweetlab.sweetspot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetlab.sweetspot.adapter.CollectionAdapter;
import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.loader.Collection;
import com.sweetlab.sweetspot.loader.CollectionLoader;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.modifiers.CollectionModifier;
import com.sweetlab.sweetspot.modifiers.DayDividerModifier;
import com.sweetlab.sweetspot.view.MainGridRecyclerView;
import com.sweetlab.sweetspot.view.ViewHelper;

import rx.Observer;

/**
 * Will show a grid of photos with day divider.
 */
public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Collection> {

    /**
     * Vertical scrolling.
     */
    private static final int RECYCLER_VIEW_ORIENTATION = StaggeredGridLayoutManager.VERTICAL;

    /**
     * Photos and day dividers.
     */
    private static final CollectionModifier INSERT_DAY_DIVIDERS = new DayDividerModifier();

    /**
     * The recycler view.
     */
    private MainGridRecyclerView mMainGridRecyclerView;

    /**
     * The recycler view layout manager.
     */
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    /**
     * The span of the recycler view.
     */
    private int mSpan;

    /**
     * The adapter.
     */
    private CollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainGridRecyclerView = (MainGridRecyclerView) findViewById(R.id.main_grid_recycler_view);
        mMainGridRecyclerView.setHasFixedSize(true);

        mSpan = getResources().getInteger(R.integer.main_grid_span);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(mSpan, RECYCLER_VIEW_ORIENTATION);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mMainGridRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        getSupportLoaderManager().initLoader(LoaderConstants.MAIN_GRID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Collection> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getApplicationContext(), INSERT_DAY_DIVIDERS);
    }

    @Override
    public void onLoadFinished(Loader<Collection> loader, Collection list) {
        mAdapter = new CollectionAdapter(list, RECYCLER_VIEW_ORIENTATION, mSpan);
        mAdapter.subscribeForClicks(new MainGridClickObserver());
        ViewHelper.runOnLayout(mMainGridRecyclerView, new Runnable() {
            @Override
            public void run() {
                mMainGridRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Collection> loader) {

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
            Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
            intent.putExtra(BundleKeys.UNMODIFIED_POSITION, collectionItemClick.getUnmodifiedPosition());
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }
}
