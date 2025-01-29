package com.example.guardwild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    TextView name,email,contact,city,dob,gender,district,password;
    ImageView profile,editprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name = findViewById(R.id.textView17);
        email = findViewById(R.id.textView19);
        contact = findViewById(R.id.textView21);
        city = findViewById(R.id.textView23);
        dob = findViewById(R.id.textView25);
        gender = findViewById(R.id.textView27);
        district = findViewById(R.id.textView31);
        password = findViewById(R.id.textView29);
        profile = findViewById(R.id.imageView3);
        editprofile = findViewById(R.id.imageView5);
        editprofile.setOnClickListener(this);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sh.getString("url", "") + "and_user_profile";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("ok")){
                        JSONArray js=obj.getJSONArray("data");
                        JSONObject js1=js.getJSONObject(0);
                        name.setText(js1.getString("name"));
                        email.setText(js1.getString("email"));
                        contact.setText(js1.getString("phone"));
                        city.setText(js1.getString("city"));
                        dob.setText(js1.getString("dob"));
                        gender.setText(js1.getString("gender"));
                        district.setText(js1.getString("district"));
                        password.setText(js1.getString("password"));
                        // To show image
                        String fullUrl = PreferenceManager.getDefaultSharedPreferences(UserProfile.this).getString("iurl", "") + js1.getString("photo");
                        Picasso.with(UserProfile.this).load(fullUrl).transform(new CircleTransform()).into(profile);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Invalid user" ,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            // value Passing android to python
            @Override
            protected Map<String, String> getParams() {
                //   SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String,String>params=new HashMap<String,String>();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("uid", sh.getString("uid",""));//passing to python

                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == editprofile) {
            Intent i = new Intent(getApplicationContext(), EditProfile.class);
            startActivity(i);
        }
    }
}