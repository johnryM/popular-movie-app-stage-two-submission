package com.mangonon.johnry.popularmovieapp.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
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

	protected Trailer(Parcel in) {
		mId = in.readString();
		mKey = in.readString();
		mTitle = in.readString();
		mSite = in.readString();
		mType = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mKey);
		dest.writeString(mTitle);
		dest.writeString(mSite);
		dest.writeString(mType);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
		@Override
		public Trailer createFromParcel(Parcel in) {
			return new Trailer(in);
		}

		@Override
		public Trailer[] newArray(int size) {
			return new Trailer[size];
		}
	};

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
