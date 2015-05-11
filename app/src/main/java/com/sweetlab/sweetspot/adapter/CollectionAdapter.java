package com.sweetlab.sweetspot.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.RequestCreator;
import com.sweetlab.diskpicasso.DiskPicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.loader.CollectionItem;
import com.sweetlab.sweetspot.photometa.DateMeta;
import com.sweetlab.sweetspot.photometa.PhotoMeta;
import com.sweetlab.sweetspot.view.AspectImageView;

import java.util.List;

import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * TODO place exceptions behind debug flag.
 * <p/>
 * Adapter holding data about photos and date dividers.
 * <p/>
 * Clients can register for item clicks using a RxJava observer.
 */
public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * The decoding quality.
     */
    private static final Bitmap.Config JPEG_CONFIG = Bitmap.Config.RGB_565;

    /**
     * The list of items in the collection. Can be either photos or date dividers.
     */
    private final List<CollectionItem> mCollectionList;

    /**
     * Client can subscribe for item clicks.
     */
    private PublishSubject<CollectionItemClick> mClickSubject;

    /**
     * Constructor.
     *
     * @param list List of items.
     */
    public CollectionAdapter(List<CollectionItem> list) {
        mCollectionList = list;
        mClickSubject = PublishSubject.create();
    }

    @Override
    public int getItemViewType(int position) {
        return mCollectionList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CollectionItem.TYPE_PHOTO:
                View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_collection_item, parent, false);
                return new PhotoViewHolder(photoView);
            case CollectionItem.TYPE_DATE:
                View dateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_collection_date, parent, false);
                return new DateViewHolder(dateView);
            default:
                throw new RuntimeException("wtf in onCreateViewHolder");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectionItem collectionItem = mCollectionList.get(position);
        switch (collectionItem.getType()) {

            case CollectionItem.TYPE_PHOTO:
                PhotoMeta photoMeta = collectionItem.getObject(PhotoMeta.class);
                PhotoViewHolder photoHolder = (PhotoViewHolder) holder;

                setClickListener(position, photoMeta, photoHolder);
                configurePhotoView(photoMeta, photoHolder);
                loadPhotoView(photoMeta, photoHolder);
                break;

            case CollectionItem.TYPE_DATE:
                DateMeta dateMeta = collectionItem.getObject(DateMeta.class);
                DateViewHolder dateHolder = (DateViewHolder) holder;
                configureDateView(dateMeta, dateHolder);
                break;

            default:
                throw new RuntimeException("wtf in onBindViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mCollectionList.size();
    }

    /**
     * Subscribe for photo clicks.
     *
     * @param observer Observer.
     */
    public void subscribeForClicks(Observer<? super CollectionItemClick> observer) {
        mClickSubject.subscribe(observer);
    }

    /**
     * Configure the date TextView.
     *
     * @param dateMeta   Meta data.
     * @param dateHolder Holder of the date TextView.
     */
    private void configureDateView(DateMeta dateMeta, DateViewHolder dateHolder) {
        dateHolder.getDateTextView().setText(dateMeta.toString());
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) dateHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
    }

    /**
     * Configure the image view.
     *
     * @param photoMeta Photo meta data.
     * @param holder    Holder with view.
     */
    private void configurePhotoView(PhotoMeta photoMeta, PhotoViewHolder holder) {
        holder.getImageView().setAspectRatio(photoMeta.getAspectRatio());
    }

    /**
     * Load bitmap into image view.
     *
     * @param photo  The meta info.
     * @param holder The holder.
     */
    private void loadPhotoView(final PhotoMeta photo, PhotoViewHolder holder) {
        final AspectImageView imageView = holder.getImageView();
        final int width = imageView.getMeasuredWidth();
        final int height = imageView.getMeasuredHeight();

        DiskPicasso instance = DiskPicasso.getInstance();
        RequestCreator cacheLoader = instance.getCachedLoader(photo.getUrl(), width, height, JPEG_CONFIG);

        if (cacheLoader != null) {
            cacheLoader.into(imageView);
        } else {
            instance.getLoader(photo.getUrl(), JPEG_CONFIG).fit().centerInside().into(imageView);
        }
    }

    /**
     * Set a click listener on the photo.
     *
     * @param adapterPosition Adapter position.
     * @param photoMeta       Photo meta data.
     * @param holder          Holder with view.
     */
    private void setClickListener(int adapterPosition, PhotoMeta photoMeta, PhotoViewHolder holder) {
        holder.getImageView().setOnClickListener(new ViewOnClickListener(adapterPosition, photoMeta, holder));
    }

    /**
     * Click listener for photos.
     */
    private class ViewOnClickListener implements View.OnClickListener {
        private final int mAdapterPosition;
        private final PhotoMeta mPhotoMeta;
        private final PhotoViewHolder mHolder;

        public ViewOnClickListener(int adapterPosition, PhotoMeta photoMeta, PhotoViewHolder holder) {
            mAdapterPosition = adapterPosition;
            mPhotoMeta = photoMeta;
            mHolder = holder;
        }

        @Override
        public void onClick(View v) {
            mClickSubject.onNext(new CollectionItemClick(mAdapterPosition, mPhotoMeta, mHolder));
        }
    }
}
