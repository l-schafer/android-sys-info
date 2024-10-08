package com.schafer.sys;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class RAMActivity extends AppCompatActivity implements Module {
    private LinearLayout ramLL;

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_ram);
        setTitle("RAM Information");

        ramLL = findViewById(R.id.ram_info_LL);
        getRAMInfo();
    }

    private void getRAMInfo() {
        String theString = "";
        try {
            String[] DATA = {"/system/bin/cat", "/proc/meminfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] byteArry = new byte[1024];
            while (inputStream.read(byteArry) != -1) {
                theString = theString + new String(byteArry);
            }
            inputStream.close();
        } catch (Exception ex) { ex.printStackTrace(); }

        //parse data via. magic
        List<String> s = Arrays.stream(theString.split("[:\n\r\t]")).map(String::new).collect(Collectors.toList());
        for (int i = 0; i < s.size(); i++) {
            s.set(i,s.get(i).trim());

            //Fix for some metrics having an additional or unlabelled value;
            if (i % 2 == 0 && s.get(i).endsWith("kB")) {
                s.remove(i);
            }
        }

        int tableID = generateCategory(ramLL, "/proc/meminfo");
        Vector<Metric> x = new Vector<>();
        while (s.size() >= 2) {
            x.add(new Metric(s.remove(0), s.remove(0)));
        }
        displayMetrics(findViewById(tableID), x);
    }
}
