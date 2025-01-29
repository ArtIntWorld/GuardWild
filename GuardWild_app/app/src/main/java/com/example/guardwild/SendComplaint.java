package com.example.guardwild;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendComplaint extends AppCompatActivity implements View.OnClickListener {
    EditText ed_complaint;
    Button send;
    ImageView img_complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_complaint);
        ed_complaint = findViewById(R.id.editTextTextMultiLine);
        send = findViewById(R.id.button5);
        img_complaint = findViewById(R.id.imageView2);
        send.setOnClickListener(this);
        img_complaint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            String complaint = ed_complaint.getText().toString();
            if (complaint.isEmpty()) {
                ed_complaint.setError("Enter the Complaint");
            } else {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url = sh.getString("url", "") + "and_user_complaint";
                String uid = sh.getString("uid", "");

                if (uid.isEmpty()) {
                    // Use the uid for your logic, e.g., displaying user-specific information
                    Toast.makeText(getApplicationContext(), "User ID not found", Toast.LENGTH_SHORT).show();
                } else {
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    try {
                                        JSONObject obj = new JSONObject(new String(response.data));

                                        if (obj.getString("status").equals("ok")) {
                                            Toast.makeText(getApplicationContext(), "Complaint Registration successful", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), ViewComplaintReply.class);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Complaint Registration failed", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("complaint", complaint);
                            params.put("uid", uid);
                            params.put("status", "pending");
                            return params;
                        }
                    };

                    Volley.newRequestQueue(this).add(volleyMultipartRequest);
                }
            }
        } else if (v == img_complaint) {
            Intent i = new Intent(getApplicationContext(), ViewComplaintReply.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(getApplicationContext(), UserHome.class);
            startActivity(i);
        }
    }
}