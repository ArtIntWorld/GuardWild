package com.example.guardwild;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnimalList extends AppCompatActivity {
    ListView li;
    String[] name,description,endangered,risk,type,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);
        li=findViewById(R.id.listAnimal);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sh.getString("url", "") + "and_user_animal_list";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                        JSONArray js = jsonObj.getJSONArray("data");//from python
                        name = new String[js.length()];
                        description = new String[js.length()];
                        endangered = new String[js.length()];
                        risk = new String[js.length()];
                        type = new String[js.length()];
                        photo = new String[js.length()];

                        for (int i = 0; i < js.length(); i++) {
                            JSONObject u = js.getJSONObject(i);
                            name[i] = u.getString("name");
                            description[i] = u.getString("description");
                            endangered[i] = u.getString("endangered");
                            risk[i] = u.getString("risk");
                            type[i] = u.getString("type");
                            photo[i] = u.getString("photo");
                        }
                        li.setAdapter(new CustomAnimalList(getApplicationContext(), name, description, endangered, risk, type, photo));

                    } else {
                        Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
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
}