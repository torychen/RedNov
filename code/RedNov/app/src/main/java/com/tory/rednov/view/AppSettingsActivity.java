package com.tory.rednov.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.tory.rednov.R;
import com.tory.rednov.view.AppSettingsFragment;

public class AppSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AppSettingsActivity_CCC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AppSettingsFragment appSettingsFragment = new AppSettingsFragment();
        transaction.add(R.id.flAppSettings, appSettingsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //To avoid recreate parent activity
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: back to parent activity.");
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
