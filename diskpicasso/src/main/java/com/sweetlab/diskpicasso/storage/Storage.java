package com.sweetlab.diskpicasso.storage;

import android.content.Context;
import android.graphics.Bitmap;

import com.sweetlab.diskpicasso.CacheEntry;
import com.sweetlab.diskpicasso.filesystem.FileSystem;
import com.sweetlab.diskpicasso.filesystem.WriteRequest;
import com.sweetlab.diskpicasso.journal.Journal;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Storage handling file and journal write, read and remove operation.
 */
public class Storage {
    private static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private final FileSystem mFileSystem;
    private final Journal mJournal;
    private final Object mGuard = new Object();

    /**
     * Constructor.
     *
     * @param context         Preferably android application context.
     * @param journal         Journal to keep entries.
     * @param compressQuality Bitmap compress quality.
     */
    public Storage(Context context, Journal journal, int compressQuality) {
        mJournal = journal;
        mFileSystem = new FileSystem(context, compressQuality, COMPRESS_FORMAT);
    }

    public Observable<CacheEntry> write(final WriteRequest req) {
        return Observable.create(new Observable.OnSubscribe<CacheEntry>() {
            @Override
            public void call(Subscriber<? super CacheEntry> subscriber) {
                try {
                    CacheEntry entry;
                    synchronized (mGuard) {
                        File cacheFile = mFileSystem.write(req);
                        entry = createCacheEntry(req, cacheFile);
                        mJournal.insert(entry);
                    }
                    subscriber.onNext(entry);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Remove from storage.
     *
     * @param entry Entry to remove.
     */
    public void remove(CacheEntry entry) {
        synchronized (mGuard) {
            mFileSystem.remove(entry);
            mJournal.remove(entry);
        }
    }

    /**
     * Fetch from storage.
     *
     * @return All entries.
     */
    public CacheEntry[] fetchAll() {
        synchronized (mGuard) {
            return mJournal.retrieveAll();
        }
    }

    /**
     * Create a cache entry.
     *
     * @param req       Write request.
     * @param cacheFile The cache file to create.
     * @return The cache entry created.
     */
    private CacheEntry createCacheEntry(WriteRequest req, File cacheFile) {
        Bitmap bitmap = req.getBitmap();
        String path = req.getPath();
        return new CacheEntry(path, cacheFile, bitmap.getWidth(), bitmap.getHeight(),
                bitmap.getConfig(), (int) cacheFile.length());
    }
}