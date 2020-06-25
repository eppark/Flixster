package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

// Encapsulate a movie
@Parcel // annotation indicates class is Parcelable
public class Movie {

    // Fields must be public for Parceler
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    String releaseDate;
    Integer id;
    String ytVideoUrl;
    public String ytId;

    // no-arg, empty constructor required for Parceler
    public Movie() {
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        releaseDate = jsonObject.getString("release_date");
        id = jsonObject.getInt("id");
        ytVideoUrl = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s", id, "538d34da62949e40e163c04fdc23906f");
    }

    // Iterate through the JSON array and construct a movie for each one in the array
    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public Integer getId() {
        return id;
    }

    public String getYtVideoUrl() {
        return ytVideoUrl;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
