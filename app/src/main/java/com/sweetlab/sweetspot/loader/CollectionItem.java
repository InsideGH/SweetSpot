package com.sweetlab.sweetspot.loader;

/**
 * This class holds the type and the object associated with the type.
 */
public class CollectionItem {
    public static final int TYPE_PHOTO = 0;
    public static final int TYPE_DATE = 1;

    private final int mType;
    private Object mObject;

    public CollectionItem(int type) {
        mType = type;
    }

    /**
     * Set the object.
     *
     * @param object Object to insert.
     */
    public void setObject(Object object) {
        mObject = object;
    }

    /**
     * Get the type of the object.
     *
     * @return The type.
     */
    public int getType() {
        return mType;
    }

    /**
     * Get the object stored within this item.
     *
     * @param classType The class type.
     * @param <T>       Class generic.
     * @return The casted object.
     */
    public <T> T getObject(Class<T> classType) {
        return classType.cast(mObject);
    }
}
