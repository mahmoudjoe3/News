package com.example.sportnews;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.sportnews.pojo.Post;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SectionLoader  extends AsyncTaskLoader<List<String>> {
    private static final String TAG ="SectionLoader" ;
    URL url;
    public SectionLoader(@NonNull Context context, URL url) {
        super(context);
        this.url=url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(TAG, "LifeCycle onStartLoading: ");
        forceLoad();
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {

        Log.d(TAG, "LifeCycle loadInBackground: ");
        String JSONString = null;
        try {
            JSONString = HttpHandler.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "onCreate: makeHttpRequest error->", e);
        }
        return QueryUtils.extractSectionList(JSONString);
    }

}
