package com.sweetlab.sweetspot.modifiers;

import com.sweetlab.sweetspot.loader.CollectionItem;
import com.sweetlab.sweetspot.photometa.DateMeta;
import com.sweetlab.sweetspot.photometa.PhotoMeta;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * This RxJava method takes one input and produces multiple (max 2) outputs. To
 * be used in RxJava flatMap methods for example.
 */
public class DayDividerModifier implements CollectionModifier {
    private int prevDay = -1;

    @Override
    public Observable<CollectionItem> call(CollectionItem photoItem) {
        List<CollectionItem> list = new ArrayList<>();
        PhotoMeta photo = photoItem.getObject(PhotoMeta.class);
        int day = photo.getDay();
        if (day != prevDay) {
            prevDay = day;
            CollectionItem dateItem = new CollectionItem(CollectionItem.TYPE_DATE);
            dateItem.setObject(new DateMeta(photo.getReadableDateTaken()));
            list.add(dateItem);
        }
        list.add(photoItem);
        return Observable.from(list);
    }
}
