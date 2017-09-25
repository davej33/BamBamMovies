package com.android.example.bambammovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.example.bambammovies.R;
import com.android.example.bambammovies.data.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by charlotte on 9/18/17.
 */

public final class JsonUtils {

    private static String mPopularQuery = "0";
    private static String mRatingQuery = "0";
    private static final String POPULARITY = "popular";
    private static final String RATING = "top_rated";

    public static ContentValues[] parseData(String response) throws JSONException {

        // Query Api keys
        final String TITLE_KEY = "title";
        final String RELEASE_DATE_KEY = "release_date";
        final String PLOT_KEY = "overview";
        final String POPULARITY_KEY = "popularity";
        final String RATING_KEY = "vote_average";
        final String POSTER_KEY = "poster_path";
        final String ID_KEY = "id";
        final String AUTHOR_KEY = "author";
        final String REVIEW_KEY = "content";
        final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

        // create JSONArray from string results
        JSONObject root = new JSONObject(response);
        JSONArray index = root.getJSONArray("results");

        // create ContentValues[] object
        ContentValues[] cvArray = new ContentValues[index.length()];

        // iterate through json array to get data and
        for (int i = 0; i < index.length() ; i++) {
            JSONObject element = index.getJSONObject(i);

            String title = element.getString(TITLE_KEY);
            String date = element.getString(RELEASE_DATE_KEY);
            String plot = element.getString(PLOT_KEY);
            long popularity = element.getLong(POPULARITY_KEY);
            long rating = element.getLong(RATING_KEY);
            int id = element.getInt(ID_KEY);
            String poster_path = element.getString(POSTER_KEY);
            String poster = BASE_IMAGE_URL + poster_path;

            ContentValues cv = new ContentValues();
            cv.put(Contract.MovieEntry.MOVIE_TITLE, title);
            cv.put(Contract.MovieEntry.MOVIE_DATE, date);
            cv.put(Contract.MovieEntry.MOVIE_PLOT, plot);
            cv.put(Contract.MovieEntry.MOVIE_TMDB_ID, id);
            cv.put(Contract.MovieEntry.MOVIE_POPULARITY, popularity);
            cv.put(Contract.MovieEntry.MOVIE_RATING, rating);
            cv.put(Contract.MovieEntry.MOVIE_POSTER, poster);
            cv.put(Contract.MovieEntry.MOVIE_POPULARITY_QUERY, mPopularQuery);
            cv.put(Contract.MovieEntry.MOVIE_RATING_QUERY, mRatingQuery);

            Log.w("JsonUtils", "cv: " + cv);
            cvArray[i] = cv;
        }

        resetQueryTypes();

        return cvArray;
    }

    private static void resetQueryTypes() {
        mRatingQuery = "0";
        mPopularQuery = "0";
    }

    // sets db value to define movie by query type
    public static void setQueryType(String queryType) {
        switch(queryType){
            case POPULARITY:
                mPopularQuery = "1";
                break;
            case RATING:
                mRatingQuery = "1";
                break;
            default:
                Log.e("JsonUtils", "Query Type Error");
        }

    }
}
