package com.mangonon.johnry.popularmovieapp.app.model;

public class Review {

	private String mAuthor;
	private String mReview;

	public Review(String author, String review) {
		mAuthor = author;
		mReview = review;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public String getReview() {
		return mReview;
	}

}
