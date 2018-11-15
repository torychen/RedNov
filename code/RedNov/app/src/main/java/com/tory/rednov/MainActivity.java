package com.tory.rednov;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tory.rednov.controller.AppSettingsListener;
import com.tory.rednov.model.AppSettings;
import com.tory.rednov.model.IPCApplication;
import com.tory.rednov.utilities.UtiToast;
import com.tory.rednov.view.AppSettingsActivity;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /*
         * Init app here.
         */
        UtiToast.setContext(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //tory pass. UtiToast.Toast("you click settings.");
            Intent intent = new Intent(MainActivity.this, AppSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    void updateAppSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean debugFlag = sp.getBoolean(this.getString(R.string.pref_key_debug_flag), false);
        IPCApplication.getAppSettings().setDebugFlag(debugFlag);

        Log.d(TAG, "debug flag is " + debugFlag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAppSettings();
    }
}
