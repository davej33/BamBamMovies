package com.android.example.bambammovies;

import android.app.ActionBar;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set action bar
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        // add fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_fragment, new MainFragment().newInstance()).commit();

        // get screen width, pass to adapter
        int width = getScreenDimensions() / 2;
        MovieAdapter.setWidthHeight(width);
    }

    private int getScreenDimensions() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
