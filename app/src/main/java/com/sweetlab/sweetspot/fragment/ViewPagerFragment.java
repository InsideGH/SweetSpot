package com.sweetlab.sweetspot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.adapter.ViewPagerAdapter;
import com.sweetlab.sweetspot.loader.CollectionItem;
import com.sweetlab.sweetspot.loader.CollectionLoader;
import com.sweetlab.sweetspot.loader.LoaderConstants;
import com.sweetlab.sweetspot.modifiers.ModifierFactory;
import com.sweetlab.sweetspot.modifiers.ModifierType;

import java.util.List;

/**
 * The photo view pager fragment.
 */
public class ViewPagerFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<CollectionItem>> {
    /**
     * The view pager.
     */
    private ViewPager mViewPager;

    /**
     * Create a view pager fragment.
     *
     * @return A view pager fragment.
     */
    public static ViewPagerFragment createInstance() {
        return new ViewPagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_pager_view, container, false);
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LoaderConstants.VIEWPAGER, null, this);
    }

    @Override
    public Loader<List<CollectionItem>> onCreateLoader(int id, Bundle args) {
        return new CollectionLoader(getActivity().getApplicationContext(), ModifierFactory.create(ModifierType.NONE));
    }

    @Override
    public void onLoadFinished(Loader<List<CollectionItem>> loader, List<CollectionItem> list) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), list);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<CollectionItem>> loader) {
    }
}
