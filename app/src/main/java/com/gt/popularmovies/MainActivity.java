package com.gt.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gt.popularmovies.com.gt.popularmovies.fragment.GridFragment;
import com.gt.popularmovies.com.gt.popularmovies.fragment.MovieDetailFragment;
import com.gt.popularmovies.com.gt.popularmovies.util.Constant;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private MovieDetailFragment movieDetailFragment;
    private GridFragment gridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_fragment);
        gridFragment = (GridFragment) getFragmentManager().findFragmentById(R.id.movie_grid_fragment);
    }

    public void update(HashMap<String, Object> map) {

        if (movieDetailFragment != null) {
            movieDetailFragment.clearData();
            movieDetailFragment.updateScreen(map);
        } else {
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra(Constant.MOVIE_ORIGINAL_TITLE_EXTRA, (String) map.get(String.valueOf(Constant.MOVIE_ORIGINAL_TITLE_EXTRA)));
            intent.putExtra(Constant.MOVIE_OVERVIEW_EXTRA, String.valueOf(map.get(Constant.MOVIE_OVERVIEW_EXTRA)));
            intent.putExtra(Constant.MOVIE_VOTE_AVERAGE_EXTRA, Double.toString(Double.parseDouble(String.valueOf(map.get(Constant.MOVIE_VOTE_AVERAGE_EXTRA)))));
            intent.putExtra(Constant.MOVIE_RELEASE_DATE_EXTRA, String.valueOf(map.get(String.valueOf(Constant.MOVIE_RELEASE_DATE_EXTRA))));
            intent.putExtra(Constant.MOVIE_POSTER_IMAGE_EXTRA, String.valueOf(map.get(Constant.MOVIE_POSTER_IMAGE_EXTRA)));
            intent.putExtra(Constant.MOVIE_ID_EXTRA, String.valueOf(map.get(Constant.MOVIE_ID_EXTRA)));
            intent.putExtra(Constant.MOVIE_FAV, Boolean.parseBoolean(String.valueOf((map.get(Constant.MOVIE_FAV)))));

            startActivity(intent);
        }

    }
}
