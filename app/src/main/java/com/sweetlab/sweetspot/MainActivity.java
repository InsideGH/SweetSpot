package com.sweetlab.sweetspot;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetlab.sweetspot.adapter.CollectionItemClick;
import com.sweetlab.sweetspot.fragment.CollectionFragment;
import com.sweetlab.sweetspot.fragment.CollectionFragmentListener;
import com.sweetlab.sweetspot.fragment.PhotoFragment;
import com.sweetlab.sweetspot.messaging.BundleKeys;
import com.sweetlab.sweetspot.modifiers.ModifierType;
import com.sweetlab.sweetspot.view.AspectImageView;
import com.sweetlab.sweetspot.view.ViewHelper;

public class MainActivity extends Activity implements CollectionFragmentListener{
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

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_activity_container);
        if (fragment == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CollectionFragment photoCollection = CollectionFragment.createInstance(SPAN, ORIENTATION, DAY_DIVIDERS);
            transaction.add(R.id.main_activity_container, photoCollection, CollectionFragment.class.getSimpleName());
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

        PhotoFragment photoFragment = new PhotoFragment();
        AspectImageView imageView = collectionItemClick.getPhotoHolder().getImageView();

        Fragment current = getFragmentManager().findFragmentById(R.id.main_activity_container);

        current.setExitTransition(new Explode());
        photoFragment.setReturnTransition(new Fade());
        photoFragment.setEnterTransition(new Fade());

        Bundle arguments = new Bundle();
        arguments.putSerializable(BundleKeys.PHOTO_META_KEY, collectionItemClick.getPhotoMeta());
        arguments.putParcelable(BundleKeys.BITMAP_KEY, ViewHelper.copyBitmap(imageView, false));
        photoFragment.setArguments(arguments);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_container, photoFragment);
        transaction.addToBackStack(PhotoFragment.class.getSimpleName());
        transaction.commit();
    }
}
