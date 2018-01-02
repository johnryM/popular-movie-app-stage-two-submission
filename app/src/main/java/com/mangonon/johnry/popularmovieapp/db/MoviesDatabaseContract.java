package com.mangonon.johnry.popularmovieapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesDatabaseContract {

	public static final String AUTHORITY = "com.mangonon.johnry.popularmovieapp";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public static final String PATH_FAVOURITES = "favourites";


	private MoviesDatabaseContract() {}

	public static class MovieEntry implements BaseColumns {

		public static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

		public static final String MOVIE_TABLE_NAME = "movie";
		public static final String COLUMN_MOVIE_ID = "movie_id";
		public static final String COLUMN_MOVIE_NAME = "name";
		public static final String COLUMN_MOVIE_IMAGE_URL = "image";
		public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
		public static final String COLUMN_MOVIE_RATING = "rating";
		public static final String COLUMN_MOVIE_RELEASE = "release";
	}

}
