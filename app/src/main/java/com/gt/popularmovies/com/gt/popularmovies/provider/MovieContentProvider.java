package com.gt.popularmovies.com.gt.popularmovies.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.gt.popularmovies.com.gt.popularmovies.util.SelectionBuilder;

/**
 * Created by geetthaker on 8/23/15.
 */
public class MovieContentProvider extends ContentProvider {

    public static final int movie_detail = 1;
    public static final int movie_tailer = 2;
    public static final int movie_review = 3;

    public static final UriMatcher uriMatcher;
    private DatabaseHelper databaseHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH, movie_detail);
        uriMatcher.addURI(TrailerContract.CONTENT_AUTHORITY, TrailerContract.PATH, movie_tailer);
        uriMatcher.addURI(ReviewContract.CONTENT_AUTHORITY, ReviewContract.PATH, movie_review);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = uriMatcher.match(uri);
        Cursor c = null;
        Context ctx = null;

        switch (uriMatch) {
            case movie_detail:
                // Return all known entries.
                builder.table(MovieContract.MovieColumns.TABLE_NAME).where(selection, selectionArgs);
                c = builder.query(db, projection, sortOrder);
                ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            case movie_tailer:
                // Return all known entries.
                builder.table(TrailerContract.TrailerColumns.TABLE_NAME).where(selection, selectionArgs);
                c = builder.query(db, projection, sortOrder);
                ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            case movie_review:
                // Return all known entries.
                builder.table(ReviewContract.ReviewColumns.TABLE_NAME).where(selection, selectionArgs);
                c = builder.query(db, projection, sortOrder);
                ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        assert db != null;
        final int match = uriMatcher.match(uri);
        Uri result;
        long id = 0;
        switch (match) {
            case movie_detail:
                id = db.insertOrThrow(MovieContract.MovieColumns.TABLE_NAME, null, values);
                result = Uri.parse(MovieContract.MovieColumns.CONTENT_URI + "/" + id);
                break;
            case movie_tailer:
                id = db.insertOrThrow(TrailerContract.TrailerColumns.TABLE_NAME, null, values);
                result = Uri.parse(TrailerContract.TrailerColumns.CONTENT_URI + "/" + id);
                break;
            case movie_review:
                id = db.insertOrThrow(ReviewContract.ReviewColumns.TABLE_NAME, null, values);
                result = Uri.parse(ReviewContract.ReviewColumns.CONTENT_URI + "/" + id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
