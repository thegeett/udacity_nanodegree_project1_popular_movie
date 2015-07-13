package com.gt.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.gt.popularmovies.com.gt.popularmovies.com.gt.populatmovies.adapter.MovieAdapter;
import com.gt.popularmovies.com.gt.popularmovies.model.Movie;
import com.gt.popularmovies.com.gt.popularmovies.model.MovieDB;
import com.gt.popularmovies.com.gt.popularmovies.util.Constant;
import com.gt.popularmovies.com.gt.popularmovies.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;
    private Context mContext;
    List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(movieAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMovies != null) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(Constant.MOVIE_ORIGINAL_TITLE_EXTRA, mMovies.get(position).getOriginalTitle());
                    intent.putExtra(Constant.MOVIE_OVERVIEW_EXTRA, mMovies.get(position).getOverview());
                    intent.putExtra(Constant.MOVIE_VOTE_AVERAGE_EXTRA, mMovies.get(position).getVoteAverage());
                    intent.putExtra(Constant.MOVIE_RELEASE_DATE_EXTRA, mMovies.get(position).getReleaseDate());
                    intent.putExtra(Constant.MOVIE_POSTER_IMAGE_EXTRA, mMovies.get(position).getPosterPath());
                    startActivity(intent);
                }
            }
        });

        getMovies(Constant.SORT_BY_POPULARITY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.movie_sort_by)
                    .setItems(R.array.sort_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    getMovies(Constant.SORT_BY_POPULARITY);
                                    break;
                                case 1:
                                    getMovies(Constant.SORT_BY_HIGHEST_RATED);
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
                mMovies = movies;
                movieAdapter.upDateEntries(movies);
            }
        }.execute();
    }

}
