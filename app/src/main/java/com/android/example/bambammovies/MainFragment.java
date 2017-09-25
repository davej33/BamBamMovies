package com.android.example.bambammovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.bambammovies.data.Contract;
import com.android.example.bambammovies.utils.NetworkUtils;
import com.android.example.bambammovies.utils.SyncUtils;
import com.android.volley.toolbox.StringRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mAdapter;
    private static final int MOVIE_LOADER_ID = 111;
    private static final int COLUMNS_PORTRAIT = 2;
    private static final int COLUMNS_LANDSCAPE = 3;
    private static SharedPreferences sPreferences;
    private static boolean sPrefChanged = false;
    private static String sSortValue = "popular";


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //create view
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Log.i(LOG_TAG, "VIEW CREATED %%%%%%%% ");

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        mAdapter = new MovieAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        setupSharedPreferences();

        // set grid layout column numbers based on orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_PORTRAIT));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_LANDSCAPE));
        }

        // check db init state, display data if already initialized
        if (SyncUtils.isInitializeDb(getContext())) {
            displayData();
        } else {
            // if db not initialized, wait for 1 second to initialize then display
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayData();
                }
            }, 1000);
        }

        // Inflate the layout for this fragment
        return rootView;
    }


    private void setupSharedPreferences() {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                displayData();
                Log.i(LOG_TAG, "SharedPrefChangeListenerRun %%%%%%%%%%%% " + s);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {Contract.MovieEntry._ID, Contract.MovieEntry.MOVIE_TITLE, Contract.MovieEntry.MOVIE_POSTER, Contract.MovieEntry.MOVIE_FAVORITE};
        String[] one = {"1"};
        switch (sSortValue) {
            case "popular":
                Log.w(LOG_TAG, "Loader = Popular " + sSortValue);
                return new CursorLoader(getContext(), Contract.MovieEntry.MOVIE_URI, null, Contract.MovieEntry.MOVIE_POPULARITY_QUERY + "=?", one, null);
            case "top_rated":
                Log.w(LOG_TAG, "Loader = Rated " + sSortValue);
                String sortByRating = Contract.MovieEntry.MOVIE_RATING + " ASC";
                return new CursorLoader(getContext(), Contract.MovieEntry.MOVIE_URI, null, Contract.MovieEntry.MOVIE_RATING_QUERY + "=?", one, sortByRating);
            default:
                Log.e(LOG_TAG, "No Matching Sort Value for CursorLoader: " + sSortValue);
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "Cursor count: " + data.getCount());
        data.moveToFirst();
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public static void prefChanged(String stringValue) {
        sPrefChanged = true;
        sSortValue = stringValue;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void displayData() {
        if (mAdapter.getItemCount() == 0 || mAdapter == null) {
            Log.i(LOG_TAG, "DisplayData init run: ");
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            Log.i(LOG_TAG, "DisplayData restart run: ");
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(LOG_TAG, "onStart Run");
        if (sPrefChanged) {
            StringRequest r = SyncUtils.buildStringRequest(getContext());
            NetworkUtils.addToRequestQueue(getContext(), r, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayData();
                }
            }, 1000);
            sPrefChanged = false;
        }
    }
}
