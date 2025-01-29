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

public class CustomStationDetails extends BaseAdapter {
    String[] name, email, phone;
    private final Context context;

    public CustomStationDetails(Context applicationContext, String[] name, String[] phone, String[] email) {
        this.context = applicationContext;
        this.name = name;
        this.phone = phone;
        this.email = email;

    }

    @Override
    public int getCount() {
        return name.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_station_details, null);
        } else {
            gridView = (View) view;
        }

        TextView tv1 = (TextView) gridView.findViewById(R.id.textView38);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView40);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView42);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
        tv3.setTextColor(Color.WHITE);
        tv1.setText(name[i]);
        tv2.setText(email[i]);
        tv3.setText(phone[i]);
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);

        return gridView;
    }
}