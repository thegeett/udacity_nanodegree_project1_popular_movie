package com.gt.popularmovies.com.gt.popularmovies.com.gt.populatmovies.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gt.popularmovies.com.gt.popularmovies.model.Movie;
import com.gt.popularmovies.com.gt.popularmovies.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by geetthaker on 7/10/15.
 */
public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mMovies;

    public MovieAdapter(Context c, List<Movie> movies) {
        mContext = c;
        mMovies = movies;
    }

    public int getCount() {
        return mMovies.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        String baseUrl = Constant.IMAGE_BASE_URL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            baseUrl += Constant.IMAGE_WIDTH_L;
        } else {
            baseUrl += Constant.IMAGE_WIDTH;
        }

        Picasso.with(mContext).load(baseUrl + mMovies.get(position).getPosterPath()).into(imageView);
        return imageView;
    }

    public void upDateEntries(List<Movie> movies) {
        if (movies != null) {
            this.mMovies.clear();
            this.mMovies.addAll(movies);
            notifyDataSetChanged();
        }
    }

    public List<Movie> getmMovies() {
        return mMovies;
    }

    public void setmMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }
}