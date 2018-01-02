package com.mangonon.johnry.popularmovieapp;

import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mangonon.johnry.popularmovieapp.app.model.Movie;
import com.mangonon.johnry.popularmovieapp.db.MovieDatabaseManager;
import com.mangonon.johnry.popularmovieapp.utils.ConnectionTask;
import com.mangonon.johnry.popularmovieapp.utils.Helper;
import com.mangonon.johnry.popularmovieapp.utils.JsonUtils;
import com.mangonon.johnry.popularmovieapp.utils.MovieDatabaseTaskLoader;
import com.mangonon.johnry.popularmovieapp.utils.NetworkUtils;
import com.mangonon.johnry.popularmovieapp.ui.MovieAdapter;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.AdapterClickListener,
        SortDialogFragment.SortDialogListener, ConnectionTask.ConnectionTaskCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIEDB_LOADER_ID = 0;
    private final String EXTRA_CURRENT_SORT = "extra_current_sort";

    private final int SPAN_COUNT_PORTRAIT = 2;
    private final int SPAN_COUNT_LANDSCAPE = 3;

    private ArrayList<Movie> mMovieDatalist = new ArrayList<>();
    private NetworkUtils.SortType mCurrentSortType = NetworkUtils.SortType.POPULAR;

    @BindView(R.id.movie_list) RecyclerView mMovieRecyclerview;
    @BindView(R.id.error_text_view) TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mCurrentSortType = (NetworkUtils.SortType) savedInstanceState.getSerializable(EXTRA_CURRENT_SORT);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        int SPAN_COUNT = Helper.isLandscape(this) ? SPAN_COUNT_LANDSCAPE : SPAN_COUNT_PORTRAIT;
        mMovieRecyclerview.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        mMovieRecyclerview.setHasFixedSize(true);

        if (NetworkUtils.isOnline(this) && (mCurrentSortType != NetworkUtils.SortType.FAVOURITE)) {
            new ConnectionTask(this).execute(NetworkUtils.buildMovieDataUrl(mCurrentSortType));
        } else if (mCurrentSortType == NetworkUtils.SortType.FAVOURITE) {
            getSupportLoaderManager().initLoader(MOVIEDB_LOADER_ID, null, this);
        } else {
            showErrorView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort) {
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleOnClick(int position) {
        Movie movie = mMovieDatalist.get(position);
        startActivity(MovieDetailsActivity.newInstance(this, movie));
    }

    @Override
    public void onSortSelected(NetworkUtils.SortType sortType) {
        mCurrentSortType = sortType;

        if (sortType == NetworkUtils.SortType.FAVOURITE) {
            getSupportLoaderManager().initLoader(MOVIEDB_LOADER_ID, null, this);
        } else {
            new ConnectionTask(this).execute(NetworkUtils.buildMovieDataUrl(sortType));
        }

        MovieAdapter movieAdapter = (MovieAdapter) mMovieRecyclerview.getAdapter();

        if (movieAdapter == null) {
            mMovieRecyclerview.setAdapter(new MovieAdapter(mMovieDatalist, this));
        } else {
            movieAdapter.refresh(this, mMovieDatalist);
        }
        showMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_CURRENT_SORT, mCurrentSortType);
        super.onSaveInstanceState(outState);
    }

    private void showSortDialog() {
        if (!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, getResources().getString(R.string.connection_error),Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fm  = getSupportFragmentManager();
        SortDialogFragment sortDialog = new SortDialogFragment();
        sortDialog.show(fm, "sort_fragment");
    }

    private void showErrorView() {
        mErrorTextView.setVisibility(View.VISIBLE);
        mMovieRecyclerview.setVisibility(View.INVISIBLE);
    }

    private void showMovies() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMovieRecyclerview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskDone(int requestCode, String output) {
        try {
            mMovieDatalist = JsonUtils.getMovieItems(MainActivity.this, output);
            mMovieRecyclerview.setAdapter(new MovieAdapter(mMovieDatalist, MainActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.movie_data_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MovieDatabaseTaskLoader(this, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieDatalist.clear();
        mMovieDatalist = MovieDatabaseManager.getInstance().getAllFavourites(data);
        mMovieRecyclerview.setAdapter(new MovieAdapter(mMovieDatalist, this));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieRecyclerview.setAdapter(new MovieAdapter(new ArrayList<Movie>(), this));
    }
}
