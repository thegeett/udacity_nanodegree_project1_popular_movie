package com.gt.popularmovies.com.gt.popularmovies.model;

import java.util.List;

/**
 * Created by geetthaker on 7/10/15.
 */
public class MovieDB {

    private int page;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
