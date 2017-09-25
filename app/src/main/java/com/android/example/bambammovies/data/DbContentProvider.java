package com.android.example.bambammovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dave on 9/11/17.
 */

public class DbContentProvider extends ContentProvider {

    private static final String LOG_TAG = DbContentProvider.class.getSimpleName();
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
        SQLiteDatabase db = sDbHelper.getWritableDatabase();
        int rowsInserted = 0;

        db.beginTransaction();
        long check;
        try{
            for (ContentValues cv : values) {
                check = db.insert(Contract.MovieEntry.MOVIE_TABLE_NAME,null, cv);
                if(check != -1)rowsInserted++;
            }
            db.setTransactionSuccessful();
        } catch (SQLException e){
            Log.e(LOG_TAG, "Bulk Insert Error: " + e);
        } finally {
            db.endTransaction();
        }
        Log.i(LOG_TAG, "Rows Bulk Inserted: " + rowsInserted);
        return rowsInserted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] selection, @Nullable String selectionArgs, @Nullable String[] strings1, @Nullable String orderBy) {
        SQLiteDatabase db = sDbHelper.getReadableDatabase();
        Cursor cursor = null;

        switch(sUriMatcher.match(uri)){
            case TABLE_CODE:
                cursor = db.query(Contract.MovieEntry.MOVIE_TABLE_NAME,selection,selectionArgs,null,null,null,orderBy);
                break;
            case ITEM_CODE:
                String id = uri.getLastPathSegment();
                cursor = db.query(Contract.MovieEntry.MOVIE_TABLE_NAME,null, Contract.MovieEntry.MOVIE_DB_ID, new String[]{id},null,null,null);
                break;
            default:
                Log.e(LOG_TAG, "No Query Type Match: " + uri);
                break;
        }

        return cursor;
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
