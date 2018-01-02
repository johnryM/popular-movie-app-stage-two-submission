package com.mangonon.johnry.popularmovieapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.mangonon.johnry.popularmovieapp.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public enum SortType {
        POPULAR("popular"),
        TOP_RATED("top_rated"),
        FAVOURITE("favourite");

        String title;

        SortType(String s) {
            title = s;
        }

        public String toString() {
            return title;
        }
    }

    public enum InfoType {
        REVIEWS("reviews"),
        VIDEOS("videos");

        String title;

        InfoType(String s) { title = s; }

        public String toString() {return title; }
    }

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String MOVIEMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String KEY = "";
    private static String KEY_PARAM = "api_key";

    public static URL buildMovieDataUrl(SortType sortType) {

        String sortBy = sortType.toString();

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Movie database URI " + url);

        return url;
    }

    public static URL buildAdditionalInfoMovieUrl(String id, InfoType infoType) {

        String infoString = infoType.toString();

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(infoString)
                .appendQueryParameter(KEY_PARAM, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built additional Movie database URI " + url);

        return url;
    }

    public static URL buildMovieImageUrl(String imageSize, String imageId) {
        Uri builtUri = Uri.parse(MOVIEMAGE_BASE_URL).buildUpon()
                .appendPath(imageSize)
                .appendPath(imageId)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Movie image URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
