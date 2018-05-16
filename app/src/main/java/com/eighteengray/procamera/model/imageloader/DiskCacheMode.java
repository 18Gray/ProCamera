package com.eighteengray.procamera.model.imageloader;

public enum DiskCacheMode
{
    /** Caches with both {@link #SOURCE} and {@link #RESULT}. */
    ALL(true, true),
    /** Saves no data to cache. */
    NONE(false, false),
    /** Saves just the original data to cache. */
    SOURCE(true, false),
    /** Saves the media item after all transformations to cache. */
    RESULT(false, true);

    private final boolean cacheSource;
    private final boolean cacheResult;

    DiskCacheMode(boolean cacheSource, boolean cacheResult){
        this.cacheSource = cacheSource;
        this.cacheResult = cacheResult;
    }
}
