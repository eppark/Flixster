package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public final String CONFIG_URL = "https://api.themoviedb.org/3/configuration?api_key=";
    public static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String TAG = "MainActivity"; // easily log data
    public static String POSTER_SIZE = "original";
    public static String BACKDROP_SIZE = "original";
    public static String BASE_URL = "https://image.tmdb.org/t/p/";

    // For the Intent
    public static final String KEY_WATCHED_MOVIES = "watched_movies";
    public static final int WATCHED_TEXT_CODE = 20;

    List<Movie> movies;
    Set<Integer> watchlist;
    List<Movie> watchedMovies;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onWatchedAction(MenuItem mi) {
        if (watchedMovies.size() == 0) {
            Toast.makeText(getApplicationContext(), "No watched movies", Toast.LENGTH_SHORT).show();
        } else {
            // Create an intent
            Intent intent = new Intent(MainActivity.this, WatchedActivity.class);
            intent.putExtra(KEY_WATCHED_MOVIES, Parcels.wrap(watchedMovies));

            // Display the movie trailer activity
            startActivityForResult(intent, WATCHED_TEXT_CODE);
        }
    }

    public Set<Integer> getWatchlist() {
        return watchlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Get rid of the splash loading screen
        super.onCreate(savedInstanceState);

        // Set ViewBinding
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        movies = new ArrayList<>();
        watchlist = new HashSet<>();
        watchedMovies = new ArrayList<>();
        loadWatched(); // Load watched movies if we have them

        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies, getFilesDir());

        // Set the adapter on the recycler view
        binding.rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        // Find the image sizes and image URL from the MovieDB configuration file
        client.get(CONFIG_URL + getString(R.string.mvdb_api_key), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Fetch results
                    JSONObject images = jsonObject.getJSONObject("images");
                    BASE_URL = images.getString("secure_base_url");
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
        // Get all of the now playing movies
        client.get(NOW_PLAYING_URL + getString(R.string.mvdb_api_key), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Fetch results and turn them into Movies
                    JSONArray results = jsonObject.getJSONArray("results");
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

    // Add a movie to the watched list
    public void addWatchedMovie(Integer id) {
        if (!watchlist.contains(id)) {
            watchlist.add(id);
            AsyncHttpClient client = new AsyncHttpClient();
            // MovieDB returns a Json
            // Get details of the current movie
            client.get(MainActivity.MOVIE_URL + id + "?api_key=" + getString(R.string.mvdb_api_key), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    JSONObject result = json.jsonObject;
                    try {
                        // Fetch result and turn into Movie
                        Movie temp = new Movie(getString(R.string.mvdb_api_key), result);
                        temp.setWatched();
                        watchedMovies.add(temp);
                    } catch (JSONException e) {
                        Log.e(TAG, "Hit json exception", e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, "onFailure");
                }
            });
            saveWatched();
            Log.d("add", "wc" + watchlist);
            Log.d("add", "id " + id);
        }
    }

    // Remove a movie from the watched list
    public void removeWatchedActivity(Integer id) {
        int position = getIndex(watchlist, id);
        Log.d("del", "wc" + watchlist);
        Log.d("del", "pos" + position + "id "+ id);
        watchlist.remove(id);
        watchedMovies.remove(position);
        saveWatched();
    }

    // Get index from a Set
    private int getIndex(Set<Integer> set, Integer value) {
        int result = 0;
        for (Integer entry:set) {
            if (entry.equals(value)) return result;
            result++;
        }
        return -1;
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function converts an ArrayList of Strings into an ArrayList of Integers
    private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
            }
        }
        return result;
    }

    // This function will load items by reading every line of the data file
    private void loadWatched() {
        try {
            ArrayList<Integer> temp = getIntegerArray(new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset())));
            for (Integer id : temp) {
                addWatchedMovie(id);
            }
            Log.e("MainActivity", "Successfully read items");
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            watchlist = new HashSet<>();
        }
    }

    // This function saves items by writing to the file
    private void saveWatched() {
        try {
            FileUtils.writeLines(getDataFile(), watchlist);
            Log.e("MainActivity", "Successfully wrote items");
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}