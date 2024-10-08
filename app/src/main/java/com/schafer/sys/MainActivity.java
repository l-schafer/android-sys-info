package com.schafer.sys;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity{
    ListItem appsLI;
    ListItem batteryLI;
    ListItem storageLI;
    ListItem displayLI;
    ListItem cpuLI;
    ListItem telecomLI;
    ListItem ramLI;
    ListItem bluetoothLI;
    ListItem wifiLI;
    SharedPreferences mSharedPrefs;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Change the theme if preference is true
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = mSharedPrefs.getBoolean("dark_theme", false);
        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new AutoTransition());

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeCategories();
        setClickListeners();
    }


    private void initializeCategories() {
        appsLI = new ListItem(R.id.appsLL, R.id.list_apps_image_view);
        appsLI.setImage(R.drawable.round_apps_black_24dp);

        batteryLI = new ListItem(R.id.batteryLL, R.id.list_battery_image_view);
        batteryLI.setImage(R.drawable.ic_round_battery_std_24);

        storageLI = new ListItem(R.id.storageLL, R.id.list_storage_image_view);
        storageLI.setImage(R.drawable.ic_round_storage_24);

        displayLI = new ListItem(R.id.deviceLL, R.id.list_device_image_view);
        displayLI.setImage(R.drawable.ic_round_perm_device_information_24);

        cpuLI     = new ListItem(R.id.cpuLL, R.id.list_cpu_image_view);
        cpuLI.setImage(R.drawable.ic_round_memory_24);

        telecomLI = new ListItem(R.id.telecomLL, R.id.list_telecom_image_view);
        telecomLI.setImage(R.drawable.ic_baseline_cell_tower_24);

        ramLI     = new ListItem(R.id.ramLL, R.id.list_ram_image_view);
        ramLI.setImage(R.drawable.ic_round_memory_24);

        bluetoothLI = new ListItem(R.id.bluetoothLL, R.id.list_bluetooth_image_view);
        bluetoothLI.setImage(R.drawable.round_bluetooth_black_24dp);

        wifiLI = new ListItem(R.id.wifiLL, R.id.list_wifi_image_view);
        wifiLI.setImage(R.drawable.round_wifi_black_24dp);
    }

    private void setClickListeners() {
        appsLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AppsActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        batteryLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BatteryActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        storageLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, StorageActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        displayLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DeviceInfoActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        cpuLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CPUActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        telecomLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TelecomActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        ramLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RAMActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        bluetoothLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BluetoothActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));
        wifiLI.linearLayout().setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WiFiActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListItem extends AppCompatActivity {
        private final LinearLayout linearLayout;
        private final ImageView imageView;
        ListItem(int linearLayoutID, int imageViewID) {
            this.linearLayout = MainActivity.this.findViewById(linearLayoutID);
            this.imageView = MainActivity.this.findViewById(imageViewID);
        }
        public LinearLayout linearLayout() {
            return linearLayout;
        }
        public void setImage(int drawableID) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(MainActivity.this, drawableID));
        }
    }
}

