package com.schafer.sys;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class AppsActivity extends AppCompatActivity implements Module {

    private LinearLayout appsLL;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());
        setContentView(R.layout.activity_apps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Apps");

        appsLL = findViewById(R.id.apps_info_LL);
        generateMetrics();
    }
    private void generateMetrics() {

        // Get a reference to the PackageManager
        PackageManager pm = getPackageManager();

        // Get a list of installed applications
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Create a Vector to hold the metrics
        Vector<Metric> a = new Vector<>();
        Vector<Metric> b = new Vector<>();

        int appsId = generateCategory(appsLL, "Installed Apps");
        int weirdAppsId = generateCategory(appsLL, "More Packages");

        // Loop through the list of installed applications
        for (ApplicationInfo app : apps) {
            String appLabel = (String) pm.getApplicationLabel(app);

            // Add the application name to the metrics
            if(appLabel.contains(".")) {
                System.out.println(appLabel);
                b.add(new Metric(appLabel, " "));
            } else {
                a.add(new Metric(appLabel, app.packageName));
            }

            // Add additional data about the application
            //a.add(new Metric("datapoint label", "data"));
            //a.add(new Metric("datapoint label", "data"));
            //a.add(new Metric("datapoint label", "data"));
        }

        a.sort(new Comparator<Metric>() {
            @Override
            public int compare(Metric m1, Metric m2) {
                return m1.title.compareTo(m2.title);
            }
        });
        b.sort(new Comparator<Metric>() {
            @Override
            public int compare(Metric m1, Metric m2) {
                return m1.title.compareTo(m2.title);
            }
        });



        // Display the metrics
        a.add(0, new Metric("[ Includes system apps ]", " "));
        displayMetrics(findViewById(appsId), a);
        displayMetrics(findViewById(weirdAppsId), b);



        //Vector<Metric> a = new Vector<>();
        //int appsId = generateCategory(appsLL, "Installed Apps");
        //a.add(new Metric("<subhead>", "App Name"));
        //a.add(new Metric("datapoint label", "data"));
        //a.add(new Metric("datapoint label", "data"));
        //a.add(new Metric("datapoint label", "data"));
        //a.add(new Metric("datapoint label", "data"));
        //...
        //displayMetrics(findViewById(appsId), a);
    }
}