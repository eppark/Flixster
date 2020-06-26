package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MainActivity;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.WatchedActivity;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;
    public File dir;

    public MovieAdapter(Context context, List<Movie> movies, File dir) {
        this.context = context;
        this.movies = movies;
        this.dir = dir;
    }

    // Inflate a layout (item_movie) from XML and return the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Populate data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the passed-in position
        Movie movie = movies.get(position);

        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Class cannot be static
    // Implements the View.OnClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);

            // OnClickListener
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            // Change image URL depending on portrait (poster image) or landscape (backdrop image)
            String imageUrl;
            Integer placeholderImage;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                placeholderImage = R.drawable.flicks_movie_placeholder;
            } else {
                imageUrl = movie.getPosterPath();
                placeholderImage = R.drawable.flicks_backdrop_placeholder;
            }

            // Rounded corners
            Glide.with(context).load(imageUrl)
                    .placeholder(placeholderImage)
                    .transform(new RoundedCornersTransformation(40, 0)).into(ivPoster);
        }

        // When the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // Gets the item position

            // Make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);

                // Create an intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie)); // serialize the movie using Parceler

                // Show the activity
                context.startActivity(intent);
            }
        }

        // When the user long-clicks on a row, add the movie to the watched list
        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition(); // Gets the item position

            // Make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);

                movie.setWatched();

                if (context instanceof MainActivity) {
                    // Change watched setting
                    if (((MainActivity)context).getWatchlist().contains(movie.getId()) || !movie.watched) {
                        // Let the adapter know
                        ((MainActivity)context).removeWatchedActivity(movie.getId());
                        Toast.makeText(context, "Removed movie from watchlist", Toast.LENGTH_SHORT).show();
                    } else {
                        ((MainActivity)context).addWatchedMovie(movie.getId());
                        Toast.makeText(context, "Added movie to watchlist", Toast.LENGTH_SHORT).show();
                    }
                    // Save
                    if (WatchedActivity.watchedAdapter != null) {
                        WatchedActivity.watchedAdapter.notifyDataSetChanged();
                    }
                }
            }
            // Set a delay so we don't add a million of them
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 1000);
            return true;
        }
    }
}
