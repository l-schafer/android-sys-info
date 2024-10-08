package com.schafer.sys;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothStatusCodes;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class BluetoothActivity extends AppCompatActivity implements Module {
    private LinearLayout bluetoothLL;

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bluetooth);
        setTitle("Bluetooth Devices Information");

        bluetoothLL = findViewById(R.id.bluetooth_info_LL);
        getDevicesInfo();
    }

    private void getDevicesInfo() {

        BluetoothAdapter btAdapt = BluetoothAdapter.getDefaultAdapter();

        // Enumerate all paired/saved devices
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            int id = generateCategory(bluetoothLL, "Permission required");
            Vector<Metric> x = new Vector<>();
            x.add(new Metric("You must grant \"Nearby Devices\" permission in the system app settings.", " "));
            displayMetrics(findViewById(id), x);
        } else {

            int bluetoothGeneral = generateCategory(bluetoothLL, "Bluetooth Hardware");
            Vector<Metric> bg = new Vector<>();
            bg.add(new Metric("address", String.valueOf(btAdapt.getAddress()), "02:00:00:00:00:00", "INSUFFICIENT_PERMISSION", "<data>"));
            bg.add(new Metric("bonded devices", String.valueOf(btAdapt.getBondedDevices())));
            //bg.add(new Metric("address", btAdapt.getDiscoverableTimeout()));
            bg.add(new Metric("max advertising length", btAdapt.getLeMaximumAdvertisingDataLength()));
            bg.add(new Metric("max connected devices", btAdapt.getMaxConnectedAudioDevices()));
            bg.add(new Metric("name", btAdapt.getName()));
            //bg.add(new Metric("address", btAdapt.getProfileConnectionState()));
            //bg.add(new Metric("address", btAdapt.getScanMode()));
            bg.add(new Metric("state", getBluetoothState(btAdapt.getState())));
            //bg.add(new Metric("address", btAdapt.isDiscovering()));
            bg.add(new Metric("enabled", btAdapt.isEnabled()));
            bg.add(new Metric("<subhead>", "Features"));
            bg.add(new Metric("LE 2M PHY", btAdapt.isLe2MPhySupported(), "supported", "not supported"));
            bg.add(new Metric("LE broadcast assistant", getFeatureSupport(btAdapt.isLeAudioBroadcastAssistantSupported())));
            bg.add(new Metric("LE broadcast source", getFeatureSupport(btAdapt.isLeAudioBroadcastSourceSupported())));
            bg.add(new Metric("LE audio", getFeatureSupport(btAdapt.isLeAudioSupported())));
            bg.add(new Metric("LeCodedPhy", btAdapt.isLeCodedPhySupported()));
            bg.add(new Metric("Le extended advertising", btAdapt.isLeExtendedAdvertisingSupported(), "supported", "not supported"));
            bg.add(new Metric("Le periodic advertising", btAdapt.isLePeriodicAdvertisingSupported(), "supported", "not supported"));
            bg.add(new Metric("multiple advertisement", btAdapt.isMultipleAdvertisementSupported(), "supported", "not supported"));
            bg.add(new Metric("offloaded filtering", btAdapt.isOffloadedFilteringSupported(), "supported", "not supported"));
            bg.add(new Metric("offloaded scan batching", btAdapt.isOffloadedScanBatchingSupported(), "supported", "not supported"));
            displayMetrics(findViewById(bluetoothGeneral), bg);


            Set<BluetoothDevice> pairedDevices = btAdapt.getBondedDevices();
            if (!pairedDevices.isEmpty()) {
                // Convert the set to a list
                List<BluetoothDevice> deviceList = GetBluetoothDevicesAlphabetical(pairedDevices);

                for (BluetoothDevice device : deviceList) {

                    int tmpID = generateCategory(bluetoothLL, device.getName());
                    Vector<Metric> x = new Vector<>();
                    x.add(new Metric("address", device.getAddress()));
                    x.add(new Metric("alias", device.getAlias()));
                    x.add(new Metric("bond state", getBondState(device.getBondState())));
                    x.add(new Metric("device type", getDeviceType(device.getType())));
                    x.add(new Metric("hash code", device.hashCode()));
                    x.add(new Metric("UUIDs", Arrays.toString(device.getUuids())));
                    displayMetrics(findViewById(tmpID), x);
                }
            }
        }

        //getSupportedCodecs();

    }

    //public void getSupportedCodecs() {
    //    AudioManager audioManager;
    //    audioManager = (AudioManager) BluetoothActivity.this.getSystemService(Context.AUDIO_SERVICE);
    //    AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
//
    //    for (AudioDeviceInfo deviceInfo : audioDevices) {
//
//
    //        int tmpID = generateCategory(bluetoothLL, String.valueOf(deviceInfo.getId()));
    //        Vector<Metric> x = new Vector<>();
    //        x.add(new Metric("Supported Codec", String.valueOf(deviceInfo.getAudioProfiles())));
    //        audioManager.
    //        displayMetrics(findViewById(tmpID), x);
//
//
    //    }
    //}

    @NonNull
    private static List<BluetoothDevice> GetBluetoothDevicesAlphabetical(Set<BluetoothDevice> pairedDevices) {
        List<BluetoothDevice> deviceList = new ArrayList<>(pairedDevices);

        // Sort the list by device name in alphabetical order
        deviceList.sort(new Comparator<BluetoothDevice>() {
            @SuppressLint("MissingPermission")
            @Override
            public int compare(BluetoothDevice device1, BluetoothDevice device2) {
                return device1.getName().compareTo(device2.getName());
            }
        });
        return deviceList;
    }

    private String getDeviceType(int device_type) {
        switch (device_type) {
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                return "UNKNOWN";
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return "CLASSIC";
            case BluetoothDevice.DEVICE_TYPE_LE:
                return "LE";
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                return "DUAL";
            default:
                return "--error: unknown device type ID--";
        }
    }

    private String getBondState(int bond_state) {
        switch(bond_state) {
            case BluetoothDevice.BOND_NONE:
                return "NONE";
            case BluetoothDevice.BOND_BONDING:
                return "BONDING";
            case BluetoothDevice.BOND_BONDED:
                return "BONDED";
            default:
                return "--error: unknown bond state--";
        }
    }

    private String getBluetoothState(int bluetooth_state) {
        switch(bluetooth_state) {
            case BluetoothAdapter.STATE_OFF:
                return "OFF";
            case BluetoothAdapter.STATE_TURNING_ON:
                return "TURNING_ON";
            case BluetoothAdapter.STATE_ON:
                return "ON";
            case BluetoothAdapter.STATE_TURNING_OFF:
                return "TURNING_OFF";
            default:
                return "--error: unknown state--";
        }
    }

    private String getFeatureSupport(int bt_feature_supported) {
        switch(bt_feature_supported) {
            case BluetoothStatusCodes.FEATURE_SUPPORTED:
                return "SUPPORTED";
            case BluetoothStatusCodes.FEATURE_NOT_SUPPORTED:
                return "NOT_SUPPORTED";
            case BluetoothStatusCodes.ERROR_UNKNOWN:
                return "ERR_UNKNOWN";
            case BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED:
                return "ERR_BLUETOOTH_NOT_ENABLED";
            default:
                return "--error: unknown feature status--";
        }
    }

}
