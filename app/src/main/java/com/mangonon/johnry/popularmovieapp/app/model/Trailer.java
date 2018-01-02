package com.mangonon.johnry.popularmovieapp.app.model;

public class Trailer {
	private String mId;
	private String mKey;
	private String mTitle;
	private String mSite;
	private String mType;

	public Trailer(String mId, String mKey, String mTitle, String mSite, String mType) {
		this.mId = mId;
		this.mKey = mKey;
		this.mTitle = mTitle;
		this.mSite = mSite;
		this.mType = mType;
	}

	public String getId() {
		return mId;
	}

	public String getKey() {
		return mKey;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getSite() {
		return mSite;
	}

	public String getType() {
		return mType;
	}

	public String toString() {
		return "Trailer info: " +
				mId + " " +
				mKey + " " +
				mTitle + " " +
				mSite + " " +
				mType;
	}

}
