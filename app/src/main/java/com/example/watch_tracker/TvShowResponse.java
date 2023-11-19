package com.example.watch_tracker;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TvShowResponse {
    @SerializedName("results")
    private List<TvShow> results;

    public List<TvShow> getResults() {
        return results;
    }

    public void setResults(List<TvShow> results) {
        this.results = results;
    }
}
