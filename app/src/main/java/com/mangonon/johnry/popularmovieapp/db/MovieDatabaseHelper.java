package com.mangonon.johnry.popularmovieapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mangonon.johnry.popularmovieapp.db.MoviesDatabaseContract.*;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "movie.db";
	public static final int DATABASE_VERSION = 1;

	public MovieDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
				MovieEntry.MOVIE_TABLE_NAME + " (" +
				MovieEntry.COLUMN_MOVIE_ID + " TEXT PRIMARY KEY NOT NULL UNIQUE," +
				MovieEntry.COLUMN_MOVIE_NAME + " TEXT," +
				MovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT," +
				MovieEntry.COLUMN_MOVIE_IMAGE_URL + " TEXT," +
				MovieEntry.COLUMN_MOVIE_RATING + " TEXT," +
				MovieEntry.COLUMN_MOVIE_RELEASE + " REAL" +
				");";

		db.execSQL(SQL_CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.MOVIE_TABLE_NAME);
		onCreate(db);
	}

}
