package com.gt.popularmovies.com.gt.popularmovies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.gt.popularmovies.MainActivity;
import com.gt.popularmovies.R;
import com.gt.popularmovies.com.gt.popularmovies.com.gt.populatmovies.adapter.MovieAdapter;
import com.gt.popularmovies.com.gt.popularmovies.model.Movie;
import com.gt.popularmovies.com.gt.popularmovies.model.MovieDB;
import com.gt.popularmovies.com.gt.popularmovies.provider.MovieContract;
import com.gt.popularmovies.com.gt.popularmovies.util.Constant;
import com.gt.popularmovies.com.gt.popularmovies.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by geetthaker on 8/24/15.
 */
public class GridFragment extends Fragment {

    private static final String MOVIES_LIST = "MOVIES_LIST";
    private static final String FAV = "FAV";

    private MovieAdapter movieAdapter;
    private Context mContext;
    private ArrayList<Movie> mMovies;
    private boolean fav, mHasFrg;
    private GridView gridview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment, container, false);
        gridview = (GridView) view.findViewById(R.id.gridview);

        mContext = this.getActivity();

        final MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_fragment);

        movieAdapter = new MovieAdapter(mContext, new ArrayList<Movie>());

        gridview.setAdapter(movieAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.MOVIE_ORIGINAL_TITLE_EXTRA, mMovies.get(position).getOriginalTitle());
                map.put(Constant.MOVIE_OVERVIEW_EXTRA, mMovies.get(position).getOverview());
                map.put(Constant.MOVIE_VOTE_AVERAGE_EXTRA, mMovies.get(position).getVoteAverage());
                map.put(Constant.MOVIE_RELEASE_DATE_EXTRA, mMovies.get(position).getReleaseDate());
                map.put(Constant.MOVIE_POSTER_IMAGE_EXTRA, mMovies.get(position).getPosterPath());
                map.put(Constant.MOVIE_ID_EXTRA, mMovies.get(position).getId());
                map.put(Constant.MOVIE_FAV, fav);

                ((MainActivity)getActivity()).update(map);

            }
        });

        if (savedInstanceState != null && (savedInstanceState.containsKey(FAV) && savedInstanceState.getBoolean(FAV))) {
            getFevoritesMovie();
        } else if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_LIST)) {
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
            movieAdapter.upDateEntries(mMovies, false);
        } else {
            getMovies(Constant.SORT_BY_POPULARITY);
        }

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("MOVIES_LIST", mMovies);
        savedInstanceState.putBoolean("FAV", fav);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.movie_sort_by)
                    .setItems(R.array.sort_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    fav = false;
                                    getMovies(Constant.SORT_BY_POPULARITY);
                                    break;
                                case 1:
                                    fav = false;
                                    getMovies(Constant.SORT_BY_HIGHEST_RATED);
                                    break;
                                case 2:
                                    fav = true;
                                    getFevoritesMovie();
                                    break;
                                default:
                                    getMovies(Constant.SORT_BY_POPULARITY);
                                    break;
                            }
                        }
                    });
            builder.create();
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMovies(final String sortBy) {
        new AsyncTask<Void, Void, List<Movie>>() {

            @Override
            protected List<Movie> doInBackground(Void... params) {
                try {
                    HashMap<String, String> urlParams = new HashMap<String, String>();
                    urlParams.put(Constant.SORT_BY_PARAM, sortBy);
                    urlParams.put(Constant.API_KEY_PARAMS, Constant.MOVIE_KEY);

                    String result = Util.makeHttpGetCall(Constant.BASE_URL + "?" + Util.getPostDataString(urlParams));
                    MovieDB movieDB = null;
                    if (result != null && !result.isEmpty()) {
                        movieDB = new Gson().fromJson(result, MovieDB.class);
                    }
                    if (movieDB != null) {
                        return movieDB.getResults();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                super.onPostExecute(movies);
                mMovies = (ArrayList<Movie>) movies;
                movieAdapter.upDateEntries(movies, false);
            }
        }.execute();
    }

    public void getFevoritesMovie() {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... params) {
                ArrayList<Movie> movies = getFevoritesMovieList();
                if (movies != null && movies.size() > 0) {
                    return movies;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                if (movies != null) {
                    mMovies = (ArrayList<Movie>) movies;
                    movieAdapter.upDateEntries(movies, true);
                }
            }
        }.execute();
    }

    public ArrayList<Movie> getFevoritesMovieList() {

        ArrayList<Movie> movies = new ArrayList<Movie>();
        Uri uri = MovieContract.MovieColumns.CONTENT_URI;
        String[] mProjection = {MovieContract.MovieColumns.COLUMN_NAME_MOVIE_ID,
                MovieContract.MovieColumns.COLUMN_NAME_ORIGINAL_TITLE,
                MovieContract.MovieColumns.COLUMN_NAME_OVERVIEW,
                MovieContract.MovieColumns.COLUMN_NAME_POSTER_PATH,
                MovieContract.MovieColumns.COLUMN_NAME_VOTE_AVERAGE,
                MovieContract.MovieColumns.COLUMN_NAME_RELEASE_DATE
        };
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        Cursor c = getActivity().getContentResolver().query(uri, mProjection, mSelectionClause, mSelectionArgs, MovieContract.MovieColumns._ID);

        if (c.moveToFirst()) {
            do {
                Movie movie = new Movie();
                long id = c.getLong(c.getColumnIndex(MovieContract.MovieColumns.COLUMN_NAME_MOVIE_ID));
                String originalTitle = c.getString(c.getColumnIndex(MovieContract.MovieColumns.COLUMN_NAME_ORIGINAL_TITLE));
                String overview = c.getString(c.getColumnIndex(MovieContract.MovieColumns.COLUMN_NAME_OVERVIEW));
                String posterPath = c.getString(c.getColumnIndex(MovieContract.MovieColumns.COLUMN_NAME_POSTER_PATH));
                String voteAverage = c.getString(c.getColumnIndex(MovieContract.MovieColumns.COLUMN_NAME_VOTE_AVERAGE));
                String releaseDate = c.getString(c.getColumnIndex(MovieContract.MovieColumns.COLUMN_NAME_RELEASE_DATE));

                movie.setId(id);
                movie.setOriginalTitle(originalTitle);
                movie.setOverview(overview);
                movie.setPosterPath(posterPath);
                if (voteAverage != null) {
                    movie.setVoteAverage(Double.parseDouble(voteAverage));
                }
                movie.setReleaseDate(releaseDate);
                movies.add(movie);
            } while (c.moveToNext());
        }
        return movies;

    }
}
