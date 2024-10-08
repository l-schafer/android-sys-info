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

public class CPUActivity extends AppCompatActivity implements Module {
    private LinearLayout cpuLL;

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cpu);
        setTitle("CPU Information");

        cpuLL = findViewById(R.id.cpu_info_LL);
        getCPUInfo();
    }



    private void getCPUInfo() {
        String theString = "";
        try {
            String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] byteArry = new byte[1024];
            while (inputStream.read(byteArry) != -1) {
                theString = theString + new String(byteArry);
            }
            inputStream.close();
        } catch (Exception ex) { ex.printStackTrace(); }

        //Log.d("CPU_DATA_theString", theString);
        //parse data via. magic
        List<String> s = Arrays.stream(theString.split("[:\n\r]")).map(String::new).collect(Collectors.toList());
        //Log.d("CPU_DATA_PRE-SORT_TEST", s.toString());
        for (int i = 0; i < s.size(); i++) {
            s.set(i,s.get(i).trim());
            if (s.get(i).isEmpty()) {
                s.remove(i);
            }
            //Log.d("CPU_DATA_TEST", s.get(i));
        }

        //General
        int tableId = Integer.MIN_VALUE; int t = 0;
        Vector<Metric> general = new Vector<>();
        while (!s.get(0).contains("processor")) {
            if (t == 0) {
                tableId = generateCategory(cpuLL, "General");
            }
            else {
                general.add(new Metric(s.remove(0), s.remove(0)));
            }
            t = 1;
        }

        Vector<Metric> x = new Vector<>();
        int max = Integer.MAX_VALUE; int counter = 0; int tempTID = Integer.MIN_VALUE;
        while (s.size() >= 2) {
            if (s.get(0).contains("processor")) {
                if (!x.isEmpty()) {
                    if (max == Integer.MAX_VALUE) { max = x.size(); }
                    displayMetrics(findViewById(tempTID), x);
                    counter = 0;
                }
                tempTID = generateCategory(cpuLL, s.remove(0) + s.remove(0));
            }
            else {
                if (counter < max) {
                    x.add(new Metric(s.remove(0), s.remove(0)));
                }
                else {
                    displayMetrics(findViewById(tempTID), x);
                    break;
                }
                counter++;
            }
        }

        while (s.size() >= 2) {
            general.add(new Metric(s.remove(0), s.remove(0)));
        }
        if (tableId!=Integer.MIN_VALUE) {
            displayMetrics(findViewById(tableId), general);
        }


        /*
        int mainID = generateCategory(cpuLL, "Chip Information");
        Vector<Metric> h = new Vector<>();
        h.add(new Metric(s.remove(0), s.remove(0)));
        h.add(new Metric(s.remove(0), s.remove(0)));
        h.add(new Metric(s.remove(s.size()-10), s.remove(s.size()-9)));
        h.add(new Metric(s.remove(s.size()-8), s.remove(s.size()-7)));
        h.add(new Metric(s.remove(s.size()-6), s.remove(s.size()-5)));
        h.add(new Metric(s.remove(s.size()-4), s.remove(s.size()-3)));
        h.add(new Metric(s.remove(s.size()-2), s.remove(s.size()-1)));
        displayMetrics(findViewById(mainID), h);
        */
        /*
        int proCount = 0;
        for (int i = 0; i < s.size()-10; i+= 14) {
            Vector<Metric> tmp = new Vector<>();
            int cpuCatID = generateCategory(cpuLL, "Processor " + proCount);
            tmp.add(new Metric(s.get(i), s.get(i+1)));
            tmp.add(new Metric(s.get(i+2), s.get(i+3)));
            tmp.add(new Metric(s.get(i+4), s.get(i+5)));
            tmp.add(new Metric(s.get(i+6), s.get(i+7)));
            tmp.add(new Metric(s.get(i+8), s.get(i+9)));
            tmp.add(new Metric(s.get(i+10), s.get(i+11)));
            tmp.add(new Metric(s.get(i+12), s.get(i+13)));
            proCount++;
            displayMetrics(findViewById(cpuCatID), tmp);
        }

        */
    }
}
