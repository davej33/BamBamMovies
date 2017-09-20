package com.android.example.bambammovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by charlotte on 9/11/17.
 */

public final class Contract {

    public static final String CONTENT_AUTHORITY = "com.android.example.bambammovies";
    public static final String MOVIE_PATH = "movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns{

        public static final Uri MOVIE_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(MOVIE_PATH)
                .build();

        public static final String MOVIE_TABLE_NAME = "movies";
        public static final String MOVIE_DB_ID = BaseColumns._ID;
        public static final String MOVIE_TMDB_ID = "server_id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_DATE = "date";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_POPULARITY = "popularity";
        public static final String MOVIE_POSTER = "poster";
        public static final String MOVIE_FAVORITE = "favorite";
        public static final String MOVIE_PLOT = "plot";
    }
}
