package com.example.android.newstoday;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by aaryan on 17-03-2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Query URL
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<News> listOfNews = QueryUtils.fetchNewsData(mUrl);
        return listOfNews;
    }
}
