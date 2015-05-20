package com.sweetlab.sweetspot.loader;

import android.util.SparseArray;

import java.util.List;

/**
 * This hold a collection of items.
 */
public class Collection {
    private final List<CollectionItem> mList;
    private final SparseArray<Integer> mUnmodifiedPositions;

    /**
     * Constructor.
     *
     * @param list A list of items that this collection will hold.
     */
    public Collection(List<CollectionItem> list) {
        mList = list;
        mUnmodifiedPositions = scanItems(list);
    }

    /**
     * Get the item collection.
     *
     * @return The item collection.
     */
    public List<CollectionItem> getItems() {
        return mList;
    }

    /**
     * Convert the local adapter position to a photos only adapter position.
     *
     * @param adapterPosition The adapter position in this collections space.
     * @return The converted adapter position (mapped to a photo only collection).
     */
    public int getUnmodifiedPosition(int adapterPosition) {
        return mUnmodifiedPositions.get(adapterPosition);
    }

    /**
     * Creates a sparse array to be able to retrieve adapter position to be used in a unmodified collection (just photos).
     *
     * @param list A collection of items.
     * @return A sparse array to be used to convert positions.
     */
    private SparseArray<Integer> scanItems(List<CollectionItem> list) {
        SparseArray<Integer> sparseArray = new SparseArray<>();
        int position = 0;
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getType() == CollectionItem.TYPE_PHOTO) {
                sparseArray.put(i, position);
                position++;
            }
        }
        return sparseArray;
    }
}
