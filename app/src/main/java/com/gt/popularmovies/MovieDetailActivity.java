package com.gt.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gt.popularmovies.com.gt.popularmovies.fragment.MovieDetailFragment;
import com.gt.popularmovies.com.gt.popularmovies.util.Constant;

import java.util.HashMap;


public class MovieDetailActivity extends AppCompatActivity {

    private Intent intent;
    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_fragment);
        intent = getIntent();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(Constant.MOVIE_ORIGINAL_TITLE_EXTRA, intent.getStringExtra(Constant.MOVIE_ORIGINAL_TITLE_EXTRA));
        map.put(Constant.MOVIE_OVERVIEW_EXTRA, intent.getStringExtra(Constant.MOVIE_OVERVIEW_EXTRA));
        map.put(Constant.MOVIE_VOTE_AVERAGE_EXTRA, intent.getStringExtra(Constant.MOVIE_VOTE_AVERAGE_EXTRA));
        map.put(Constant.MOVIE_RELEASE_DATE_EXTRA, intent.getStringExtra(Constant.MOVIE_RELEASE_DATE_EXTRA));
        map.put(Constant.MOVIE_POSTER_IMAGE_EXTRA, intent.getStringExtra(Constant.MOVIE_POSTER_IMAGE_EXTRA));
        map.put(Constant.MOVIE_ID_EXTRA, intent.getStringExtra(Constant.MOVIE_ID_EXTRA));
        map.put(Constant.MOVIE_FAV, intent.getBooleanExtra(Constant.MOVIE_FAV, false));

        movieDetailFragment.updateScreen(map);

    }
}
