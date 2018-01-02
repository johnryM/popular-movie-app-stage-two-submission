package com.mangonon.johnry.popularmovieapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.mangonon.johnry.popularmovieapp.db.MoviesDatabaseContract.*;

public class FavouriteMovieContentProvider extends ContentProvider {

	public static final int FAVOURITES = 100;
	public static final int FAVOURITE_WITH_ID = 101;

	private static final UriMatcher sUriMatcher = buildUriMatcher();

	MovieDatabaseHelper mMovieDbHelper;

	public static UriMatcher buildUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		//uri for all favourites
		uriMatcher.addURI(AUTHORITY,  PATH_FAVOURITES, FAVOURITES);
		// single favourite
		uriMatcher.addURI(AUTHORITY, PATH_FAVOURITES + "/#", FAVOURITE_WITH_ID);

		return uriMatcher;
	}


	@Override
	public boolean onCreate() {
		mMovieDbHelper = new MovieDatabaseHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		final SQLiteDatabase db= mMovieDbHelper.getReadableDatabase();

		int match = sUriMatcher.match(uri);
		Cursor resultCursor;
		switch (match) {
			case FAVOURITES:
				resultCursor = db.query(MovieEntry.MOVIE_TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder
				);
				break;
			case FAVOURITE_WITH_ID:
				String movieId = uri.getPathSegments().get(1);
				String mSelection = MovieEntry.COLUMN_MOVIE_ID + " = ?";
				String[] mSelectionArgs = { movieId };
				resultCursor = db.query(MovieEntry.MOVIE_TABLE_NAME,
						projection,
						mSelection,
						mSelectionArgs,
						null,
						null,
						null
				);
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return resultCursor;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

		int match = sUriMatcher.match(uri);

		Uri resultUri;
		switch(match) {
			case FAVOURITES:
				long id = db.insert(MovieEntry.MOVIE_TABLE_NAME, null, values);
				if (id > 0) {
					resultUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
				} else {
					throw new android.database.SQLException("Failed to insert row: " + uri);
				}
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return resultUri;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

		int match = sUriMatcher.match(uri);

		int resultInt;
		switch(match) {
			case FAVOURITE_WITH_ID:
				String movieId = uri.getPathSegments().get(1);
				String mWhereClause = MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
				String[] mWhereArgs = { movieId };
				resultInt = db.delete(MovieEntry.MOVIE_TABLE_NAME, mWhereClause, mWhereArgs);
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		return resultInt;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		return 0;
	}
}
