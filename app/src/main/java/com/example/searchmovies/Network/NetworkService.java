package com.example.searchmovies.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("search/movie.json")
    Call<JsonObject> getMovies(@Query("query") String query);
}
