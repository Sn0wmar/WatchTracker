package com.example.watch_tracker;

import com.google.gson.annotations.SerializedName;
import java.util.List;

//D2claration de la classe MovieResponse
public class MovieResponse {
    @SerializedName("results")
    private List<Movie> results;


//Méthode getter pour récupérer la liste de films
    public List<Movie> getResults() {
        return results;
    }

    //Méthode setter pour défiir la liste de films
    public void setResults(List<Movie> results) {
        this.results = results;
    }
}