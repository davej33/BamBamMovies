package com.android.example.bambammovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.example.bambammovies.MainFragment;
import com.android.example.bambammovies.data.Contract;
import com.android.example.bambammovies.data.DbHelper;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.net.URL;
import java.sql.SQLException;

/**
 * Created by charlotte on 9/15/17.
 */

public final class SyncUtils {

    private static final String LOG_TAG = SyncUtils.class.getSimpleName();
    private static DbHelper sDbHelper;
    private static Context mContext;

    public static boolean sDbIsInitialized = false;

    // initialize DB
    public static boolean isInitializeDb(final Context context) {
        mContext = context;
        if (sDbIsInitialized) return true;

        // set init value to true
        sDbIsInitialized = true;

        // initialize DB
        initDb(context);

        // Query db in background thread
        Thread thread = new Thread(new Runnable() {
            Cursor cursor;
            @Override
            public void run() {
                cursor = sDbHelper.getReadableDatabase().query(
                        Contract.MovieEntry.MOVIE_TABLE_NAME,
                        new String[]{Contract.MovieEntry.MOVIE_TMDB_ID},
                        null, null, null, null, null);

                    if (cursor == null || cursor.getCount() < 1) {

                        // initialize request queue
                        NetworkUtils.initRequestQueue(context);

                        // builds url string
                        StringRequest initStringRequest = new StringRequest(NetworkUtils.buildMovieUrl(context), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // json parse
                                ContentValues[] cvArray = null;
                                try{
                                    cvArray = JsonUtils.parseData(response);
                                    if(cvArray != null) insertData(context, cvArray);
                                } catch (JSONException e){
                                    Log.e(LOG_TAG, "json parse error: " + e);
                                }



                                Log.i(LOG_TAG, "Init StringRequest Response: " + response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                checkVolleyError(error);
                            }
                        }) ;

                        // add string request to request queue
                        NetworkUtils.addToRequestQueue(initStringRequest, null);
            }
                cursor.close();
            }
        });
        thread.run();

        return false;
    }

    private static void insertData(Context context, ContentValues[] cvArray) {
        int count = 0;
        try{
            count = context.getContentResolver().bulkInsert(Contract.MovieEntry.MOVIE_URI, cvArray);
        } catch (Exception e){
            Log.e(LOG_TAG, "Bulk Insert Error: " + count);
        }

        Log.e(LOG_TAG, "RowsInserted: " + count);
    }

    private static void initDb(Context context) {
        sDbHelper = new DbHelper(context);
    }

    private static  void checkVolleyError(VolleyError error){
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();

        } else if (error instanceof AuthFailureError) {
            Toast.makeText(mContext, "Authentication Error!", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(mContext, "Server Side Error!", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(mContext, "Network Error!", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(mContext, "Parse Error!", Toast.LENGTH_SHORT).show();
        }
    }


}
