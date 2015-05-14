package com.sweetlab.sweetspot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.fragment.CollectionFragmentListener;
import com.sweetlab.sweetspot.fragment.FragmentTags;
import com.sweetlab.sweetspot.fragment.MultiPhotoFragment;
import com.sweetlab.sweetspot.fragment.RecyclerViewFragment;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.modifiers.ModifierType;

/**
 * Will show a grid of photos with day divider.
 */
public class MainActivity extends FragmentActivity implements CollectionFragmentListener {
    /**
     * Set two columns.
     */
    private static final int SPAN = 2;

    /**
     * Vertical scrolling.
     */
    private static final int ORIENTATION = StaggeredGridLayoutManager.VERTICAL;

    /**
     * Insert day dividers.
     */
    private static final ModifierType DAY_DIVIDERS = ModifierType.DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);
        if (fragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment photoCollection = RecyclerViewFragment.createInstance(SPAN, ORIENTATION, DAY_DIVIDERS, LoaderConstants.MAIN_GRID);
            transaction.add(R.id.main_activity_container, photoCollection, FragmentTags.MAIN_GRID_TAG);
            transaction.commit();
        }
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
    public void onItemClicked(CollectionItemClick collectionItemClick) {
        Log.d("Peter100", "MainActivity.onItemClicked");

        Fragment fragment = MultiPhotoFragment.createInstance();
//        AspectImageView imageView = collectionItemClick.getPhotoHolder().getImageView();
//        PhotoFragment fragment = PhotoFragment.createInstance(collectionItemClick.getPhotoMeta(), ViewHelper.copyBitmap(imageView, false), false, false);
//        fragment.setReturnTransition(new Fade());
//        fragment.setEnterTransition(new Fade());

        Fragment current = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

        current.setExitTransition(new Explode());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_container, fragment, FragmentTags.MULTI_PHOTO_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
