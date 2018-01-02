package com.mangonon.johnry.popularmovieapp.app.model;

public class MovieBuilder {
    private String mImageUrl;
    private String mSynopsis;
    private float mRating;
    private String mReleaseDate;
    private String mTitle;
    private String mId;

    public MovieBuilder setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
        return this;
    }

    public MovieBuilder setSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
        return this;
    }

    public MovieBuilder setRating(float mRating) {
        this.mRating = mRating;
        return this;
    }

    public MovieBuilder setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
        return this;
    }

    public MovieBuilder setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public MovieBuilder setId(String id) {
        this.mId = id;
        return this;
    }

    public Movie createMovie() {
        return new Movie(mImageUrl, mSynopsis, mRating, mReleaseDate, mTitle, mId);
    }
}