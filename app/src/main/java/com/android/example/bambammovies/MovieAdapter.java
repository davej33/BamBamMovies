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
    private static int sHolderWidth;
    private static int sHolderHeight;

    public MovieAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_layout, parent, false);
        view.getLayoutParams().height = sHolderHeight;
        view.getLayoutParams().width = sHolderWidth;
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        sCursor.moveToPosition(position);

        String poster_url = sCursor.getString(sCursor.getColumnIndex(Contract.MovieEntry.MOVIE_POSTER));
        int id = sCursor.getInt(sCursor.getColumnIndex(Contract.MovieEntry.MOVIE_DB_ID));
        String title = sCursor.getString(sCursor.getColumnIndex(Contract.MovieEntry.MOVIE_TITLE));

        Log.w("Adapter", "ID / TITLE: " + id + " / " + title);
        Picasso.with(mContext)
                .load(poster_url)
                .error(R.drawable.error)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(sHolderWidth, sHolderHeight)
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

    static void setWidthHeight(int width) {
        sHolderWidth = width;
        sHolderHeight = (int) (sHolderWidth * 1.5);
        Log.i("Adapter", "width: " + width);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterView;

          MovieViewHolder(View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.poster_layout);
        }


    }
}
