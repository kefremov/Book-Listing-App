package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;



public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;

        Log.i(LOG_TAG, ": Loaded.");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i("On start loading", ": Force loaded.");
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Book> books = Utils.fetchBookData(mUrl);
        Log.i(LOG_TAG, ": Loaded in background.");
        return books;

    }
}
