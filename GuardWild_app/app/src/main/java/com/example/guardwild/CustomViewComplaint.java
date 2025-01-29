package com.example.guardwild;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomViewComplaint extends BaseAdapter {
    String[] complaint, reply;
    private final Context context;

    public CustomViewComplaint(Context applicationContext, String[] complaint, String[] reply) {
        this.context = applicationContext;
        this.complaint = complaint;
        this.reply = reply;

    }

    @Override
    public int getCount() {
        return complaint.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            gridView = inflator.inflate(R.layout.activity_custom_view_complaint, null);
        } else {
            gridView = (View) view;
        }

        TextView tv1 = (TextView) gridView.findViewById(R.id.textView12);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView14);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
        tv1.setText(complaint[i]);
        tv2.setText(reply[i]);
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);

        return gridView;
    }
}