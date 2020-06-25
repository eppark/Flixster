package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String KEY_MOVIE_VID = "movie_vid_url";
    public static final int TRAILER_TEXT_CODE = 20;

    Movie movie;

    // View objects
    ImageView ivBackdropImage;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView tvReleaseDate;
    RatingBar rbPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivBackdropImage = (ImageView) findViewById(R.id.ivBackdropImage);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        rbPopularity = (RatingBar) findViewById(R.id.rbPopularity);

        // Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // Set the image
        Glide.with(this).load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .transform(new RoundedCornersTransformation(40, 0)).into(ivBackdropImage);

        // Set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // Popularity is from 1 to 100, so divide by 20
        float popularity = movie.getPopularity().floatValue();
        rbPopularity.setRating(popularity = popularity > 0 ? popularity / 20.0f : popularity);

        // Vote average is from 1 to 10, so divide by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // Set the release date
        tvReleaseDate.setText(String.format("Release date: %s", movie.getReleaseDate()));

        AsyncHttpClient client = new AsyncHttpClient();
        // MovieDB returns a Json
        client.get(movie.getYtVideoUrl(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("Movie", "onSuccess");

                JSONObject jsonObject = json.jsonObject;
                try {
                    // Fetch results and turn them into Movies
                    JSONArray results = jsonObject.getJSONArray("results");
                    if (results.length() > 0) // if we have a video
                    {
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject details = results.getJSONObject(i);
                            // Ensure we get a YT video
                            if (details.getString("site").equals("YouTube")) {
                                movie.ytKey = details.getString("key");
                                break;
                            }
                        }
                        Log.d("Movie", "Successfully grabbed video " + movie.ytKey + " for " + movie.getId());
                    } else {
                        movie.ytKey = null;
                    }
                } catch (JSONException e) {
                    Log.e("Movie", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Movie", "onFailure");
            }
        });

        ivBackdropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If we do have a video we can use
                if (movie.ytKey != null) {
                    Log.d("MovieDetailActivity", "Success onClickListener");
                    // Create an intent
                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra(KEY_MOVIE_VID, movie.ytKey);

                    // Display the movie trailer activity
                    startActivityForResult(intent, TRAILER_TEXT_CODE);
                } else {
                    // Let the user know there are no related videos to the movie
                    Toast.makeText(getApplicationContext(), "No videos available for this movie", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}