package com.example.guardwild;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class CustomAnimalList extends BaseAdapter {
    String[] name,description,endangered,risk,type,photo;
    private final Context context;

    public CustomAnimalList(Context applicationContext, String[] name, String[] description, String[] endangered, String[] risk, String[] type,String[] photo) {
        this.context = applicationContext;
        this.name = name;
        this.description = description;
        this.endangered = endangered;
        this.risk = risk;
        this.type = type;
        this.photo = photo;

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
            gridView = inflator.inflate(R.layout.activity_custom_animal_list, null);
        } else {
            gridView = (View) view;
        }

        TextView tv1 = (TextView) gridView.findViewById(R.id.textView32);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView33);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView34);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView35);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView36);
        ImageView animal = (ImageView) gridView.findViewById(R.id.imageView4);
        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
        tv3.setTextColor(Color.WHITE);
        tv4.setTextColor(Color.WHITE);
        tv5.setTextColor(Color.WHITE);

        tv1.setText(name[i]);
        tv2.setText(description[i]);
        tv3.setText(type[i]);
        tv4.setText(risk[i]);
        tv5.setText(endangered[i]);

        String fullUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("iurl", "") + photo[i];
        Picasso.with(context).load(fullUrl).fit().centerCrop().into(animal);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);

        return gridView;
    }
}