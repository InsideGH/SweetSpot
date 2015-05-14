package com.sweetlab.sweetspot.loader;

/**
 * This class holds the type and the object associated with the type.
 */
public class CollectionItem {
    /**
     * Held object is a photo (PhotoMeta) type.
     */
    public static final int TYPE_PHOTO = 0;

    /**
     * Held object is a date (DateMeta) type.
     */
    public static final int TYPE_DATE = 1;

    /**
     * The type.
     */
    private final int mType;

    /**
     * The held object.
     */
    private Object mObject;

    /**
     * Constructor.
     *
     * @param type Type if object to hold.
     */
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
