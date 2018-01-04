package com.mangonon.johnry.popularmovieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mangonon.johnry.popularmovieapp.app.model.Review;
import com.mangonon.johnry.popularmovieapp.ui.ReviewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {

	private static String EXTRA_MOVIE_NAME = "extra_name";
	private static String EXTRA_REVIEWS = "reviews";

	@BindView(R.id.review_list) RecyclerView mRecyclerView;
	@BindView(R.id.error_text_view) TextView mErrorTextView;
	@BindView(R.id.my_toolbar) Toolbar mToolbar;

	private String mMovieName;
	private ArrayList mReviewList;

	public static Intent newInstance(Context context, String movieName, ArrayList<Review> list) {
		Intent intent = new Intent(context, ReviewsActivity.class);
		intent.putExtra(EXTRA_MOVIE_NAME, movieName);
		intent.putExtra(EXTRA_REVIEWS, list);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMovieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
		mReviewList = getIntent().getParcelableArrayListExtra(EXTRA_REVIEWS);
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

		mRecyclerView.setAdapter(new ReviewAdapter(mReviewList));
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return super.onSupportNavigateUp();
	}
}
