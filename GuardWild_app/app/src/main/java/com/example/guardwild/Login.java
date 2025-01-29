package com.example.guardwild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText ed_email,ed_password;
    Button login,signup;
    //    TextView ;
    public static String lid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_email = findViewById(R.id.editTextText2);
        ed_password = findViewById(R.id.editTextText3);
        login = findViewById(R.id.button2);
        signup = findViewById(R.id.button6);

        login.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == login){
            String email = ed_email.getText().toString();
            String password = ed_password.getText().toString();

            if (email.isEmpty()){
                ed_email.setError("Enter your email");
            }
            else if (password.isEmpty()){
                ed_password.setError("Enter your password");
            }
            else{
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url= sh.getString("url","")+"and_login";
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {


                                    JSONObject obj = new JSONObject(new String(response.data));

                                    if (obj.getString("status").equals("ok")) {
                                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


                                        String usertype = obj.getString("user_type");
                                        lid = obj.getString("lid");
                                        if ("user".equals((usertype))) {
                                            Toast.makeText(getApplicationContext(), "User login successfully", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor ed = sh.edit();
                                            ed.putString("lid", obj.getString("lid"));
                                            ed.putString("uid", obj.getString("uid"));
                                            ed.commit();


                                            Intent j = new Intent(getApplicationContext(),UserHome.class);
                                            startActivity(j);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Handle the case where status is not "ok"
                                        Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        params.put("email", email);//passing to python
                        params.put("password", password);//passing to python
                        return params;
                    }


                };

                Volley.newRequestQueue(this).add(volleyMultipartRequest);

            }
        }
        else{
            Intent i = new Intent(getApplicationContext(), SignUp.class);
            startActivity(i);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ij=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(ij);
    }
}