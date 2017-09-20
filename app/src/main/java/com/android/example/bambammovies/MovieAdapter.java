package com.android.example.bambammovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.example.bambammovies.data.Contract;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by charlotte on 9/12/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static Cursor sCursor;
    private Context mContext;

    public MovieAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        sCursor.moveToPosition(position);

        String poster_url = sCursor.getString(sCursor.getColumnIndex(Contract.MovieEntry.MOVIE_POSTER));
        Log.i("Adapter", "Poster URL: " + poster_url);
        Picasso.with(mContext)
                .load(poster_url)
                .error(R.drawable.error)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(200, 300)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(holder.posterView);

    }

    @Override
    public int getItemCount() {
        if (sCursor != null) {
            return sCursor.getCount();
        } else {
            return 0;
        }
    }

    void swapCursor(Cursor cursor) {
        sCursor = cursor;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.poster_layout);
        }
    }
}
