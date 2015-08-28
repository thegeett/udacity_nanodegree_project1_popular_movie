package com.gt.popularmovies.com.gt.popularmovies.model;

import java.util.List;

/**
 * Created by geetthaker on 8/23/15.
 */
public class MovieTrailer {

    private int id;
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
