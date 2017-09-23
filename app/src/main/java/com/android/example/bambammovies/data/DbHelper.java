package com.android.example.bambammovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by charlotte on 9/11/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie_db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + Contract.MovieEntry.MOVIE_TABLE_NAME + "(" +

                Contract.MovieEntry.MOVIE_DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                Contract.MovieEntry.MOVIE_PLOT + " TEXT NOT NULL, " +
                Contract.MovieEntry.MOVIE_TMDB_ID + " INTEGER UNIQUE NOT NULL, " +
                Contract.MovieEntry.MOVIE_DATE + " TEXT NOT NULL, " +
                Contract.MovieEntry.MOVIE_POPULARITY + " INTEGER NOT NULL, " +
                Contract.MovieEntry.MOVIE_RATING + " INTEGER NOT NULL, " +
                Contract.MovieEntry.MOVIE_POSTER + " TEXT, " +
                Contract.MovieEntry.MOVIE_POPULARITY_QUERY + " INTEGER NOT NULL, " +
                Contract.MovieEntry.MOVIE_RATING_QUERY + " INTEGER NOT NULL, " +
                Contract.MovieEntry.MOVIE_FAVORITE + " INTEGER DEFAULT 0, " +
                " UNIQUE (" + Contract.MovieEntry.MOVIE_TMDB_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(createDb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MovieEntry.MOVIE_TABLE_NAME);
        onCreate(db);
    }
}
