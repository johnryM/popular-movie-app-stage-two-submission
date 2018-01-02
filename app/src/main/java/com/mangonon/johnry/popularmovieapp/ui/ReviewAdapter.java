package com.mangonon.johnry.popularmovieapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mangonon.johnry.popularmovieapp.R;
import com.mangonon.johnry.popularmovieapp.app.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

	private ArrayList<Review> mReviewArrayList;

	public ReviewAdapter(ArrayList<Review> reviewList) {
		mReviewArrayList = reviewList;
	}

	@Override
	public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.review_item, parent, false);
		return new ReviewHolder(view);
	}

	@Override
	public void onBindViewHolder(ReviewHolder reviewHolder, int position) {
		reviewHolder.bind(mReviewArrayList.get(position));
	}

	@Override
	public int getItemCount() {
		return mReviewArrayList.size();
	}

	public class ReviewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.author_text_view) TextView mAuthorTextView;
		@BindView(R.id.review_text_view) TextView mReviewTextView;

		public ReviewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bind(Review review) {
			mAuthorTextView.setText(review.getAuthor());
			mReviewTextView.setText(review.getReview());
		}

	}

}
