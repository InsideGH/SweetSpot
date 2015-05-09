package com.sweetlab.sweetspot;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetlab.sweetspot.adapter.LocalImageAdapter;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.loader.LocalImageLoader;

public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SPAN_COUNT = 2;
    private static final int ORIENTATION = StaggeredGridLayoutManager.VERTICAL;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private LocalImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, ORIENTATION);
        mLayoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecyclerView.setLayoutManager(mLayoutManager);
        getLoaderManager().initLoader(LoaderConstants.LOCAL_IMAGE, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LocalImageLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mImageAdapter = new LocalImageAdapter(cursor);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
