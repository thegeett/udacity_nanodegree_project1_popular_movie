package com.gt.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.popularmovies.com.gt.popularmovies.util.Constant;
import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {

    private String originalTitle;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String posterPath;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        Intent intent = getIntent();

        TextView movieNameTextView = (TextView) findViewById(R.id.movie_name_text_view);
        ImageView posterImage = (ImageView) findViewById(R.id.movie_poster_image);
        TextView movieRattingTextView = (TextView) findViewById(R.id.movie_ratting);
        TextView movieReleaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
        TextView movieOverviewTextView = (TextView) findViewById(R.id.movie_overview);


        if (intent != null) {
            movieNameTextView.setText(intent.getStringExtra(Constant.MOVIE_ORIGINAL_TITLE_EXTRA));
            String baseUrl = Constant.IMAGE_BASE_URL;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                baseUrl += Constant.IMAGE_POSTER_WIDTH_L;
            } else {
                baseUrl += Constant.IMAGE_POSTER_WIDTH;
            }
            Picasso.with(mContext).load(baseUrl + intent.getStringExtra(Constant.MOVIE_POSTER_IMAGE_EXTRA)).into(posterImage);
            movieRattingTextView.setText(Constant.MOVIE_RATTING_TEXT + "" + Double.toString(intent.getDoubleExtra(Constant.MOVIE_VOTE_AVERAGE_EXTRA, 0.0)));
            movieReleaseDateTextView.setText(Constant.MOVIE_RELEASE_DATE_TEXT + "" + intent.getStringExtra(Constant.MOVIE_RELEASE_DATE_EXTRA));
            movieOverviewTextView.setText(intent.getStringExtra(Constant.MOVIE_OVERVIEW_EXTRA));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
