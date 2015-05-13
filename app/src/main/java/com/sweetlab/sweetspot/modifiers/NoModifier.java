package com.sweetlab.sweetspot.modifiers;

import com.sweetlab.sweetspot.loader.CollectionItem;

import rx.Observable;

/**
 * A modifier that doesn't alter the source.
 */
public class NoModifier implements CollectionModifier {

    @Override
    public Observable<CollectionItem> call(CollectionItem photoItem) {
        return Observable.just(photoItem);
    }
}
