package com.tony.demo.pagelisttest;

import android.app.Application;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * @author tony
 */
public class PageListApplication extends Application {

    private static PageListApplication INSTANCE = null;
    private BoxStore mBoxStore;

    public static PageListApplication getInstance() {
        return INSTANCE;
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mBoxStore = MyObjectBox.builder().androidContext(this).build();
        new AndroidObjectBrowser(mBoxStore).start(this);
    }
}
