package com.example.android.newstoday;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for NEWS SOURCE data from the site
     */
    private static final String NEWS_REQUEST_URL =
            "https://newsapi.org/v1/articles?apiKey=a65fef495a5c4156b5ca3f656ef3e312";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Test=Main Activity OnCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Creates a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Sets the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Sets an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news article.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                url = currentNews.getUrl();

                /*new FetchWebsiteData().execute();*/

                Intent i = new Intent(MainActivity.this, WebViewActivity.class);

                i.putExtra("url", url);

                startActivity(i);

            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No Internet Connection");
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle bundle) {
        // Create a new loader for the given URL
        Log.i(LOG_TAG, "Test=OnCreateLoader() called....");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String category=sharedPrefs.getString(
                getString(R.string.settings_category_key),
                getString(R.string.settings_category_default)
        );

        String sortBy=sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("sortBy", sortBy);
        uriBuilder.appendQueryParameter("source", category);

        return new NewsLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        Log.i(LOG_TAG, "Test=OnLoadFinished() called...");

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found due to some error"
        mEmptyStateTextView.setText("error");

        // Clear the adapter of previous news data
        mAdapter.clear();
        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        Log.i(LOG_TAG, "Test=OnLoaderReset() called....");

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}