package com.android.example.bambammovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by dave on 9/11/17.
 */

public class DbContentProvider extends ContentProvider {

    private static final int TABLE_CODE = 100;
    private static final int ITEM_CODE = 101;
    private static UriMatcher sUriMatcher = getUriMatcher();
    private static DbHelper sDbHelper;

    private static UriMatcher getUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.CONTENT_AUTHORITY, Contract.MOVIE_PATH, 100);
        matcher.addURI(Contract.CONTENT_AUTHORITY + "/#", Contract.MOVIE_PATH, 101);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        sDbHelper = new DbHelper(getContext()); // stopped here, next code bulkinsert
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        // TODO code
        return super.bulkInsert(uri, values);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        // TODO code

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
