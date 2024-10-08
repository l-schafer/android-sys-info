package com.schafer.sys;

import static android.graphics.Typeface.BOLD;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Objects;
import java.util.Vector;

public interface Module {

    default void displayMetrics(TableLayout table, Vector<Metric> metrics) {
        for (Metric m : metrics) {
            TableRow tr = new TableRow(table.getContext());
            TextView title = new TextView(tr.getContext());
            Space space = new Space(tr.getContext());
            TextView data = new TextView(tr.getContext());
            if (Objects.equals(m.title, "<subhead>")) {
                title.setText(m.data);
                data.setText("");
                title.setTextSize(17); data.setTextSize(12);
                title.setTypeface(Typeface.DEFAULT, BOLD);
                title.setMinHeight(90);
                title.setGravity(Gravity.BOTTOM);
            } else {
                title.setText(m.title);
                data.setText(m.data);
                title.setTextSize(12); data.setTextSize(12);
            }
            space.setMinimumWidth(100);
            title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            data.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            data.setGravity(Gravity.START);
            tr.addView(title);
            tr.addView(space);
            tr.addView(data);
            table.addView(tr);
        }
        metrics.clear();
    }
    default int generateCategory(LinearLayout linearLayout, String name) {
        Space space = new Space(linearLayout.getContext());
        TextView categoryName = new TextView(linearLayout.getContext());
        TableLayout table = new TableLayout(linearLayout.getContext());
        space.setMinimumHeight(25);
        linearLayout.addView(space);
        categoryName.setText(name);
        categoryName.setTextSize(23);
        categoryName.setTypeface(Typeface.DEFAULT, BOLD);
        linearLayout.addView(categoryName);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,0);
        lp.height = WRAP_CONTENT;
        lp.width = MATCH_PARENT;
        table.setId(View.generateViewId());
        table.setLayoutParams(lp);
        linearLayout.addView(table);
        Space space2 = new Space(linearLayout.getContext());
        space2.setMinimumHeight(25);
        linearLayout.addView(space2);
        return table.getId();
    }


    class Metric {

        public String title;
        public String data;

        Metric(String title, String data) {
            this.title = title;
            if (data == null || data.isEmpty()) {
                this.data = "--empty--";
            } else { this.data = data; }
        }
        Metric(String title, String data, String match, String if_match, String if_not_match) {
            /*
              Compare with match string. "<data>" can be used in conditional return, returns the data string conveniently
              if data==match
                if if_match == "<data>" --> data
                else --> if_match
              else (data != match)
                if if_not_match == "<data>" --> data
                else --> if_not_match
            */
            this.title = title;
            String tmp = "";
            if (Objects.equals(data, match)) {
                if (Objects.equals(if_match, "<data>")) {tmp = data;}
                else {tmp = if_match;}
            } else {
                if (Objects.equals(if_not_match, "<data>")) {tmp = data;}
                else {tmp = if_not_match;}
            }
            if (tmp.isEmpty()) {
                this.data = "--empty--";
            }
            else { this.data = tmp; }
        }
        Metric(String title, int data) {
            this.title = title;
            String tmp = Integer.toString(data);
            if (tmp.isEmpty()) {
                this.data = "--empty--";
            }
            else { this.data = tmp; }
        }
        Metric(String title, float data) {
            this.title = title;
            String tmp = Float.toString(data);
            if (tmp.isEmpty()) {
                this.data = "--empty--";
            }
            else { this.data = tmp; }
        }
        Metric(String title, double data) {
            this.title = title;
            String tmp = Double.toString(data);
            if (tmp.isEmpty()) {
                this.data = "--empty--";
            }
            else { this.data = tmp; }
        }
        Metric(String title, Boolean data) {
            this.title = title;
            String tmp = Boolean.toString(data);
            if (tmp.isEmpty()) {
                this.data = "--empty--";
            }
            else { this.data = tmp; }
        }
        Metric(String title, Boolean data, String if_true, String if_false) {
            // Boolean metric with custom text for the condition
            this.title = title;
            String tmp = Boolean.toString(data);
            if (!data.toString().isEmpty()) {
                if (data) {tmp = if_true;}
                else {tmp = if_false;}
            }
            if (tmp.isEmpty()) {
                this.data = "--empty--";
            }
            else { this.data = tmp; }
        }
    }
}
