package com.android.example.bambammovies;

import android.app.ActionBar;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.example.bambammovies.data.Contract;
import com.android.example.bambammovies.utils.NetworkUtils;
import com.android.example.bambammovies.utils.SyncUtils;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.list_fragment, new MainFragment().newInstance()).commit(); // stopped here
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
