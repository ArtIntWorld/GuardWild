package com.example.guardwild;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ViewComplaintReply extends AppCompatActivity {
    ListView li;
    String[] complaint,reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint_reply);
        li=findViewById(R.id.listView);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sh.getString("url", "") + "and_user_view_complaint";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                        JSONArray js = jsonObj.getJSONArray("data");//from python
                        complaint = new String[js.length()];
                        reply = new String[js.length()];

                        for (int i = 0; i < js.length(); i++) {
                            JSONObject u = js.getJSONObject(i);
                            complaint[i] = u.getString("complaint");
                            reply[i] = u.getString("reply");
                        }
                        li.setAdapter(new CustomViewComplaint(getApplicationContext(), complaint, reply));

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