package com.gt.popularmovies.com.gt.popularmovies.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.gt.popularmovies.com.gt.popularmovies.util.Constant;

/**
 * Created by geetthaker on 8/23/15.
 */
public class ReviewContract {


    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = Constant.CONTENT_PROVIDER_URL;

    /**
     * Base URI. (content://com.example.android.network.sync.basicsyncadapter)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    public static final String PATH = "movie_review";

    /**
     * Columns supported by "entries" records.
     */
    public static class ReviewColumns implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        /**
         * Table name where records are stored for "List_Detail" resources.
         */
        public static final String TABLE_NAME = "MOVIE_REVIEW";

        public static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";

        public static final String COLUMN_NAME_CONTENT = "CONTENT";

        public static final String COLUMN_NAME_AUTHOR = "AUTHOR";

    }
}
