package com.sweetlab.sweetspot.adapter;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.RequestCreator;
import com.sweetlab.diskpicasso.DiskPicasso;
import com.sweetlab.sweetspot.R;
import com.sweetlab.sweetspot.photometa.LocalPhoto;
import com.sweetlab.sweetspot.view.AspectImageView;

public class LocalImageAdapter extends RecyclerView.Adapter<PhotoHolder> {
    private static final Bitmap.Config JPEG_CONFIG = Bitmap.Config.RGB_565;
    private final Cursor mCursor;

    /**
     * Constructor.
     *
     * @param cursor Cursor.
     */
    public LocalImageAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View photo = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
        return new PhotoHolder(photo);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        LocalPhoto photoMeta = createPhotoMeta(position);
        setOnClickListener(position, holder);
        setViewAspect(photoMeta, holder);
        loadPhoto(photoMeta, holder);
    }

    private void setOnClickListener(int position, PhotoHolder holder) {

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Create photo meta data from cursor.
     *
     * @param position Position in cursor.
     * @return The meta data.
     */
    private LocalPhoto createPhotoMeta(int position) {
        mCursor.moveToPosition(position);
        final String path = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
        final int width = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.WIDTH));
        final int height = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.HEIGHT));
        final int orientation = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION));
        return new LocalPhoto(path, width, height, orientation);
    }

    /**
     * Set aspect ration to image view.
     *
     * @param photoMeta Photo meta data.
     * @param holder    Holder with view.
     */
    private void setViewAspect(LocalPhoto photoMeta, PhotoHolder holder) {
        holder.getImageView().setAspectRatio(photoMeta.getAspectRatio());
    }

    /**
     * Load bitmap into image view.
     *
     * @param photo  The meta info.
     * @param holder The holder.
     */
    private void loadPhoto(final LocalPhoto photo, PhotoHolder holder) {
        AspectImageView imageView = holder.getImageView();
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
}
