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

public class LocalImageAdapter extends RecyclerView.Adapter<LocalImageAdapter.ImageViewHolder> {
    private static final Bitmap.Config JPEG_CONFIG = Bitmap.Config.RGB_565;
    private final Cursor mCursor;

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private final View mPhoto;
        private final AspectImageView mImageView;

        public ImageViewHolder(View photo) {
            super(photo);
            mPhoto = photo;
            mImageView = (AspectImageView) mPhoto.findViewById(R.id.photo_image);
        }
    }

    /**
     * Constructor.
     *
     * @param cursor Cursor.
     */
    public LocalImageAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View photo = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo, parent, false);
        return new ImageViewHolder(photo);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        LocalPhoto photoMeta = createPhotoMeta(position);

        setViewAspect(photoMeta, holder);
        loadPhoto(photoMeta, holder);
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
    private void setViewAspect(LocalPhoto photoMeta, ImageViewHolder holder) {
        holder.mImageView.setAspectRatio(photoMeta.getAspectRatio());
    }

    /**
     * Load bitmap into image view.
     *
     * @param photo  The meta info.
     * @param holder The holder.
     */
    private void loadPhoto(final LocalPhoto photo, ImageViewHolder holder) {
        final int width = holder.mImageView.getMeasuredWidth();
        final int height = holder.mImageView.getMeasuredHeight();

        DiskPicasso instance = DiskPicasso.getInstance();
        RequestCreator cacheLoader = instance.getCachedLoader(photo.getUrl(), width, height, JPEG_CONFIG);

        if (cacheLoader != null) {
            cacheLoader.into(holder.mImageView);
        } else {
            instance.getLoader(photo.getUrl(), JPEG_CONFIG).fit().centerInside().into(holder.mImageView);
        }
    }
}
