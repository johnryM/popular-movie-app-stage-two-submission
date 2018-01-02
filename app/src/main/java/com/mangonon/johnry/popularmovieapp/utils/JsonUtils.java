package com.mangonon.johnry.popularmovieapp.utils;

import android.content.Context;

import com.mangonon.johnry.popularmovieapp.app.model.Movie;
import com.mangonon.johnry.popularmovieapp.app.model.MovieBuilder;
import com.mangonon.johnry.popularmovieapp.app.model.Review;
import com.mangonon.johnry.popularmovieapp.app.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static String ARRAY_ELEMENT = "results";
    private static String IMAGE_PATH = "poster_path";
    private static String SYNOPSIS = "overview";
    private static String RATING = "vote_average";
    private static String RELEASE_DATE = "release_date";
    private static String TITLE = "original_title";
    private static String ID = "id";

    private static String REVIEW_AUTHOR = "author";
    private static String REVIEW_CONTENT = "content";

    private static String TRAILER_ID = "id";
    private static String TRAILER_KEY = "key";
    private static String TRAILER_TITLE = "name";
    private static String TRAILER_SITE = "site";
    private static String TRAILER_TYPE = "type";


    public static ArrayList<Movie> getMovieItems(Context context, String jsonString) throws JSONException {

        if (jsonString == null) {
            return null;
        }

        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject movieJsonObject = new JSONObject(jsonString);

        JSONArray movieJsonArray = movieJsonObject.getJSONArray(ARRAY_ELEMENT);

        if (movieJsonArray == null) {
            return null;
        }

        for (int i = 0; i < movieJsonArray.length(); i++) {

            JSONObject movieDetailsObject = movieJsonArray.getJSONObject(i);

            Movie movie = new MovieBuilder()
                    .setImageUrl(formatImagePath(movieDetailsObject.getString(IMAGE_PATH)))
                    .setSynopsis(movieDetailsObject.getString(SYNOPSIS))
                    .setRating(Float.parseFloat(movieDetailsObject.getString(RATING)))
                    .setReleaseDate(movieDetailsObject.getString(RELEASE_DATE))
                    .setTitle(movieDetailsObject.getString(TITLE))
                    .setId(movieDetailsObject.getString(ID))
                    .createMovie();

            movieList.add(movie);
        }

        return movieList;
    }

    public static ArrayList<Review> getReviewItems(Context context, String jsonString) throws JSONException {
        if (jsonString == null) {
            return null;
        }

        ArrayList<Review> reviewList = new ArrayList<>();
        JSONObject reviewJsonObject = new JSONObject(jsonString);

        JSONArray reviewJsonArray = reviewJsonObject.getJSONArray(ARRAY_ELEMENT);

        if (reviewJsonArray == null) {
            return null;
        }

        for (int i = 0; i < reviewJsonArray.length(); i++) {
            JSONObject reviewObject = reviewJsonArray.getJSONObject(i);

            Review review = new Review(reviewObject.getString(REVIEW_AUTHOR),
                    reviewObject.getString(REVIEW_CONTENT));

            reviewList.add(review);
        }

        return reviewList;
    }

    public static ArrayList<Trailer> getTrailerItems(Context context, String jsonString) throws JSONException {
        if (jsonString == null) {
            return null;
        }

        ArrayList<Trailer> trailerList = new ArrayList<>();
        JSONObject trailerJsonObject = new JSONObject(jsonString);

        JSONArray trailerJsonArray = trailerJsonObject.getJSONArray(ARRAY_ELEMENT);

        if (trailerJsonArray == null) {
            return null;
        }

        for (int i = 0; i < trailerJsonArray.length(); i++) {
            JSONObject trailerObject = trailerJsonArray.getJSONObject(i);

            Trailer trailer = new Trailer(
                    trailerObject.getString(TRAILER_ID),
                    trailerObject.getString(TRAILER_KEY),
                    trailerObject.getString(TRAILER_TITLE),
                    trailerObject.getString(TRAILER_SITE),
                    trailerObject.getString(TRAILER_TYPE));

            trailerList.add(trailer);
        }
        return trailerList;
    }


    private static String formatImagePath(String oldPath) {
        return oldPath.substring(1);
    }

}
