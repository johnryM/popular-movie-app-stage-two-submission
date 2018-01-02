package com.mangonon.johnry.popularmovieapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mangonon.johnry.popularmovieapp.R;
import com.mangonon.johnry.popularmovieapp.app.model.Movie;
import com.mangonon.johnry.popularmovieapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>  {

    private static final String IMAGE_SIZE = "w500";
    private ArrayList<Movie> mMovieList = new ArrayList<>();

    private AdapterClickListener mListener;

    public MovieAdapter(ArrayList<Movie> movieList, AdapterClickListener listener) {
        mMovieList.addAll(movieList);
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position) {
        Log.d("Position", Integer.toString(position));
        movieViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void refresh(Context context, ArrayList<Movie> movies) {

        if (mMovieList != null) {
            mMovieList.clear();
            mMovieList.addAll(movies);
            this.notifyDataSetChanged();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.movie_data_error), Toast.LENGTH_SHORT).show();
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.movie_image_view) ImageView mMovieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            String moviePath = mMovieList.get(position).getmImageUrl();
            String url = NetworkUtils.buildMovieImageUrl(IMAGE_SIZE, moviePath).toString();
            Picasso.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(mMovieImageView);
        }

        @Override
        public void onClick(View v) {
            mListener.handleOnClick(this.getAdapterPosition());
        }
    }

    public interface AdapterClickListener {
        void handleOnClick(int position);
    }

}
