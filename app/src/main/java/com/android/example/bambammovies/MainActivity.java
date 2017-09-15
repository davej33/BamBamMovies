package com.android.example.bambammovies;

import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.example.bambammovies.data.Contract;
import com.android.example.bambammovies.utils.NetworkUtils;
import com.android.example.bambammovies.utils.SyncUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static boolean sIsInitialized = false;
    private static MovieAdapter sAdapter;
    private static final int MOVIE_LOADER_ID = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup recyclerView with adapter
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        sAdapter = new MovieAdapter();
        recyclerView.setAdapter(sAdapter);
        recyclerView.setHasFixedSize(true);

        // check db init state, display data is already initialized
        if (SyncUtils.initializeDb()) {
            displayData();
        } else {
            // if db not initialized, wait for 1 second to initialize then display
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayData();
                }
            }, 1000);
        }
    }

    private void displayData() {
        if(sAdapter.getItemCount() == 0 || sAdapter == null){
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Contract.MovieEntry.MOVIE_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount() < 1 ){
            Log.e(LOG_TAG, "MainActivity cursor empty or null");
        } else {
            data.moveToFirst();
            sAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        sAdapter.swapCursor(null);

    }
}
