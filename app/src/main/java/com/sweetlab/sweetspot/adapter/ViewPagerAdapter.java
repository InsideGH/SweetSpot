package com.sweetlab.sweetspot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sweetlab.sweetspot.fragment.PhotoFragment;
import com.sweetlab.sweetspot.loader.Collection;
import com.sweetlab.sweetspot.loader.CollectionItem;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

/**
 * Adapter for paging photos.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * The list of items.
     */
    private Collection mList;

    /**
     * Pager width in pixels.
     */
    private int mWidth;

    /**
     * Pager height in pixels.
     */
    private int mHeight;

    /**
     * Constructor.
     *
     * @param fm   Support fragment manager.
     * @param list List of collection items.
     */
    public ViewPagerAdapter(FragmentManager fm, Collection list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        CollectionItem item = mList.getItems().get(position);
        switch (item.getType()) {
            case CollectionItem.TYPE_PHOTO:
                return PhotoFragment.createInstance(item.getObject(PhotoMeta.class), mWidth, mHeight);
            case CollectionItem.TYPE_DATE:
                throw new RuntimeException("wtf in PhotoPagerAdapter getItem");
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList.getItems().size();
    }

    /**
     * Set a new collection.
     *
     * @param collection
     */
    public void setCollection(Collection collection) {
        mList = collection;
        notifyDataSetChanged();
    }

    public void setDimensions(int width, int height) {
        mWidth = width;
        mHeight = height;
    }
}