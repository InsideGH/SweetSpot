package com.sweetlab.sweetspot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.fragment.CarouselFragment;
import com.sweetlab.sweetspot.fragment.FragmentTags;
import com.sweetlab.sweetspot.fragment.MainGridFragment;
import com.sweetlab.sweetspot.fragment.MultiPhotoFragment;
import com.sweetlab.sweetspot.fragment.RecyclerViewFragment;
import com.sweetlab.sweetspot.fragment.ViewPagerFragment;

/**
 * Will show a grid of photos with day divider.
 */
public class MainActivity extends FragmentActivity implements MainGridFragment.MainGridListener, CarouselFragment.CarouselListener, ViewPagerFragment.ViewPagerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);
        if (fragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment photoCollection = new MainGridFragment();
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
    public void onMainGridItemClicked(CollectionItemClick collectionItemClick) {
        Log.d("Peter100", "MainActivity.onMainGridItemClicked");
        Fragment fragment = MultiPhotoFragment.createInstance();
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.main_activity_container);

        current.setExitTransition(new Explode());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_container, fragment, FragmentTags.MULTI_PHOTO_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCarouselItemClicked(CollectionItemClick collectionItemClick) {
        Log.d("Peter100", "MainActivity.onCarouselItemClicked");
    }

    @Override
    public void onViewPagerSingleTap() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.MULTI_PHOTO_TAG);
        if (fragment != null) {
            Log.d("Peter100", "MainActivity.onViewPagerSingleTap");
            MultiPhotoFragment multiFragment = (MultiPhotoFragment) fragment;
            multiFragment.toggleCarousel();
        }
    }

    @Override
    public void onViewPagerSelected(int position) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.MULTI_PHOTO_TAG);
        if (fragment != null) {
            Log.d("Peter100", "MainActivity.onViewPagerSelected");
            MultiPhotoFragment multiFragment = (MultiPhotoFragment) fragment;
            multiFragment.setCarouselPosition(position);
        }
    }
}
