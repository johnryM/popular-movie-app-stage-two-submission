package com.mangonon.johnry.popularmovieapp.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable{
	private String mAuthor;
	private String mReview;

	public Review(String author, String review) {
		mAuthor = author;
		mReview = review;
	}

	protected Review(Parcel in) {
		mAuthor = in.readString();
		mReview = in.readString();
	}

	public static final Creator<Review> CREATOR = new Creator<Review>() {
		@Override
		public Review createFromParcel(Parcel in) {
			return new Review(in);
		}

		@Override
		public Review[] newArray(int size) {
			return new Review[size];
		}
	};

	public String getAuthor() {
		return mAuthor;
	}

	public String getReview() {
		return mReview;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAuthor);
		dest.writeString(mReview);
	}
}
