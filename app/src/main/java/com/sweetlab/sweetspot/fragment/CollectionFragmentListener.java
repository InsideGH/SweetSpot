package com.sweetlab.sweetspot.fragment;

import com.sweetlab.sweetspot.adapter.CollectionItemClick;

/**
 * A collection fragment listener.
 */
public interface CollectionFragmentListener {
    /**
     * Called when a item in the collection is clicked.
     * @param collectionItemClick
     */
    void onItemClicked(CollectionItemClick collectionItemClick);
}
