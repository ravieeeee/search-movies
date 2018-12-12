package com.example.searchmovies.View;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.searchmovies.Model.Movie;
import com.example.searchmovies.Global.GlobalApplication;
import com.example.searchmovies.R;
import com.example.searchmovies.View.Util.MovieRecyclerViewAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener {
    private final String TAG = MainActivity.class.getName();
    private Call<JsonObject> getMovies;
    private ArrayList<Movie> movies;

    private EditText et_searchQuery;
    private Button btn_search;

    private RecyclerView rv_movies;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();


        rv_movies = findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv_movies.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(rv_movies.getContext(), layoutManager.getOrientation());
        rv_movies.addItemDecoration(dividerItemDecoration);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movies);
        movieRecyclerViewAdapter.setClickListener(this);
        rv_movies.setAdapter(movieRecyclerViewAdapter);

        et_searchQuery = findViewById(R.id.et_searchQuery);
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movies.clear();
                
                String query = et_searchQuery.getText().toString();
                getMovies = GlobalApplication.networkService.getMovies(query);
                getMovies.clone().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            for (JsonElement j : response.body().getAsJsonArray("items")) {
                                JsonObject movie = j.getAsJsonObject();

                                String title = movie.get("title").getAsString();
                                Uri link = Uri.parse(movie.get("link").getAsString());
                                Uri image = Uri.parse(movie.get("image").getAsString());
                                String pubDate = movie.get("pubDate").getAsString();
                                String director = movie.get("director").getAsString();
                                String actor = movie.get("actor").getAsString();
                                String userRating = movie.get("userRating").getAsString();

                                Movie newMovie = new Movie(title, link, image, pubDate,
                                        director, actor, userRating);
                                movies.add(newMovie);
                            }
                            movieRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                movieRecyclerViewAdapter.getLinkOfMovie(position));
        startActivity(browserIntent);
    }
}
