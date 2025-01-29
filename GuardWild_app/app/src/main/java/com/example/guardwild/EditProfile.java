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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    EditText ed_name, ed_password, ed_city, ed_email, ed_phone, ed_dob, ed_confirmpassword;
    ImageView bt_photo;
    String gender;
    Button bt_update;
    String path, attach;
    byte[] byteArray = null;
    Spinner sp_district,sp_division,sp_station;
    ArrayAdapter<String> districtAdapter, divisionAdapter,stationAdapter;
    ArrayList<String> districtList, divisionList,stationList;
    ArrayList<Integer> divisionIdList,stationIdList;
    String districtsUrl, divisionsUrl,stationsUrl;
    RadioButton gen_male, gen_female, gen_other;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ed_name = findViewById(R.id.editTextText4);
        ed_phone = findViewById(R.id.editTextPhone);
        ed_password = findViewById(R.id.editTextTextPassword);
        ed_confirmpassword = findViewById(R.id.editTextTextPassword2);
        ed_email = findViewById(R.id.editTextTextEmailAddress);
        bt_update = findViewById(R.id.button4);
        ed_city = findViewById(R.id.editTextText7);
        ed_dob = findViewById(R.id.editTextDate);
        bt_photo = findViewById(R.id.imageView);
        gen_male = findViewById(R.id.radioButton8);
        gen_female = findViewById(R.id.radioButton9);
        gen_other = findViewById(R.id.radioButton10);
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



        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sh.getString("url", "") + "and_get_profile";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equals("ok")) {
                        JSONArray js = obj.getJSONArray("data");
                        JSONObject js1 = js.getJSONObject(0);
                        ed_name.setText(js1.getString("name"));
                        ed_email.setText(js1.getString("email"));
                        ed_phone.setText(js1.getString("phone"));
                        ed_city.setText(js1.getString("city"));
                        ed_dob.setText(js1.getString("dob"));
                        String district=js1.getString("district");
                        String division=js1.getString("division");
                        String station=js1.getString("station");
                        selectDistrict(district);

                        ed_password.setText(js1.getString("password"));

                        if (gender.equals("Male")) gen_male.setChecked(true);
                        else if (gender.equals("Female")) gen_female.setChecked(true);
                        else gen_other.setChecked(true);


                        String fullUrl = PreferenceManager.getDefaultSharedPreferences(EditProfile.this).getString("iurl", "") + js1.getString("photo");
                        Picasso.with(EditProfile.this).load(fullUrl).transform(new CircleTransform()).into(bt_photo);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("uid", sh.getString("uid", ""));//passing to python

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

    private void selectDistrict(String districtName) {
        int index = districtList.indexOf(districtName);

        // Check if the district name exists
        if (index != -1) {
            // Select the district in the Spinner
            sp_district.setSelection(index);
        } else {
            // Handle case where the district is not found
            Toast.makeText(getApplicationContext(), "District not found: " + districtName, Toast.LENGTH_SHORT).show();
        }
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

//    private void selectDivision(String districtName, String divisionName) {
//        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String durl = sh.getString("url", "") + "and_get_divisions_by_name";
//        loadDivision();
//        int index = divisionList.indexOf(divisionName);
//
//        // Check if the district name exists
//        if (index != -1) {
//            // Select the district in the Spinner
//            sp_division.setSelection(index);
//        } else {
//            // Handle case where the district is not found
//            Toast.makeText(getApplicationContext(), "Division not found: " + divisionName, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void selectStation(String divisionName, String stationName) {
//
//        int index = stationList.indexOf(stationName);
//
//        // Check if the district name exists
//        if (index != -1) {
//            // Select the district in the Spinner
//            sp_station.setSelection(index);
//        } else {
//            // Handle case where the district is not found
//            Toast.makeText(getApplicationContext(), "Station not found: " + stationName, Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onClick(View v) {
        if (v == bt_update) {

        }
        else {
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