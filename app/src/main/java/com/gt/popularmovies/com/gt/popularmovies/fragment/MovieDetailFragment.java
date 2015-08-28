package com.gt.popularmovies.com.gt.popularmovies.fragment;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.popularmovies.MovieDetailActivity;
import com.gt.popularmovies.R;
import com.gt.popularmovies.com.gt.popularmovies.model.MovieReview;
import com.gt.popularmovies.com.gt.popularmovies.model.MovieTrailer;
import com.gt.popularmovies.com.gt.popularmovies.model.Review;
import com.gt.popularmovies.com.gt.popularmovies.model.Trailer;
import com.gt.popularmovies.com.gt.popularmovies.provider.MovieContract;
import com.gt.popularmovies.com.gt.popularmovies.provider.ReviewContract;
import com.gt.popularmovies.com.gt.popularmovies.provider.TrailerContract;
import com.gt.popularmovies.com.gt.popularmovies.util.Constant;
import com.gt.popularmovies.com.gt.popularmovies.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by geetthaker on 8/25/15.
 */
public class MovieDetailFragment extends Fragment {

    private static String MOVIE_TRAILER = "MOVIE_TRAILER";
    private static String MOVIE_REVIEW = "MOVIE_REVIEW";
    private static String MAP_LIST = "MAP_LIST";

    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String posterPath;
    private Context mContext;
    private LinearLayout movieTrailerLinerLayout, movieReviewLinerLayout, movieDetailLayout;
    private ProgressBar trailerProgressbar, reviewProgressBar;
    private Bitmap bitmap;
    private ImageView posterImage;
    //private Intent intent;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private boolean fav;
    private TextView movieNameTextView, movieRattingTextView, movieReleaseDateTextView, movieOverviewTextView;
    private HashMap<String, Object> mMap;
    private Button mMarkAsFavoriteButton;
    private RelativeLayout movieSelectionMessageLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        mContext = getActivity();

        //intent = getIntent();

        movieNameTextView = (TextView) view.findViewById(R.id.movie_name_text_view);
        posterImage = (ImageView) view.findViewById(R.id.movie_poster_image);
        movieRattingTextView = (TextView) view.findViewById(R.id.movie_ratting);
        movieReleaseDateTextView = (TextView) view.findViewById(R.id.movie_release_date);
        movieOverviewTextView = (TextView) view.findViewById(R.id.movie_overview);
        movieTrailerLinerLayout = (LinearLayout) view.findViewById(R.id.movie_trailer_liner_layout);
        trailerProgressbar = (ProgressBar) view.findViewById(R.id.movie_trailer_progressBar);
        movieReviewLinerLayout = (LinearLayout) view.findViewById(R.id.movie_review_liner_layout);
        reviewProgressBar = (ProgressBar) view.findViewById(R.id.movie_review_progressBar);
        mMarkAsFavoriteButton = (Button) view.findViewById(R.id.mark_as_favorite_button);
        movieDetailLayout = (LinearLayout) view.findViewById(R.id.movie_detail_layout);
        movieSelectionMessageLayout = (RelativeLayout) view.findViewById(R.id.movie_selection_message_layout);


        if (savedInstanceState != null) {
            movieSelectionMessageLayout.setVisibility(View.GONE);
            movieDetailLayout.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_TRAILER) && savedInstanceState.containsKey(MOVIE_REVIEW) && savedInstanceState.containsKey(MAP_LIST)) {
            mTrailers = savedInstanceState.getParcelableArrayList(MOVIE_TRAILER);
            mReviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEW);
            HashMap<String, Object> map = (HashMap<String, Object>) savedInstanceState.getSerializable(MAP_LIST);
            updateScreen(map);
        }

        mMarkAsFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteMovie();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_TRAILER, mTrailers);
        outState.putParcelableArrayList(MOVIE_REVIEW, mReviews);
        outState.putSerializable(MAP_LIST, mMap);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                ((MovieDetailActivity) getActivity()).finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateScreen(HashMap<String, Object> map) {
        if (map != null) {
            movieSelectionMessageLayout.setVisibility(View.GONE);
            movieDetailLayout.setVisibility(View.VISIBLE);
            mMap = map;
            fav = (Boolean) Boolean.valueOf(String.valueOf(map.get(Constant.MOVIE_FAV)));
            movieNameTextView.setText((String) map.get(String.valueOf(Constant.MOVIE_ORIGINAL_TITLE_EXTRA)));
            String baseUrl = Constant.IMAGE_BASE_URL;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                baseUrl += Constant.IMAGE_POSTER_WIDTH_L;
            } else {
                baseUrl += Constant.IMAGE_POSTER_WIDTH;
            }

            movieRattingTextView.setText(Constant.MOVIE_RATTING_TEXT + "" + Double.toString(Double.parseDouble(String.valueOf(map.get(Constant.MOVIE_VOTE_AVERAGE_EXTRA)))));
            movieReleaseDateTextView.setText(Constant.MOVIE_RELEASE_DATE_TEXT + "" + map.get(String.valueOf(Constant.MOVIE_RELEASE_DATE_EXTRA)));
            movieOverviewTextView.setText(String.valueOf(map.get(Constant.MOVIE_OVERVIEW_EXTRA)));

            long movieId = (long) Long.parseLong(String.valueOf(map.get(Constant.MOVIE_ID_EXTRA)));
            if (fav) {
                Picasso.with(mContext).load("file:" + String.valueOf(map.get(Constant.MOVIE_POSTER_IMAGE_EXTRA))).into(posterImage);
                getFavoriteTrailers(movieId);
                getFavoriteReviews(movieId);
            } else {
                Picasso.with(mContext).load(baseUrl + String.valueOf(map.get(Constant.MOVIE_POSTER_IMAGE_EXTRA))).into(posterImage);
                if (mTrailers != null) {
                    prepareTrailer(mTrailers);
                } else {
                    trailerProgressbar.setVisibility(View.VISIBLE);
                    movieTrailerLinerLayout.removeAllViews();
                    getTrailers(movieId);
                }
                if (mReviews != null) {
                    prepareReview(mReviews);
                } else {
                    reviewProgressBar.setVisibility(View.VISIBLE);
                    movieReviewLinerLayout.removeAllViews();
                    getReviews(movieId);
                }
            }
        }
    }

    public void favouriteMovie() {
        bitmap = ((BitmapDrawable) posterImage.getDrawable()).getBitmap();
        String fileName = writeToFile();
        saveMovie(fileName);
        saveMovieTrailer();
        saveMovieReview();
    }

    public void saveMovie(String fileName) {
        ContentValues values = new ContentValues();
        values.put(TrailerContract.TrailerColumns.COLUMN_NAME_MOVIE_ID, Long.parseLong(String.valueOf(mMap.get(Constant.MOVIE_ID_EXTRA))));
        values.put(MovieContract.MovieColumns.COLUMN_NAME_ORIGINAL_TITLE, String.valueOf(mMap.get(Constant.MOVIE_ORIGINAL_TITLE_EXTRA)));
        values.put(MovieContract.MovieColumns.COLUMN_NAME_RELEASE_DATE, String.valueOf(mMap.get(Constant.MOVIE_RELEASE_DATE_EXTRA)));
        values.put(MovieContract.MovieColumns.COLUMN_NAME_VOTE_AVERAGE, Double.toString(Double.parseDouble(String.valueOf(mMap.get(Constant.MOVIE_VOTE_AVERAGE_EXTRA)))));
        values.put(MovieContract.MovieColumns.COLUMN_NAME_OVERVIEW, String.valueOf(mMap.get(Constant.MOVIE_OVERVIEW_EXTRA)));
        values.put(MovieContract.MovieColumns.COLUMN_NAME_POSTER_PATH, fileName);

        Uri uri = mContext.getContentResolver().insert(MovieContract.MovieColumns.CONTENT_URI, values);
        long id = ContentUris.parseId(uri);
    }

    public void saveMovieTrailer() {
        if (mTrailers != null) {
            for (Trailer trailer : mTrailers) {
                ContentValues values = new ContentValues();
                values.put(TrailerContract.TrailerColumns.COLUMN_NAME_MOVIE_ID, Long.parseLong(String.valueOf(mMap.get(Constant.MOVIE_ID_EXTRA))));
                values.put(TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_KEY, trailer.getKey());
                values.put(TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_NAME, trailer.getName());

                Uri uri = mContext.getContentResolver().insert(TrailerContract.TrailerColumns.CONTENT_URI, values);
                long id = ContentUris.parseId(uri);
            }
        }
    }

    public void saveMovieReview() {
        if (mTrailers != null) {
            for (Review review : mReviews) {
                ContentValues values = new ContentValues();
                values.put(ReviewContract.ReviewColumns.COLUMN_NAME_MOVIE_ID, Long.parseLong(String.valueOf(mMap.get(Constant.MOVIE_ID_EXTRA))));
                values.put(ReviewContract.ReviewColumns.COLUMN_NAME_CONTENT, review.getContent());
                values.put(ReviewContract.ReviewColumns.COLUMN_NAME_AUTHOR, review.getAuthor());

                Uri uri = mContext.getContentResolver().insert(ReviewContract.ReviewColumns.CONTENT_URI, values);
                long id = ContentUris.parseId(uri);
            }
        }
    }


    public void getTrailers(final long movieId) {
        if (movieId > 0) {
            new AsyncTask<Void, Void, ArrayList<Trailer>>() {
                @Override
                protected ArrayList<Trailer> doInBackground(Void... params) {
                    try {
                        String result = Util.makeHttpGetCall(Constant.MOVIE_EXTRA_INFO_URL + movieId + Constant.TRAILER + "?" + Constant.API_KEY_PARAMS + "=" + Constant.MOVIE_KEY);
                        MovieTrailer movieTrailor = null;
                        if (result != null && !result.isEmpty()) {
                            movieTrailor = new Gson().fromJson(result, MovieTrailer.class);
                        }
                        if (movieTrailor != null) {
                            return (ArrayList<Trailer>) movieTrailor.getResults();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<Trailer> trailers) {
                    prepareTrailer(trailers);
                }
            }.execute();
        }
    }

    public void prepareTrailer(ArrayList<Trailer> trailers) {
        if (trailers != null) {
            mTrailers = trailers;
            trailerProgressbar.setVisibility(View.GONE);
            movieTrailerLinerLayout.removeAllViews();
            for (final Trailer trailer : mTrailers) {
                LayoutInflater vi = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View option = vi.inflate(R.layout.movie_trailor, null);
                ImageView imageView = (ImageView) option.findViewById(R.id.tailor_play_image);
                TextView textView = (TextView) option.findViewById(R.id.movie_tailor_name_text_view);

                textView.setText(trailer.getName());
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openYoutube(trailer.getKey());
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openYoutube(trailer.getKey());
                    }
                });

                movieTrailerLinerLayout.addView(option);
            }
            mTrailers = null;
        }
    }

    public void getFavoriteTrailers(final long movieId) {
        if (movieId > 0) {
            new AsyncTask<Void, Void, ArrayList<Trailer>>() {
                @Override
                protected ArrayList<Trailer> doInBackground(Void... params) {
                    ArrayList<Trailer> trailers = getFevoritesTrailorList(movieId);
                    if (trailers != null) {
                        return trailers;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<Trailer> trailers) {
                    prepareTrailer(trailers);
                }
            }.execute();
        }
    }


    public void getReviews(final long movieId) {
        if (movieId > 0) {
            new AsyncTask<Void, Void, ArrayList<Review>>() {
                @Override
                protected ArrayList<Review> doInBackground(Void... params) {
                    try {
                        String result = Util.makeHttpGetCall(Constant.MOVIE_EXTRA_INFO_URL + movieId + Constant.REVIEW + "?" + Constant.API_KEY_PARAMS + "=" + Constant.MOVIE_KEY);
                        MovieReview movieReview = null;
                        if (result != null && !result.isEmpty()) {
                            movieReview = new Gson().fromJson(result, MovieReview.class);
                        }
                        if (movieReview != null) {
                            return (ArrayList<Review>) movieReview.getResults();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<Review> reviews) {
                    prepareReview(reviews);
                }
            }.execute();
        }

    }

    public void prepareReview(ArrayList<Review> reviews) {
        if (reviews != null) {
            mReviews = reviews;
            reviewProgressBar.setVisibility(View.GONE);
            movieReviewLinerLayout.removeAllViews();
            for (Review review : mReviews) {
                LayoutInflater vi = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View option = vi.inflate(R.layout.movie_review, null);
                TextView textView = (TextView) option.findViewById(R.id.movie_review_text_view);
                TextView byTextView = (TextView) option.findViewById(R.id.movie_review_by_text_view);
                textView.setText(review.getContent());
                byTextView.setText(review.getAuthor());
                movieReviewLinerLayout.addView(option);
            }
            mReviews = null;
        }
    }

    public void getFavoriteReviews(final long movieId) {
        if (movieId > 0) {
            new AsyncTask<Void, Void, ArrayList<Review>>() {
                @Override
                protected ArrayList<Review> doInBackground(Void... params) {
                    ArrayList<Review> reviews = getFevoriteReviewList(movieId);
                    if (reviews != null) {
                        return reviews;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<Review> reviews) {
                    prepareReview(reviews);
                }
            }.execute();
        }
    }


    private void openYoutube(String key) {
        String videoUrl = Constant.YOUTUBE_URL + key;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)));
    }

    public ArrayList<Trailer> getFevoritesTrailorList(long movieId) {

        ArrayList<Trailer> trailers = new ArrayList<Trailer>();
        Uri uri = TrailerContract.TrailerColumns.CONTENT_URI;
        String[] mProjection = {TrailerContract.TrailerColumns.COLUMN_NAME_MOVIE_ID,
                TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_KEY,
                TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_NAME
        };
        String mSelectionClause = "(" + TrailerContract.TrailerColumns.COLUMN_NAME_MOVIE_ID + "=?)";
        String[] mSelectionArgs = {String.valueOf(movieId)};
        Cursor c = mContext.getContentResolver().query(uri, mProjection, mSelectionClause, mSelectionArgs, TrailerContract.TrailerColumns._ID);

        if (c.moveToFirst()) {
            do {
                Trailer trailer = new Trailer();
                long id = c.getLong(c.getColumnIndex(TrailerContract.TrailerColumns.COLUMN_NAME_MOVIE_ID));
                String key = c.getString(c.getColumnIndex(TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_KEY));
                String name = c.getString(c.getColumnIndex(TrailerContract.TrailerColumns.COLUMN_NAME_TRAILER_NAME));

                trailer.setKey(key);
                trailer.setName(name);
                trailers.add(trailer);
            } while (c.moveToNext());
        }
        return trailers;

    }

    public ArrayList<Review> getFevoriteReviewList(long movieId) {

        ArrayList<Review> reviews = new ArrayList<Review>();
        Uri uri = ReviewContract.ReviewColumns.CONTENT_URI;
        String[] mProjection = {ReviewContract.ReviewColumns.COLUMN_NAME_MOVIE_ID,
                ReviewContract.ReviewColumns.COLUMN_NAME_CONTENT,
                ReviewContract.ReviewColumns.COLUMN_NAME_AUTHOR
        };
        String mSelectionClause = "(" + ReviewContract.ReviewColumns.COLUMN_NAME_MOVIE_ID + "=?)";
        String[] mSelectionArgs = {String.valueOf(movieId)};
        Cursor c = mContext.getContentResolver().query(uri, mProjection, mSelectionClause, mSelectionArgs, ReviewContract.ReviewColumns._ID);

        if (c.moveToFirst()) {
            do {
                Review review = new Review();
                long id = c.getLong(c.getColumnIndex(ReviewContract.ReviewColumns.COLUMN_NAME_MOVIE_ID));
                String content = c.getString(c.getColumnIndex(ReviewContract.ReviewColumns.COLUMN_NAME_CONTENT));
                String author = c.getString(c.getColumnIndex(ReviewContract.ReviewColumns.COLUMN_NAME_AUTHOR));

                review.setContent(content);
                review.setAuthor(author);
                reviews.add(review);
            } while (c.moveToNext());
        }
        return reviews;

    }

    private String createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        return mCurrentPhotoPath;
    }

    private String writeToFile() {
        FileOutputStream out = null;
        try {
            String fileName = createImageFile();
            out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
