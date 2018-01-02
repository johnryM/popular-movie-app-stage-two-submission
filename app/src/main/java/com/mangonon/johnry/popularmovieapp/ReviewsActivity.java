package com.mangonon.johnry.popularmovieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mangonon.johnry.popularmovieapp.ui.ReviewAdapter;
import com.mangonon.johnry.popularmovieapp.utils.ConnectionTask;
import com.mangonon.johnry.popularmovieapp.utils.JsonUtils;
import com.mangonon.johnry.popularmovieapp.utils.NetworkUtils;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity implements ConnectionTask.ConnectionTaskCallback {

	private static String EXTRA_MOVIE_NAME = "extra_name";
	private static String EXTRA_MOVIE_ID = "extra_id";

	@BindView(R.id.review_list) RecyclerView mRecyclerView;
	@BindView(R.id.error_text_view) TextView mErrorTextView;
	@BindView(R.id.my_toolbar) Toolbar mToolbar;

	private String mMovieName;
	private String mMovieID;
	private ArrayList mReviewList;

	public static Intent newInstance(Context context, String movieName, String movieId) {
		Intent intent = new Intent(context, ReviewsActivity.class);
		intent.putExtra(EXTRA_MOVIE_NAME, movieName);
		intent.putExtra(EXTRA_MOVIE_ID, movieId);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMovieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
		mMovieID = getIntent().getStringExtra(EXTRA_MOVIE_ID);
		setContentView(R.layout.activity_reviews);
		ButterKnife.bind(this);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(getResources().getString(R.string.review_prefix) + " " + mMovieName) ;

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setHasFixedSize(true);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
		dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.item_decoration_divider));
		mRecyclerView.addItemDecoration(dividerItemDecoration);

		if (NetworkUtils.isOnline(this)) {
			new ConnectionTask(this).execute(NetworkUtils.buildAdditionalInfoMovieUrl(mMovieID, NetworkUtils.InfoType.REVIEWS));
		} else {
			showErrorView();
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return super.onSupportNavigateUp();
	}

	private void showErrorView() {
		mErrorTextView.setVisibility(View.VISIBLE);
		mRecyclerView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onTaskDone(int requestCode, String output) {
		try {
			mReviewList = JsonUtils.getReviewItems(ReviewsActivity.this, output);
			mRecyclerView.setAdapter(new ReviewAdapter(mReviewList));
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(ReviewsActivity.this, getResources().getString(R.string.review_data_error), Toast.LENGTH_SHORT).show();
		}
	}
}
