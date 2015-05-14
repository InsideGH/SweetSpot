package com.sweetlab.sweetspot.modifiers;

import com.sweetlab.sweetspot.loader.CollectionItem;

import rx.Observable;
import rx.functions.Func1;

/**
 * Collection modifier that modified a collection using RxJava func1.
 */
public interface CollectionModifier extends Func1<CollectionItem, Observable<CollectionItem>> {
}
