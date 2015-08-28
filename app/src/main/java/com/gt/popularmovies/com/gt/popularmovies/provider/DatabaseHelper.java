package com.gt.popularmovies.com.gt.popularmovies.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geetthaker on 8/23/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Database specific constant declarations
     */
    public static final String DATABASE_NAME = "moviedb.db";
    public static final int DATABASE_VERSION = 16;
    private List<String> tablesCreateStat;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        tablesCreateStat = getAllTablesCreateStat();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String createStat : tablesCreateStat) {
            db.execSQL(createStat);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    private ArrayList<String> getAllTablesCreateStat() {
        ArrayList<String> createStat = new ArrayList<String>();

        String statement = " CREATE TABLE IF NOT EXISTS " + MovieContract.MovieColumns.TABLE_NAME + " (" + MovieContract.MovieColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MovieContract.MovieColumns.COLUMN_NAME_MOVIE_ID + " INTEGER, " + MovieContract.MovieColumns.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, "
                + MovieContract.MovieColumns.COLUMN_NAME_RELEASE_DATE + " TEXT, " + MovieContract.MovieColumns.COLUMN_NAME_VOTE_AVERAGE
                + " TEXT, " + MovieContract.MovieColumns.COLUMN_NAME_OVERVIEW + " TEXT, "
                + MovieContract.MovieColumns.COLUMN_NAME_POSTER_PATH + " TEXT);";

        createStat.add(statement);
        // END

        statement = " CREATE TABLE IF NOT EXISTS " + TrailerContract.TrailerColumns.TABLE_NAME + " (" + TrailerContract.TrailerColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TrailerContract.TrailerColumns.COLUMN_NAME_MOVIE_ID + " INTEGER, " + TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_KEY + " TEXT, "
                + TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_NAME + " TEXT);";

        createStat.add(statement);
        // END

        statement = " CREATE TABLE IF NOT EXISTS " + ReviewContract.ReviewColumns.TABLE_NAME + " (" + ReviewContract.ReviewColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ReviewContract.ReviewColumns.COLUMN_NAME_MOVIE_ID + " INTEGER, " + ReviewContract.ReviewColumns.COLUMN_NAME_CONTENT + " TEXT, "
                + ReviewContract.ReviewColumns.COLUMN_NAME_AUTHOR + " TEXT);";

        createStat.add(statement);
        // END

        return createStat;
    }
}
