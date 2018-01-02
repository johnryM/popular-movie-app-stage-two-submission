package com.mangonon.johnry.popularmovieapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mangonon.johnry.popularmovieapp.app.model.Movie;
import com.mangonon.johnry.popularmovieapp.app.model.MovieBuilder;

import java.util.ArrayList;

import static com.mangonon.johnry.popularmovieapp.db.MoviesDatabaseContract.*;

public class MovieDatabaseManager {

	private static MovieDatabaseManager instance = null;

	private MovieDatabaseManager() {}

	public static synchronized MovieDatabaseManager getInstance() {
		if (instance == null) {
			instance = new MovieDatabaseManager();
		}
		return instance;
	}

	public boolean addMovieToFavourites(Context context, Movie movie) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getmId());
		contentValues.put(MovieEntry.COLUMN_MOVIE_NAME, movie.getmTitle());
		contentValues.put(MovieEntry.COLUMN_MOVIE_SYNOPSIS, movie.getmSynopsis());
		contentValues.put(MovieEntry.COLUMN_MOVIE_IMAGE_URL, movie.getmImageUrl());
		contentValues.put(MovieEntry.COLUMN_MOVIE_RATING, movie.getmRating());
		contentValues.put(MovieEntry.COLUMN_MOVIE_RELEASE, movie.getmReleaseDate());

		boolean result;
		try {
			result = (context.getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues) != null);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public ArrayList<Movie> getAllFavourites(Cursor cursor) {
		ArrayList<Movie> movieList = new ArrayList<>();
		if (cursor != null && cursor.getCount() != 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				movieList.add(new MovieBuilder()
						.setImageUrl(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_IMAGE_URL)))
						.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_SYNOPSIS)))
						.setRating(cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RATING)))
						.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE)))
						.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME)))
						.setId(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)))
						.createMovie());
			}
		}
		return movieList;
	}

	public boolean isMovieFavourited(Cursor cursor) {
		if (cursor != null && cursor.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}

	public int removeMovieFromFavourites(Context context, String id) {
		return context.getContentResolver().delete(
				MovieEntry.CONTENT_URI.buildUpon().appendPath(id).build(),
				null,
				null
		);
	}
}
