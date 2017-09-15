package com.android.example.bambammovies.utils;

import android.database.Cursor;

import com.android.example.bambammovies.data.Contract;
import com.android.example.bambammovies.data.DbHelper;

/**
 * Created by charlotte on 9/15/17.
 */

public final class SyncUtils {

    private static DbHelper sDbHelper;

    public static boolean sDbIsInitialized = false;

    // initialize DB
    public static boolean initializeDb() {
        if (sDbIsInitialized) return true;

        // set init value to true
        sDbIsInitialized = true;

        // Query db in background thread ----  Stopped here
        Thread thread = new Thread(new Runnable() {
            Cursor cursor;
            @Override
            public void run() {
                cursor = sDbHelper.getReadableDatabase().query(
                        Contract.MovieEntry.MOVIE_TABLE_NAME,
                        new String[]{Contract.MovieEntry.MOVIE_TMDB_ID},
                        null, null, null, null, null);

                    if (cursor == null || cursor.getCount() < 1) {
                        fetchData();
                    }
                cursor.close();
            }
        });

        return false;
    }

    private static void fetchData() {
    }
}
