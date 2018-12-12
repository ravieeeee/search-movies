package com.example.searchmovies.View.Util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.searchmovies.Model.Movie;
import com.example.searchmovies.Global.GlobalApplication;
import com.example.searchmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ArrayList<Movie> movies;
    private final String TAG = MovieRecyclerViewAdapter.class.getName();

    public MovieRecyclerViewAdapter(Context context, ArrayList<Movie> movies) {
        this.mInflater = LayoutInflater.from(context);
        this.movies = movies;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public Uri getLinkOfMovie(int position) {
        return movies.get(position).getLink();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rv_movies, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_movie_image;
        private TextView tv_movie_title, tv_movie_pubDate, tv_movie_director, tv_movie_actor;
        private RatingBar rb_movie_userRating;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_movie_image = itemView.findViewById(R.id.iv_movie_image);
            tv_movie_title = itemView.findViewById(R.id.tv_movie_title);
            tv_movie_pubDate = itemView.findViewById(R.id.tv_movie_pubDate);
            tv_movie_director = itemView.findViewById(R.id.tv_movie_director);
            tv_movie_actor = itemView.findViewById(R.id.tv_movie_actor);
            rb_movie_userRating = itemView.findViewById(R.id.rb_movie_userRating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Picasso.with(GlobalApplication.context).load(movie.getImage()).into(holder.iv_movie_image);
        holder.tv_movie_title.setText(movie.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_movie_title.setText(Html.fromHtml(movie.getTitle(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_movie_title.setText(Html.fromHtml(movie.getTitle()));
        }
        holder.tv_movie_pubDate.setText(movie.getPubDate());
        holder.tv_movie_director.setText(movie.getDirector());
        holder.tv_movie_actor.setText(movie.getActor());
        holder.rb_movie_userRating.setRating(Float.parseFloat(movie.getUserRating()) / 2);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
