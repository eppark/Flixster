package com.example.flixster.models;

import com.example.flixster.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// Encapsulate a movie
@Parcel // annotation indicates class is Parcelable
public class Movie {

    // Fields must be public for Parceler
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Double popularity;
    String releaseDate;
    Integer id;
    String ytVideoUrl;
    public String ytKey;

    // no-arg, empty constructor required for Parceler
    public Movie() {
    }

    public Movie(String api_key, JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        popularity = jsonObject.getDouble("popularity");
        releaseDate = jsonObject.getString("release_date");
        id = jsonObject.getInt("id");
        ytKey = null;
        ytVideoUrl = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s", id, api_key);
    }

    // Iterate through the JSON array and construct a movie for each one in the array
    public static List<Movie> fromJSONArray(String api_key, JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(api_key, movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public Integer getId() {
        return id;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getYtVideoUrl() {
        return ytVideoUrl;
    }

    // Return a URL to the movie poster path
    public String getPosterPath() {
        return String.format("%s%s/%s", MainActivity.BASE_URL, MainActivity.POSTER_SIZE, posterPath);
    }

    // Return a URL to the movie backdrop path
    public String getBackdropPath() {
        return String.format("%s%s/%s", MainActivity.BASE_URL, MainActivity.BACKDROP_SIZE, backdropPath);
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
