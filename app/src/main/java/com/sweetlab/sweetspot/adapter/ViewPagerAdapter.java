package com.sweetlab.sweetspot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sweetlab.sweetspot.fragment.PhotoFragment;
import com.sweetlab.sweetspot.loader.CollectionItem;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

import java.util.List;

/**
 * Adapter for paging photos.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * The list of items.
     */
    private final List<CollectionItem> mList;

    /**
     * Constructor.
     *
     * @param fm   Support fragment manager.
     * @param list List of collection items.
     */
    public ViewPagerAdapter(FragmentManager fm, List<CollectionItem> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        CollectionItem item = mList.get(position);
        switch (item.getType()) {
            case CollectionItem.TYPE_PHOTO:
                return PhotoFragment.createInstance(item.getObject(PhotoMeta.class), null, true, true);
            case CollectionItem.TYPE_DATE:
                throw new RuntimeException("wtf in PhotoPagerAdapter getItem");
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
