package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public final String CONFIG_URL = "https://api.themoviedb.org/3/configuration?api_key=";
    public static final String TAG = "MainActivity"; // easily log data
    public static String POSTER_SIZE = "original";
    public static String BACKDROP_SIZE = "original";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set ViewBinding
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        movies = new ArrayList<>();

        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        binding.rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        // Find the image sizes
        client.get(CONFIG_URL + getString(R.string.mvdb_api_key), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Config onSuccess");

                JSONObject jsonObject = json.jsonObject;
                try {
                    // Fetch results
                    JSONObject images = jsonObject.getJSONObject("images");
                    Log.i(TAG, "Images: " + images.toString());
                    POSTER_SIZE = images.getJSONArray("poster_sizes").getString(3);
                    BACKDROP_SIZE = images.getJSONArray("backdrop_sizes").getString(2);

                    Log.i(TAG, "Images: " + POSTER_SIZE + " and " + BACKDROP_SIZE);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // MovieDB returns a Json
        client.get(NOW_PLAYING_URL + getString(R.string.mvdb_api_key), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                JSONObject jsonObject = json.jsonObject;
                try {
                    // Fetch results and turn them into Movies
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJSONArray(getString(R.string.mvdb_api_key), results));

                    // Let the adapter know to rerender the recycler view
                    movieAdapter.notifyDataSetChanged();

                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}