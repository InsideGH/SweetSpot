package com.sweetlab.sweetspot.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetlab.diskpicasso.CacheEntry;
import com.sweetlab.diskpicasso.DiskPicasso;
import com.sweetlab.diskpicasso.SinglePicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.loader.Collection;
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
    private final Collection mCollection;

    /**
     * View orientation.
     */
    private final int mViewOrientation;

    /**
     * View span.
     */
    private final int mViewSpan;

    /**
     * Client can subscribe for item clicks.
     */
    private PublishSubject<CollectionItemClick> mClickSubject;

    /**
     * The view that this adapter feeds.
     */
    private RecyclerView mRecyclerView;

    /**
     * Lazy loaded bounded layout.
     */
    private BoundedLayout mBoundedLayout;

    /**
     * Constructor.
     *
     * @param list List of items.
     */
    public CollectionAdapter(Collection list, int orientation, int span) {
        mViewOrientation = orientation;
        mViewSpan = span;
        mCollection = list;
        mClickSubject = PublishSubject.create();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        if (mViewOrientation == StaggeredGridLayoutManager.VERTICAL) {
            mBoundedLayout = new BoundedLayout(BoundedLayout.BoundedDirection.HORIZONTAL, mRecyclerView.getMeasuredWidth() / mViewSpan);
        } else {
            mBoundedLayout = new BoundedLayout(BoundedLayout.BoundedDirection.VERTICAL, mRecyclerView.getMeasuredHeight());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mCollection.getItems().get(position).getType();
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
        CollectionItem collectionItem = mCollection.getItems().get(position);
        switch (collectionItem.getType()) {

            case CollectionItem.TYPE_PHOTO:
                PhotoMeta photoMeta = collectionItem.getObject(PhotoMeta.class);
                PhotoViewHolder photoHolder = (PhotoViewHolder) holder;

                int unmodifiedPosition = mCollection.getUnmodifiedPosition(position);
                setClickListener(position, unmodifiedPosition, photoMeta, photoHolder);
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
        return mCollection.getItems().size();
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
        final DiskPicasso instance = DiskPicasso.getInstance();
        List<CacheEntry> cacheEntries = instance.getCacheEntries(photo.getUrl());

        final int resizeX = calcPicassoResizeX(photo);
        final int resizeY = calcPicassoResizeY(photo);
        CacheEntry match = DiskPicasso.findMatch(cacheEntries, resizeX, resizeY, JPEG_CONFIG);
        if (match != null) {
            Log.d("Peter100", "CollectionAdapter.loadPhotoView cached " + match);
            SinglePicasso.getPicasso().load(match.getCacheFile()).into(imageView);
        } else {
            Log.d("Peter100", "CollectionAdapter.loadPhotoView not cached " + photo);
            instance.loadWithCacheWrite(photo.getUrl(), JPEG_CONFIG).resize(resizeX, resizeY).into(imageView);
        }
    }

    /**
     * Calculate the resize value to be used for picasso based on boundaries and photo rotation.
     *
     * @param photo The photo meta data.
     * @return The horizontal (x) resize value.
     */
    private int calcPicassoResizeX(PhotoMeta photo) {
        switch (mBoundedLayout.getBoundedDirection()) {
            case HORIZONTAL:
                if (photo.isPortrait()) {
                    return 0;
                } else {
                    return mBoundedLayout.getBoundedValue();
                }
            case VERTICAL:
                if (photo.isPortrait()) {
                    return mBoundedLayout.getBoundedValue();
                } else {
                    return 0;
                }
            default:
                throw new RuntimeException("wtf");
        }
    }

    /**
     * Calculate the resize value to be used for picasso based on boundaries and photo rotation.
     *
     * @param photo The photo meta data.
     * @return The vertical (y) resize value.
     */
    private int calcPicassoResizeY(PhotoMeta photo) {
        switch (mBoundedLayout.getBoundedDirection()) {
            case HORIZONTAL:
                if (photo.isPortrait()) {
                    return mBoundedLayout.getBoundedValue();
                } else {
                    return 0;
                }
            case VERTICAL:
                if (photo.isPortrait()) {
                    return 0;
                } else {
                    return mBoundedLayout.getBoundedValue();
                }
            default:
                throw new RuntimeException("wtf");
        }
    }

    /**
     * Set a click listener on the photo.
     *
     * @param adapterPosition    Adapter position.
     * @param unmodifiedPosition
     * @param photoMeta          Photo meta data.
     * @param holder             Holder with view.
     */
    private void setClickListener(int adapterPosition, int unmodifiedPosition, PhotoMeta photoMeta, PhotoViewHolder holder) {
        holder.getImageView().setOnClickListener(new ViewOnClickListener(adapterPosition, unmodifiedPosition, photoMeta, holder));
    }

    /**
     * Click listener for photos.
     */
    private class ViewOnClickListener implements View.OnClickListener {
        private final int mAdapterPosition;
        private final PhotoMeta mPhotoMeta;
        private final PhotoViewHolder mHolder;
        private final int mUnmodifiedPosition;

        public ViewOnClickListener(int adapterPosition, int unmodifiedPosition, PhotoMeta photoMeta, PhotoViewHolder holder) {
            mAdapterPosition = adapterPosition;
            mUnmodifiedPosition = unmodifiedPosition;
            mPhotoMeta = photoMeta;
            mHolder = holder;
        }

        @Override
        public void onClick(View v) {
            mClickSubject.onNext(new CollectionItemClick(mAdapterPosition, mUnmodifiedPosition, mPhotoMeta, mHolder));
        }
    }
}
