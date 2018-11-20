package com.tory.rednov;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import com.tory.rednov.view.VideoActivity;

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

    private static final String TAG = "MainActivity---> ";

    //To show animation dialog when try to find device.
    private DiscoveryDialogFragment discoveryDialogFragment;

    //Device list.
    List<Device> devices;

    //IPCam list to be shown on View.
    List<IPCamItem> ipCamList;
    IPCamListViewAdapter ipCamListViewAdapter;


    //FindDevicesThread.FindDevicesListener callback.
    @Override
    public void searchResult(final ArrayList<Device> devices) {
        Device device;
        IPCamItem ipCamItem;

        if (!devices.isEmpty()) {
            //Update IPCam list for view and Device list.
            for (int i = 0; i < devices.size(); i++) {
                device = devices.get(i);
                this.devices.add(device);

                ipCamItem = new IPCamItem(device.getServiceUrl(), R.drawable.ic_ipcam, i);
                ipCamList.add(ipCamItem);
            }
        } else {
            //No device found, for debug purpose, manually create some.
            if(IPCApplication.getAppSettings().getDebugFlag()) {
                device = new Device();
                device.setIpAddress(getString(R.string.play_list_local_file_ip));
                this.devices.add(device);

                ipCamItem = new IPCamItem(getString(R.string.play_list_local_file_ip), R.drawable.ic_ipcam, 0);
                ipCamList.add(ipCamItem);

                device = new Device();
                device.setIpAddress(getString(R.string.play_list_infrared_ipc_ip));
                this.devices.add(device);

                ipCamItem = new IPCamItem(getString(R.string.play_list_infrared_ipc_ip), R.drawable.ic_ipcam, 1);
                ipCamList.add(ipCamItem);

                device = new Device();
                device.setIpAddress(getString(R.string.play_list_internet_ip));
                this.devices.add(device);

                ipCamItem = new IPCamItem(getString(R.string.play_list_internet_ip), R.drawable.ic_ipcam, 2);
                ipCamList.add(ipCamItem);



                ipCamItem = new IPCamItem("", R.drawable.ic_add, 3);
                ipCamList.add(ipCamItem);

                ipCamItem = new IPCamItem("", R.drawable.ic_add, 4);
                ipCamList.add(ipCamItem);
            }

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                discoveryDialogFragment.dismiss();
                if (ipCamList.isEmpty()) {
                    UtiToast.Toast("No devices are found. Please check.");
                } else {
                    ipCamListViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //Button callback
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                discoveryDialogFragment.show(getSupportFragmentManager(),"Discovering");

                new FindDevicesThread(MainActivity.this, MainActivity.this).start();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Restore state by Bundle
        if (savedInstanceState != null) {
            savedInstanceState.get("save_something");
            Log.d(TAG, "onCreate: you saved msg is: " + savedInstanceState.get("save_something"));

        }

        discoveryDialogFragment = new DiscoveryDialogFragment();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);


        /*
         * Init app here.
         */
        UtiToast.setContext(MainActivity.this);

        //Init IPCamList for view
        ipCamList = new ArrayList<>();
        ipCamListViewAdapter = new IPCamListViewAdapter(
                MainActivity.this, R.layout.ipcam_item, ipCamList);

        ListView lvIPCam = findViewById(R.id.lvIPCam);
        lvIPCam.setAdapter(ipCamListViewAdapter);

        //Jump to video activity when click valid IPCam.
        //Add a IPCam manually when click empty slot.
        lvIPCam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                IPCamItem ipCamItem = ipCamList.get(position);
                if (TextUtils.isEmpty(ipCamItem.getIp())) {
                    Log.d(TAG, "onItemClick: an empty item");
                } else {
                    String ip = ipCamItem.getIp();
                    Log.d(TAG, "onItemClick: ip is " + ip);
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    intent.putExtra(getString(R.string.intent_key_ip), ip);
                    startActivity(intent);
                }

            }
        });

        //Init Device for FindDeviceThread.
        devices = new ArrayList<>();

        //For debug purpose request sd permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
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
        ipCamListViewAdapter.notifyDataSetChanged();
        Log.d(TAG, "onResume: done.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString("save_something", "debug same Mainactivity data.");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    //For sd permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ok");
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
