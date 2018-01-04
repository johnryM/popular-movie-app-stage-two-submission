package com.mangonon.johnry.popularmovieapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mangonon.johnry.popularmovieapp.app.model.Movie;
import com.mangonon.johnry.popularmovieapp.app.model.Review;
import com.mangonon.johnry.popularmovieapp.app.model.Trailer;
import com.mangonon.johnry.popularmovieapp.db.MovieDatabaseManager;
import com.mangonon.johnry.popularmovieapp.utils.ConnectionTask;
import com.mangonon.johnry.popularmovieapp.utils.Helper;
import com.mangonon.johnry.popularmovieapp.utils.JsonUtils;
import com.mangonon.johnry.popularmovieapp.utils.MovieDatabaseTaskLoader;
import com.mangonon.johnry.popularmovieapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends AppCompatActivity implements ConnectionTask.ConnectionTaskCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_IS_FAVOURITED = "extra_is_favourited";
    public static final String EXTRA_TRAILERS = "extra_trailers";
    public static final String EXTRA_REVIEWS = "extra_reviews";

    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private static final int DETAIL_LOADER_ID = 1;

    private Movie mMovie;
    private ArrayList<Trailer> mTrailerList;
    private ArrayList<Review> mReviewList;
    private boolean mIsFavourited = false;

    @BindView(R.id.movie_image_view) ImageView mImage;
    @BindView(R.id.movie_synopsis_view) TextView mSynopsis;
    @BindView(R.id.movie_rating_view) TextView mRating;
    @BindView(R.id.movie_rdate_view) TextView mRelease;
    @BindView(R.id.trailer_container) LinearLayout mTrailerContainer;
    @BindView(R.id.my_toolbar) Toolbar mToolbar;
    @BindView(R.id.detail_scrollview) ScrollView mScrollView;
    @BindView(R.id.first_review_text) TextView mReviewText;
    @BindView(R.id.review_text_button) TextView mReviewButton;

    public static Intent newInstance(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (mMovie != null) {
            if (savedInstanceState != null) {
                mIsFavourited = savedInstanceState.getBoolean(EXTRA_IS_FAVOURITED, false);
                mTrailerList = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
                mReviewList = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            }
            String moviePath = mMovie.getmImageUrl();
            String url = NetworkUtils.buildMovieImageUrl("w500", moviePath).toString();

            getSupportActionBar().setTitle(mMovie.getmTitle());

            Picasso.with(this)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(mImage);

            mSynopsis.setText(mMovie.getmSynopsis());
            mRating.setText(getResources().getString(R.string.score_string, String.valueOf(mMovie.getmRating())));

            try {
                mRelease.setText(Helper.getYear(mMovie.getmReleaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);

            if (mTrailerList != null) {
                addTrailersToView();
            } else {
                if (NetworkUtils.isOnline(this)) {
                    new ConnectionTask(ConnectionTask.ConnectionTaskType.TRAILER, this).execute(NetworkUtils.buildAdditionalInfoMovieUrl(mMovie.getmId(), NetworkUtils.InfoType.VIDEOS));
                } else {
                    Toast.makeText(this, R.string.online_error, Toast.LENGTH_SHORT).show();
                }
            }

            if (mReviewList != null) {
                setReviewText();
            } else {
                if (NetworkUtils.isOnline(this)) {
                    new ConnectionTask(ConnectionTask.ConnectionTaskType.REVIEW,this).execute(NetworkUtils.buildAdditionalInfoMovieUrl(mMovie.getmId(), NetworkUtils.InfoType.REVIEWS));
                } else {
                    Toast.makeText(this, R.string.online_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_details_menu, menu);

        if(mIsFavourited) {
            MenuItem menuItem = menu.findItem(R.id.action_favourite);
            if (menuItem != null) {
                menuItem.setIcon(R.drawable.ic_favorite_white_24px);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.action_favourite) {
            if (mIsFavourited) {
                if (MovieDatabaseManager.getInstance().removeMovieFromFavourites(this, mMovie.getmId()) != 0) {
                    Toast.makeText(this, getResources().getString(R.string.favourite_removed), Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_favorite_border_white_24px);
                    mIsFavourited = false;
                }
            } else {
                boolean result = MovieDatabaseManager.getInstance().addMovieToFavourites(this, mMovie);
                if (result) {
                    item.setIcon(R.drawable.ic_favorite_white_24px);
                    Toast.makeText(this, getResources().getString(R.string.favourite_added), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.favourite_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_IS_FAVOURITED, mIsFavourited);
        outState.putParcelableArrayList(EXTRA_TRAILERS, mTrailerList);
        outState.putParcelableArrayList(EXTRA_REVIEWS, mReviewList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.review_text_button)
    public void show() {
        startActivity(ReviewsActivity.newInstance(this, mMovie.getmTitle(), mReviewList));
    }

    @Override
    public void onTaskDone(ConnectionTask.ConnectionTaskType type, String output) {
        try {
            switch(type) {
                case TRAILER:
                    mTrailerList = JsonUtils.getTrailerItems(MovieDetailsActivity.this, output);
                    if (mTrailerList != null || mTrailerList.size() < 0) addTrailersToView();
                    break;
                case REVIEW:
                    mReviewList = JsonUtils.getReviewItems(this, output);
                    setReviewText();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MovieDetailsActivity.this, getResources().getString(R.string.movie_data_error), Toast.LENGTH_LONG).show();
        }
    }

    private void addTrailersToView() {
        for (int i = 0; i < mTrailerList.size(); i++) {
            final Trailer trailer = mTrailerList.get(i);

            View trailerItemView = getLayoutInflater().inflate(R.layout.trailer_item, null, false);

            //trailer link
            TextView textView = (TextView) trailerItemView.findViewById(R.id.trailer_title);
            textView.setText(getResources().getString(R.string.review_string, trailer.getTitle()));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTrailerIntent(trailer.getKey());
                }
            });

            //share action
            ImageView imageView = (ImageView) trailerItemView.findViewById(R.id.share_icon);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createShareIntent(trailer.getKey());
                }
            });

            mTrailerContainer.addView(trailerItemView);
        }
    }

    private void setReviewText() {
        if (mReviewList.size() > 0) {
            mReviewText.setText(mReviewList.get(0).getReview());
            mReviewButton.setVisibility(View.VISIBLE);
        } else {
            mReviewText.setText(R.string.no_reviews);
            mReviewButton.setVisibility(View.INVISIBLE);
        }
    }

    private void createTrailerIntent(String key) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + key));
        startActivity(webIntent);
    }

    private void createShareIntent(String key) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Uri.parse(YOUTUBE_URL + key));
        sendIntent.setType("text/plain");

        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.share_error_string), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MovieDatabaseTaskLoader(this, mMovie.getmId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mIsFavourited = MovieDatabaseManager.getInstance().isMovieFavourited(data);
        invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
