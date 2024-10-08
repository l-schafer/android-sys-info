package com.schafer.sys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;

public class BatteryActivity extends AppCompatActivity implements Module {

    private final IntentFilter BiFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private Intent batteryStatus;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());
        setContentView(R.layout.activity_battery);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Battery");

        generateMetrics();
    }
    private static final Vector<Metric> metrics = new Vector<>();

    private void generateMetrics() {
        batteryStatus = this.registerReceiver(null, BiFilter);
        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int generalTableID = generateCategory(findViewById(R.id.battery_info_LL), "General");
        metrics.add(new Metric("charging", status == BatteryManager.BATTERY_STATUS_CHARGING));
        metrics.add(new Metric("charged", status == BatteryManager.BATTERY_STATUS_FULL));
        metrics.add(new Metric("health Status", healthStatus()));
        metrics.add(new Metric("charging via. USB", chargePlug == BatteryManager.BATTERY_PLUGGED_USB));
        metrics.add(new Metric("charging via. ac-adapter", chargePlug == BatteryManager.BATTERY_PLUGGED_AC));
        metrics.add(new Metric("charging via. wireless charger", chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS));
        metrics.add(new Metric("current charge", level * 100 / scale + "%"));
        metrics.add(new Metric("technology", batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)));
        //metrics.add(new Metric("Is Present", batteryStatus.getStringExtra(BatteryManager.EXTRA_PRESENT)));
        //metrics.add(new Metric("Temperature (Celsius)", batteryStatus.getStringExtra(BatteryManager.EXTRA_TEMPERATURE)));
        metrics.add(new Metric("voltage level (mV)", batteryStatus.getIntExtra("voltage", -1)));
        //metrics.add(new Metric("Capacity Remaining (mWh)", Integer.toString(batteryStatus.getIntExtra("", -1))));
        //metrics.add(new Metric("Total Capacity (mAh)", batteryStatus.getIntExtra((BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER / 1000))));
        //metrics.add(new Metric("Average Current (mA)", batteryStatus.getIntExtra(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE / 1000)));
        //metrics.add(new Metric("Remaining Energy (mWh)", batteryStatus.getIntExtra((BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER / 1000000))));
        metrics.add(new Metric("temperature", batteryStatus.getIntExtra("temperature", -1) / 10 +"℃"));
        displayMetrics(findViewById(generalTableID), metrics);
    }

    private String healthStatus() {
        int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        switch (health) {
            case 1:
                return "UNKNOWN";
            case 2:
                return "GOOD";
            case 3:
                return "OVERHEAT";
            case 4:
                return "DEAD";
            case 5:
                return "OVER_VOLTAGE";
            case 6:
                return "UNSPECIFIED_FAILURE";
            case 7:
                return "COLD";
            default:
                return "{error:unknown-status-ID}";
        }
    }
}