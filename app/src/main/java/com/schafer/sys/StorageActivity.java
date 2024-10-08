package com.schafer.sys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.usage.ExternalStorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.transition.Slide;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class StorageActivity extends AppCompatActivity implements Module {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_storage);
        setTitle("Storage");
        generateMetrics();
    }


    private void generateMetrics() {
        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        Vector<Metric> m = new Vector<>();
        for (StorageVolume storageVolume : storageVolumes) {
            int tableId = generateCategory(findViewById(R.id.storage_info_LL), storageVolume.getMediaStoreVolumeName());
            StatFs SSM = new StatFs(Objects.requireNonNull(storageVolume.getDirectory()).getPath());

            //get access state
            boolean readable = false; boolean writeable = false;
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) { readable = writeable = true; }
            else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) { readable = true; }
            m.add(new Metric("media status", Environment.getExternalStorageState()));
            m.add(new Metric("readable", readable + ""));
            m.add(new Metric("writeable", writeable + ""));

            //general
            m.add(new Metric("availiable (excl. cache)", (SSM.getAvailableBytes() / (1024 * 1024)) + " MB"));
            m.add(new Metric("maximum capacity", (SSM.getTotalBytes() / (1024 * 1024)) + " MB"));
            m.add(new Metric("free (incl. cache)", (SSM.getFreeBytes() / (1024 * 1024)) + " MB"));
            m.add(new Metric("% availiable", Math.round((((float) SSM.getAvailableBytes()) / (float) SSM.getTotalBytes()) * 100) + "%"));
            m.add(new Metric("% free", Math.round((((float) SSM.getFreeBytes()) / ((float) SSM.getTotalBytes())) * 100) + "%"));
            m.add(new Metric("blocks availiable", SSM.getAvailableBlocksLong() + ""));
            m.add(new Metric("blocks free", SSM.getFreeBlocksLong() + ""));
            m.add(new Metric("block count", SSM.getBlockCountLong() + ""));
            m.add(new Metric("block size", SSM.getBlockSizeLong() + ""));

            //media options (requires user permission, so skipped for now.)

            // THIS IS A SYSTEM-PROTECTED PERMISSION, IT WILL NOT WORK WITHOUT MANUALLY GIVING PRIVILAGES VIA ROOT PACKAGE MANAGER
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED) {

                String[] mediaData = new String[0];
                try {
                    mediaData = getExternalStorageUsageInfo();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                m.add(new Metric("Media Total", mediaData[0]));
                m.add(new Metric("Images Total", mediaData[1]));
                m.add(new Metric("Audio Total", mediaData[2]));
                m.add(new Metric("Video Total", mediaData[3]));
                m.add(new Metric("Apps Total", mediaData[4]));

            }
            displayMetrics(findViewById(tableId), m);
        }
        /*
        mI.add(new Metric("Availiable (excl. cache)", (sI.getAvailableBytes() / (1024 * 1024)) + " MB"));
        mI.add(new Metric("Maximum Capacity", (sI.getTotalBytes() / (1024 * 1024)) + " MB"));
        mI.add(new Metric("Free (incl. cache)", (sI.getFreeBytes() / (1024 * 1024)) + " MB"));
        mI.add(new Metric("% Availiable", Math.round((((float) sI.getAvailableBytes()) / (float) sI.getTotalBytes()) * 100) + "%"));
        mI.add(new Metric("% Free", Math.round((((float)sI.getFreeBytes()) / ( (float)sI.getTotalBytes())) * 100) + "%"));
        mI.add(new Metric("Blocks Availiable", sI.getAvailableBlocksLong() + ""));
        mI.add(new Metric("Blocks Free", sI.getFreeBlocksLong() + ""));
        mI.add(new Metric("Block Count", sI.getBlockCountLong() + ""));
        mI.add(new Metric("Block Size", sI.getBlockSizeLong() + ""));

        //External
        boolean readable = false; boolean writeable = false;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) { readable = writeable = true; }
        else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) { readable = true; writeable = false; }

        mE.add(new Metric("Media Status", Environment.getExternalStorageState()));
        mE.add(new Metric("Readable", readable + ""));
        mE.add(new Metric("Writeable", writeable + ""));
        if (readable) {
            StatFs sE = new StatFs(Environment.getExternalStorageDirectory().getPath());
            mE.add(new Metric("Availiable (excl. cache)", (sE.getAvailableBytes() / (1024 * 1024)) + " MB"));
            mE.add(new Metric("Maximum Capacity", (sE.getTotalBytes() / (1024 * 1024)) + " MB"));
            mE.add(new Metric("Free (incl. cache)", (sE.getFreeBytes() / (1024 * 1024)) + " MB"));
            mE.add(new Metric("% Availiable", Math.round((( (float) sE.getAvailableBytes()) / ( (float) sE.getTotalBytes())) * 100) + "%"));
            mE.add(new Metric("% Free", Math.round(((float) sE.getFreeBytes() / (float) sE.getTotalBytes()) * 100) + "%"));
            mE.add(new Metric("Blocks Availiable", sE.getAvailableBlocksLong() + ""));
            mE.add(new Metric("Blocks Free", sE.getFreeBlocksLong() + ""));
            mE.add(new Metric("Block Count", sE.getBlockCountLong() + ""));
            mE.add(new Metric("Block Size", sE.getBlockSizeLong() + ""));
            //String[] mediaData = getExternalStorageUsageInfo();
            //mE.add(new Metric("Media Total", mediaData[0]));
            //mE.add(new Metric("Images Total", mediaData[1]));
            //mE.add(new Metric("Audio Total", mediaData[2]));
            //mE.add(new Metric("Video Total", mediaData[3]));
            //mE.add(new Metric("Apps Total", mediaData[4]));
        }
        */
    }

    //REQUIRES PACKAGE USAGE STATS PERMISSION

    private String[] getExternalStorageUsageInfo() throws IOException {
        StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
        ExternalStorageStats externalStorageStats;
        UserHandle user = android.os.Process.myUserHandle();
        externalStorageStats = storageStatsManager.queryExternalStatsForUser(StorageManager.UUID_DEFAULT, user);
        assert externalStorageStats != null;
        String totalMBytes = (externalStorageStats.getTotalBytes() / (1024 * 1024)) + " MB";
        String imageMBytes = (externalStorageStats.getImageBytes() / (1024 * 1024)) + " MB";
        String audioMBytes = (externalStorageStats.getAudioBytes() / (1024 * 1024)) + " MB";
        String videoMBytes = (externalStorageStats.getVideoBytes() / (1024 * 1024)) + " MB";
        String appMBytes = (externalStorageStats.getAppBytes() / (1024 * 1024)) + " MB";
        String[] data = {"", "", "", "", ""};
        data[0] = totalMBytes;
        data[1] = imageMBytes;
        data[2] = audioMBytes;
        data[3] = videoMBytes;
        data[4] = appMBytes;
        return data;
    }
}
