package com.android.example.bambammovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.example.bambammovies.BuildConfig;
import com.android.example.bambammovies.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by charlotte on 9/12/17.
 */

public class NetworkUtils {

    private static RequestQueue sRequestQueue;
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String TAG = "default";



    public static void initRequestQueue(Context context) {
        if(sRequestQueue == null) sRequestQueue = Volley.newRequestQueue(context);
    }

    public static void addToRequestQueue(StringRequest stringRequest, String tag){
        stringRequest.setTag(TextUtils.isEmpty(tag) ? TAG : tag );
        sRequestQueue.add(stringRequest);
    }

    // build movie query url
    public static String buildMovieUrl(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sort = preferences.getString(context.getString(R.string.pref_sort_fav_label), context.getString(R.string.pref_sort_default));

        Uri uri = Uri.parse(context.getString(R.string.query_base_url) + sort + "?").buildUpon()
                .appendQueryParameter(context.getString(R.string.api_code_key), BuildConfig.MOVIE_API_KEY)
                .build();

        Log.i(LOG_TAG, "Url: " + uri.toString());
        return uri.toString();
    }
}
