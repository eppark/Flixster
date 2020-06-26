package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.flixster.adapters.WatchedAdapter;
import com.example.flixster.databinding.ActivityWatchedBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class WatchedActivity extends AppCompatActivity {

    public static WatchedAdapter watchedAdapter = null;
    List<Movie> watchedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched);

        // Set ViewBinding
        final ActivityWatchedBinding binding = ActivityWatchedBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        // Unwrap the watched movies passed in via intent, using its simple name as a key
        watchedMovies = (List<Movie>) Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.KEY_WATCHED_MOVIES));

        // Create the adapter
        watchedAdapter = new WatchedAdapter(this, watchedMovies);

        // Set the adapter on the recycler view
        binding.rvWatchedMovies.setAdapter(watchedAdapter);

        // Set a Layout Manager on the recycler view
        binding.rvWatchedMovies.setLayoutManager(new LinearLayoutManager(this));
    }
}