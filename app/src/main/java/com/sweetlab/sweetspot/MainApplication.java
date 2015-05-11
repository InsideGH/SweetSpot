package com.sweetlab.sweetspot;

import android.app.Application;

import com.sweetlab.diskpicasso.DiskPicasso;
import com.sweetlab.diskpicasso.SinglePicasso;

public class MainApplication extends Application {
    /**
     * The disk cache size in bytes. Used by DiskPicasso.
     */
    private static final int DISK_CACHE_SIZE = 100 * 1024 * 1024;
    /**
     * The memory cache size in bytes. Used by Picasso.
     */
    private static final int MEM_CACHE_SIZE = 32 * 1024 * 1024;
    /**
     * Enable picasso indicator?
     */
    private static final boolean INDICATOR = false;
    /**
     * Enabled picasso logging?
     */
    private static final boolean LOGGING = false;

    @Override
    public void onCreate() {
        super.onCreate();
        SinglePicasso.init(this, MEM_CACHE_SIZE, INDICATOR, LOGGING);
        DiskPicasso.init(this, DISK_CACHE_SIZE);
    }
}
