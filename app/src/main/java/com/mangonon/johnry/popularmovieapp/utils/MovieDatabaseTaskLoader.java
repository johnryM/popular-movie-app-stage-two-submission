package com.mangonon.johnry.popularmovieapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.mangonon.johnry.popularmovieapp.db.MoviesDatabaseContract;

public class MovieDatabaseTaskLoader extends AsyncTaskLoader<Cursor> {

	private Cursor mCursor = null;
	private boolean mHasResult = false;
	private String mId = "";

	public MovieDatabaseTaskLoader(Context context, String id) {
		super(context);
		mId = id;
	}

	@Override
	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		} else {
			forceLoad();
		}
	}

	@Override
	public Cursor loadInBackground() {
		Uri uri;
		if (mId != null) {
			uri = MoviesDatabaseContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(mId).build();
		} else {
			uri = MoviesDatabaseContract.MovieEntry.CONTENT_URI;
		}

		return getContext().getContentResolver().query(
			uri,
			null,
			null,
			null,
			MoviesDatabaseContract.MovieEntry.COLUMN_MOVIE_NAME
		);
	}

	@Override
	public void deliverResult(Cursor data) {
		mCursor = data;
		mHasResult = true;
		super.deliverResult(data);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (mHasResult) {
			releaseResources();
		}
		mHasResult = false;
	}

	protected void releaseResources() {
		mCursor.close();
		mCursor = null;
	}
}
