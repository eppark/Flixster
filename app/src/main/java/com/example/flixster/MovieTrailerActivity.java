package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set ViewBinding
        final ActivityMovieTrailerBinding binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());

        // Layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        // Find YT video key from intent
        final String videoId = getIntent().getExtras().getString(MovieDetailsActivity.KEY_MOVIE_VID);
        Log.d("MovieTrailer", "This is: " + videoId);

        // Initialize with API key
        binding.player.initialize(getString(R.string.ggl_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                Log.e("MovieTrailerActivity", "Success initializing Youtube player");
                // Cue the video so the user can play on their own
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}