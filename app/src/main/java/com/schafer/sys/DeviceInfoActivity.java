package com.schafer.sys;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Vector;

public class DeviceInfoActivity extends AppCompatActivity implements Module {
    private LinearLayout deviceInfoLL;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_device_info);
        setTitle("Device Information");

        deviceInfoLL = findViewById(R.id.device_info_LL);
        generateOSMetrics();
        generateDeviceMetrics();
    }

    private void generateOSMetrics() {
        Vector<Metric> osMetrics = new Vector<>();
        int osTableID = generateCategory(deviceInfoLL, "Operating System");
        osMetrics.add(new Metric("base OS", Build.VERSION.BASE_OS));
        osMetrics.add(new Metric("release", Build.VERSION.RELEASE));
        osMetrics.add(new Metric("release codename", Build.VERSION.CODENAME));
        osMetrics.add(new Metric("incremental release", Build.VERSION.INCREMENTAL));
        osMetrics.add(new Metric("security patch", Build.VERSION.SECURITY_PATCH));
        osMetrics.add(new Metric("SDK", Build.VERSION.SDK_INT));
        osMetrics.add(new Metric("preview SDK", Build.VERSION.PREVIEW_SDK_INT));
        osMetrics.add(new Metric("Media Performance Class", Integer.toString(Build.VERSION.MEDIA_PERFORMANCE_CLASS)));
        displayMetrics(findViewById(osTableID), osMetrics);
    }
    private void generateDeviceMetrics() {
        Vector<Metric> deviceMetrics = new Vector<>();
        int deviceTableID = generateCategory(deviceInfoLL, "Device");
        deviceMetrics.add(new Metric("model", Build.MODEL));
        deviceMetrics.add(new Metric("name", Build.DEVICE));
        deviceMetrics.add(new Metric("type", Build.TYPE));
        deviceMetrics.add(new Metric("brand", Build.BRAND));
        deviceMetrics.add(new Metric("manufacturer", Build.MANUFACTURER));
        deviceMetrics.add(new Metric("host", Build.HOST));
        deviceMetrics.add(new Metric("product", Build.PRODUCT));
        deviceMetrics.add(new Metric("fingerprint", Build.FINGERPRINT));
        deviceMetrics.add(new Metric("build ID", Build.ID));
        deviceMetrics.add(new Metric("tags", Build.TAGS));
        deviceMetrics.add(new Metric("user", Build.USER));
        displayMetrics(findViewById(deviceTableID), deviceMetrics);

        //Hardware
        Vector<Metric> deviceHardware = new Vector<>();
        int hardwareTableID = generateCategory(deviceInfoLL, "Hardware");
        deviceHardware.add(new Metric("bootloader", Build.BOOTLOADER));
        deviceHardware.add(new Metric("main-board", Build.BOARD));
        deviceHardware.add(new Metric("hardware", Build.HARDWARE));
        deviceHardware.add(new Metric("ODM_SKU", Build.ODM_SKU));
        deviceHardware.add(new Metric("SKU", Build.SKU));
        deviceHardware.add(new Metric("SOC Manufacturer", Build.SOC_MANUFACTURER));
        deviceHardware.add(new Metric("SOC Model", Build.SOC_MODEL));
        deviceHardware.add(new Metric("display", Build.DISPLAY));
        deviceHardware.add(new Metric("radio version", Build.getRadioVersion()));
        //deviceHardware.add(new Metric("", Build.getSerial()));  //REQUIRES PRIVILEGED PHONE STATE PERMISSION

        displayMetrics(findViewById(hardwareTableID), deviceHardware);

        Vector<Metric> devicePartitions = new Vector<>();
        int devicePartitionsTableID = generateCategory(deviceInfoLL, "Partitions");
        List<Build.Partition> partitions = Build.getFingerprintedPartitions();

        for (Build.Partition partition : partitions) {
            System.out.println(partition.toString());
            devicePartitions.add(new Metric(partition.getName(), " "));
            devicePartitions.add(new Metric("      fingerprint", partition.getFingerprint()));
            devicePartitions.add(new Metric("      build time", partition.getBuildTimeMillis()));
            devicePartitions.add(new Metric("      hash code", partition.hashCode()));

        }
        displayMetrics(findViewById(devicePartitionsTableID), devicePartitions);
    }
}
