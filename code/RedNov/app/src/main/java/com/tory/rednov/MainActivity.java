package com.tory.rednov;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.tory.rednov.controller.onvif.FindDevicesThread;
import com.tory.rednov.model.AppSettings;
import com.tory.rednov.model.Device;
import com.tory.rednov.model.IPCApplication;
import com.tory.rednov.model.IPCam;
import com.tory.rednov.utilities.UtiToast;
import com.tory.rednov.view.AppSettingsActivity;
import com.tory.rednov.view.DiscoveryDialogFragment;
import com.tory.rednov.view.IPCamItem;
import com.tory.rednov.view.IPCamListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FindDevicesThread.FindDevicesListener {

    // Used to load the 'native-lib' library on application startup.
    /*
    static {
        System.loadLibrary("native-lib");
    }
    */

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();

    private static final String TAG = "MainActivity";

    private DiscoveryDialogFragment discoveryDialogFragment;

    //FindDevicesThread.FindDevicesListener callback.
    @Override
    public void searchResult(ArrayList<Device> devices) {
        //Not found any devices.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                discoveryDialogFragment.dismiss();
                //adapter.notifyDataSetChanged();
            }
        });

        if (devices.isEmpty()) {
            if (IPCApplication.getAppSettings().getDebugFlag()){
                List<IPCamItem> ipCamList = new ArrayList<>();
                ipCamList.add(new IPCamItem("192.168.9.6", R.drawable.ic_ipcam));
                ipCamList.add(new IPCamItem("192.168.9.100", R.drawable.ic_ipcam));
                ipCamList.add(new IPCamItem("192.168.9.101", R.drawable.ic_ipcam));

                IPCamListViewAdapter ipCamListViewAdapter = new IPCamListViewAdapter(
                        MainActivity.this, R.layout.ipcam_item, ipCamList);

                ListView lvIPCam = findViewById(R.id.lvIPCam);
                lvIPCam.setAdapter(ipCamListViewAdapter);
            }
        }

    }

    //Floatbutton callback


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                discoveryDialogFragment.show(getSupportFragmentManager(),"Discovering");
                //new FindDevicesThread(MainActivity.this, MainActivity.this).start();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        discoveryDialogFragment = new DiscoveryDialogFragment();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);


        /*
         * Init app here.
         */
        UtiToast.setContext(MainActivity.this);

        if (IPCApplication.getAppSettings().getDebugFlag()){
            List<IPCamItem> ipCamList = new ArrayList<>();
            ipCamList.add(new IPCamItem("192.168.9.6", R.drawable.ic_ipcam));
            ipCamList.add(new IPCamItem("192.168.9.100", R.drawable.ic_ipcam));
            ipCamList.add(new IPCamItem("192.168.9.101", R.drawable.ic_ipcam));

            IPCamListViewAdapter ipCamListViewAdapter = new IPCamListViewAdapter(
                    MainActivity.this, R.layout.ipcam_item, ipCamList);

            ListView lvIPCam = findViewById(R.id.lvIPCam);
            lvIPCam.setAdapter(ipCamListViewAdapter);
        }
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
