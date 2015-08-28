package com.gt.popularmovies.com.gt.popularmovies.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.gt.popularmovies.com.gt.popularmovies.util.Constant;

/**
 * Created by geetthaker on 8/23/15.
 */
public class MovieContract {

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
    public static final String PATH = "movie_detail";

    /**
     * Columns supported by "entries" records.
     */
    public static class MovieColumns implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        /**
         * Table name where records are stored for "List_Detail" resources.
         */
        public static final String TABLE_NAME = "MOVIE_DETAIL";

        public static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";

        public static final String COLUMN_NAME_ORIGINAL_TITLE = "ORIGINAL_TITLE";

        public static final String COLUMN_NAME_RELEASE_DATE = "RELEASE_DATE";

        public static final String COLUMN_NAME_VOTE_AVERAGE = "VOTE_AVERAGE";

        public static final String COLUMN_NAME_OVERVIEW = "OVERVIEW";

        public static final String COLUMN_NAME_POSTER_PATH = "POSTER_PATH";

    }

}
