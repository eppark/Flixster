package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set ViewBinding
        final ActivityMovieTrailerBinding binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        // temporary test video id
        final String videoId = getIntent().getExtras().getString(MovieDetailsActivity.KEY_MOVIE_VID);
        Log.d("MovieTrailer", "This is: " + videoId);

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key
        playerView.initialize(getString(R.string.ggl_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                Log.e("MovieTrailerActivity", "Success initializing Youtube player");
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}