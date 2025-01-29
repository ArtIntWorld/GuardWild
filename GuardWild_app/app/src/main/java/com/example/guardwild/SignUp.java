package com.example.guardwild;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    EditText ed_name, ed_password, ed_city, ed_email, ed_phone, ed_dob,ed_confirmpassword;
    RadioButton gen_male, gen_female, gen_other;
    Button bt_submit;
    ImageView bt_photo;
    String gender;
    String path, attach;
    byte[] byteArray = null;
    Spinner sp_district,sp_division,sp_station;
    ArrayAdapter<String> districtAdapter, divisionAdapter,stationAdapter;
    ArrayList<String> districtList, divisionList,stationList;
    ArrayList<Integer> divisionIdList,stationIdList;
    String districtsUrl, divisionsUrl,stationsUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ed_name = findViewById(R.id.editTextText4);
        ed_password = findViewById(R.id.editTextTextPassword);
        ed_confirmpassword = findViewById(R.id.editTextTextPassword2);
        ed_email = findViewById(R.id.editTextTextEmailAddress);
        ed_phone = findViewById(R.id.editTextPhone);
        gen_male = findViewById(R.id.radioButton8);
        gen_female = findViewById(R.id.radioButton9);
        gen_other = findViewById(R.id.radioButton10);
        bt_photo = findViewById(R.id.imageView);
        bt_submit = findViewById(R.id.button4);
        ed_city = findViewById(R.id.editTextText7);
        ed_dob = findViewById(R.id.editTextDate);

        sp_district = findViewById(R.id.district_spinner);
        sp_division = findViewById(R.id.division_spinner);
        sp_station = findViewById(R.id.station_spinner);

        districtList = new ArrayList<>();

        divisionList = new ArrayList<>();
        divisionIdList = new ArrayList<>();

        stationList = new ArrayList<>();
        stationIdList = new ArrayList<>();

        districtsUrl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", "") + "and_get_districts";
        divisionsUrl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", "") + "and_get_divisions";
        stationsUrl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("url", "") + "and_get_stations";

        districtList.add("Select District");
        divisionList.add("Select Division");
        stationList.add("Select Nearby Station");

        districtAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_district.setAdapter(districtAdapter);

        divisionAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, divisionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_division.setAdapter(divisionAdapter);

        stationAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, stationList);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_station.setAdapter(stationAdapter);
        
        loadDistrict();

        sp_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) { // Skip "Select District"
                    String selectedDistrict = districtList.get(position);
                    int districtId = position; // Assuming IDs start from 1
                    resetSpinner(divisionList, "Select Division");
                    resetSpinner(stationList, "Select Nearby Station");
                    loadDivision(districtId);
                } else {
                    resetSpinner(divisionList, "Select Division");
                    resetSpinner(stationList, "Select Nearby Station");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        sp_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) { // Skip "Select Division"
                    String selectedDivision = divisionList.get(position);
                    int divisionId = divisionIdList.get(position - 1); // Adjust index
                    resetSpinner(stationList, "Select Nearby Station");
                    loadStation(divisionId);
                } else {
                    resetSpinner(stationList, "Select Nearby Station");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        bt_submit.setOnClickListener(this);
        bt_photo.setOnClickListener(this);
        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
                    ed_dob.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void resetSpinner(ArrayList<String> spinnerList, String defaultValue) {
        spinnerList.clear();
        spinnerList.add(defaultValue);
        if (spinnerList == districtList) {
            districtAdapter.notifyDataSetChanged();
        } else if (spinnerList == divisionList) {
            divisionAdapter.notifyDataSetChanged();
        } else if (spinnerList == stationList) {
            stationAdapter.notifyDataSetChanged();
        }
    }

    private void loadDistrict() {
        StringRequest districtRequest = new StringRequest(Request.Method.POST, districtsUrl,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("ok")) {
                            JSONArray districts = obj.getJSONArray("data");
                            districtList.clear();
                            districtList.add("Select District");  // Add default placeholder
                            for (int i = 0; i < districts.length(); i++) {
                                JSONObject district = districts.getJSONObject(i);
                                String districtName = district.getString("district_name");
                                districtList.add(districtName);
                            }
                            districtAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error loading districts", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(districtRequest);
    }

    private void loadDivision(int districtId) {
        StringRequest divisionRequest = new StringRequest(Request.Method.POST, divisionsUrl,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("ok")) {
                            JSONArray divisions = obj.getJSONArray("data");
                            divisionList.clear();
                            divisionIdList.clear();
                            divisionList.add("Select Division");  // Add default placeholder
                            for (int i = 0; i < divisions.length(); i++) {
                                JSONObject division = divisions.getJSONObject(i);
                                String divisionName = division.getString("division_name");
                                int divisionId = division.getInt("division_id");
                                divisionList.add(divisionName);
                                divisionIdList.add(divisionId);
                            }
                            divisionAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error loading divisions", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("district_id", String.valueOf(districtId));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(divisionRequest);
    }

    private void loadStation(int divisionId) {
        StringRequest stationRequest = new StringRequest(Request.Method.POST, stationsUrl,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("ok")) {
                            JSONArray stations = obj.getJSONArray("data");
                            stationList.clear();
                            stationIdList.clear();
                            stationList.add("Select Nearby Station");  // Add default placeholder
                            for (int i = 0; i < stations.length(); i++) {
                                JSONObject station = stations.getJSONObject(i);
                                String stationName = station.getString("station_name");
                                int stationId = station.getInt("station_id");
                                stationList.add(stationName);
                                stationIdList.add(stationId);
                            }
                            stationAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error loading stations", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("division_id", String.valueOf(divisionId));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stationRequest);
    }



    @Override
    public void onClick(View v) {
        if (v == bt_submit) {
            String name = ed_name.getText().toString();
            String email = ed_email.getText().toString();
            String password = ed_password.getText().toString();
            String confirm_password = ed_confirmpassword.getText().toString();
            String phone = ed_phone.getText().toString();
            String dob = ed_dob.getText().toString();
            String city = ed_city.getText().toString();
            String district = sp_district.getSelectedItem().toString();
            String division = sp_division.getSelectedItem().toString();
            String station = sp_station.getSelectedItem().toString();

            if (gen_male.isChecked()) {
                gender = "Male";
            } else if (gen_female.isChecked()) {
                gender = "Female";
            } else if (gen_other.isChecked()) {
                gender = "Other";
            }

            if (name.isEmpty()) {
                ed_name.setError("Enter Your Name");
            } else if (email.isEmpty()) {
                ed_email.setError("Enter Your Email");
            } else if (phone.isEmpty()) {
                ed_phone.setError("Enter the Phone Number");
            } else if (city.isEmpty()) {
                ed_city.setError("Enter the Place");
            } else if (dob.isEmpty()) {
                ed_dob.setError("Enter the Date of Birth");
            } else if (password.isEmpty()) {
                ed_password.setError("Enter the Password");
            } else if (confirm_password.isEmpty()) {
                ed_confirmpassword.setError("Enter the Confirm Password");
            } else if (!password.equals(confirm_password)) {
                ed_confirmpassword.setError("Passwords do not match");
            } else if (district.equals("Select District")) {
                Toast.makeText(this, "Please select a valid district", Toast.LENGTH_SHORT).show();
            } else if (division.equals("Select Division")) {
                Toast.makeText(this, "Please select a valid division", Toast.LENGTH_SHORT).show();
            } else if (station.equals("Select Nearby Station")) {
                Toast.makeText(this, "Please select a valid station", Toast.LENGTH_SHORT).show();
            } else if (district.isEmpty() || district.equals("Select Division")) {
                Toast.makeText(this, "Please select a valid district", Toast.LENGTH_SHORT).show();
            }
            else {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url = sh.getString("url", "") + "and_user_register";

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {
                                    JSONObject obj = new JSONObject(new String(response.data));

                                    if (obj.getString("status").equals("ok")) {
                                        Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), Login.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
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
                        params.put("name",name);
                        params.put("email", email);
                        params.put("password", password);
                        params.put("city", city);
                        params.put("phone", phone);
                        params.put("gender", gender);
                        params.put("dob", dob);
                        params.put("photo", attach);
                        params.put("station",station);
                        return params;
                    }
                };

                Volley.newRequestQueue(this).add(volleyMultipartRequest);
            }
        } else{
            showFileChooser(1);
        }

    }
    void showFileChooser(int string) {         //       function to select image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), string);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override                                                      //    for showing the selected image where we select the image and bytearray
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                try {
                    path = FileUtils.getPath(this, uri);

                    File imgFile = new File(path);
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        // Apply Circle Transform
                        Bitmap circularBitmap = new CircleTransform().transform(myBitmap);

                        // Set the circular bitmap to the ImageView
                        bt_photo.setImageBitmap(circularBitmap);
                    }

                    // Convert file to byte array
                    File file = new File(path);
                    byte[] b = new byte[8192];
                    InputStream inputStream = new FileInputStream(file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int bytesRead;
                    while ((bytesRead = inputStream.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }
                    byteArray = bos.toByteArray();

                    // Base64 encoding of image for uploading as string (Optional)
                    attach = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}