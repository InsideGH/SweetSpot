package com.sweetlab.sweetspot;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetlab.sweetspot.fragment.CollectionFragment;
import com.sweetlab.sweetspot.modifiers.ModifierType;

public class MainActivity extends Activity {
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        CollectionFragment photoCollection = CollectionFragment.createInstance(SPAN, ORIENTATION, DAY_DIVIDERS);
        transaction.replace(R.id.main_activity_container, photoCollection, CollectionFragment.class.getSimpleName());
        transaction.commit();
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
}
