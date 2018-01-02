package com.mangonon.johnry.popularmovieapp.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String mImageUrl;
    private String mSynopsis;
    private float mRating;
    private String mReleaseDate;
    private String mTitle;
    private String mId;

    public Movie(String mImageUrl, String mSynopsis, float mRating, String mReleaseDate, String mTitle, String mId) {
        this.mImageUrl = mImageUrl;
        this.mSynopsis = mSynopsis;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
        this.mTitle = mTitle;
        this.mId = mId;
    }

    protected Movie(Parcel in) {
        mImageUrl = in.readString();
        mSynopsis = in.readString();
        mRating = in.readFloat();
        mReleaseDate = in.readString();
        mTitle = in.readString();
        mId = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public float getmRating() {
        return mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmId() { return mId; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageUrl);
        dest.writeString(mSynopsis);
        dest.writeFloat(mRating);
        dest.writeString(mReleaseDate);
        dest.writeString(mTitle);
        dest.writeString(mId);
    }
}
