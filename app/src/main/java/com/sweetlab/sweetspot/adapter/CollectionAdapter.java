package com.sweetlab.sweetspot.adapter;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import com.sweetlab.diskpicasso.DiskPicasso;
import com.sweetlab.diskpicasso.SinglePicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.photometa.PhotoMeta;
import com.sweetlab.sweetspot.photometa.MetaHelper;
import com.sweetlab.sweetspot.view.AspectImageView;

import java.io.File;

import rx.Observer;
import rx.subjects.PublishSubject;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder> {
    /**
     * Staggered layout manager seems to change the view size to span
     * all columns at a later stage, after it has received it's content (bitmap).
     * <p/>
     * In other words, the measurements are wrong.
     */
    private static final boolean USE_BANNER_FOR_NEW_DAYS = false;

    private static final Bitmap.Config JPEG_CONFIG = Bitmap.Config.RGB_565;
    private final Cursor mCursor;
    private final SparseArray<String> mDateArray;
    private PublishSubject<CollectionItemClick> mClickSubject;

    /**
     * Constructor.
     *
     * @param cursor Cursor.
     */
    public CollectionAdapter(Cursor cursor) {
        mCursor = cursor;
        mClickSubject = PublishSubject.create();

        mDateArray = new SparseArray<>();

        // TODO use rxjava.
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                int prevDay = -1;
                for (int i = 0; i < count; i++) {
                    PhotoMeta photoMeta = MetaHelper.createPhotoMeta(cursor);
                    cursor.moveToNext();

                    int day = photoMeta.getDay();
                    if (prevDay != day) {
                        prevDay = day;
                        mDateArray.put(i, photoMeta.getReadableDateTaken());
                    }
                }
            }
        }
    }

    /**
     * Register for photo clicks.
     *
     * @param observer Observer.
     */
    public void registerForClicks(Observer<? super CollectionItemClick> observer) {
        mClickSubject.subscribe(observer);
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View photo = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_collection_item, parent, false);
        return new CollectionViewHolder(photo);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        PhotoMeta photoMeta = MetaHelper.createPhotoMeta(mCursor);

        String isNewDate = mDateArray.get(position);
        if (isNewDate != null) {
            holder.getDateView().setText(isNewDate);
            holder.getDateView().setVisibility(View.VISIBLE);
        } else {
            holder.getDateView().setVisibility(View.GONE);
        }

        boolean debug = false;
        if (USE_BANNER_FOR_NEW_DAYS) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            if (isNewDate != null) {
                debug = true;
                layoutParams.setFullSpan(true);
            } else {
                layoutParams.setFullSpan(false);
            }
        }

        setClickListener(position, photoMeta, holder);
        setViewAspect(photoMeta, holder);
        loadPhoto(photoMeta, holder, debug);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Set aspect ration to image view.
     *
     * @param photoMeta Photo meta data.
     * @param holder    Holder with view.
     */
    private void setViewAspect(PhotoMeta photoMeta, CollectionViewHolder holder) {
        holder.getImageView().setAspectRatio(photoMeta.getAspectRatio());
    }

    /**
     * Load bitmap into image view.
     *
     * @param photo  The meta info.
     * @param holder The holder.
     */
    private void loadPhoto(final PhotoMeta photo, CollectionViewHolder holder, boolean debug) {
        AspectImageView imageView = holder.getImageView();
        final int width = imageView.getMeasuredWidth();
        final int height = imageView.getMeasuredHeight();

        if (debug) {
            SinglePicasso.getPicasso().load(new File(photo.getUrl())).fit().centerInside().transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Log.d("Peter100", "LocalImageAdapter.transform " + source.getWidth() + " " + source.getHeight());
                    return source;
                }

                @Override
                public String key() {
                    return photo.getUrl();
                }
            }).into(imageView);
        } else {
            DiskPicasso instance = DiskPicasso.getInstance();
            RequestCreator cacheLoader = instance.getCachedLoader(photo.getUrl(), width, height, JPEG_CONFIG);

            if (cacheLoader != null) {
                cacheLoader.into(imageView);
            } else {
                instance.getLoader(photo.getUrl(), JPEG_CONFIG).fit().centerInside().into(imageView);
            }
        }
    }

    /**
     * Set a click listener on the photo.
     *
     * @param adapterPosition Adapter position.
     * @param photoMeta       Photo meta data.
     * @param holder          Holder with view.
     */
    private void setClickListener(int adapterPosition, PhotoMeta photoMeta, CollectionViewHolder holder) {
        View.OnClickListener listener;
        holder.getImageView().setOnClickListener(new ViewOnClickListener(adapterPosition, photoMeta, holder));
    }

    /**
     * Click listener for photos.
     */
    private class ViewOnClickListener implements View.OnClickListener {
        private final int mAdapterPosition;
        private final PhotoMeta mPhotoMeta;
        private final CollectionViewHolder mHolder;

        public ViewOnClickListener(int adapterPosition, PhotoMeta photoMeta, CollectionViewHolder holder) {
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
