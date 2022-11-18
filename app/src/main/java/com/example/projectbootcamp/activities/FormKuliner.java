package com.example.projectbootcamp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectbootcamp.R;
import com.example.projectbootcamp.enums.Apis;
import com.example.projectbootcamp.enums.Params;
import com.example.projectbootcamp.utils.GpsTracker;

import org.json.JSONException;
import org.json.JSONObject;

public class FormKuliner extends AppCompatActivity {

    EditText inputNama, inputKeterangan, inputKontributor, inputLatitude, inputLongtitude, inputStatus;
    Button btnPostData, btnShowMap;

    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    String latitude, longtitude;
    private GpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kuliner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputNama = findViewById(R.id.inputNama);
        inputKeterangan = findViewById(R.id.inputKeterangan);
        inputKontributor = findViewById(R.id.inputKontributor);
        inputLatitude = findViewById(R.id.inputLatitude);
        inputLongtitude = findViewById(R.id.inputLongtitude);
        inputStatus = findViewById(R.id.inputStatus);
        btnPostData = findViewById(R.id.btnSimpan);
        btnShowMap = findViewById(R.id.btnShowMap);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (ContextCompat.checkSelfPermission(FormKuliner.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FormKuliner.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE_LOCATION_PERMISSION);
        }

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FormKuliner.this);
        inputLatitude.setOnClickListener(view -> {
            getLocation(view);
        });
        btnPostData.setOnClickListener(view -> {
            String nama = inputNama.getText().toString();
            String keterangan = inputKeterangan.getText().toString();
            String kontributor = inputKontributor.getText().toString();
            String latitude = inputLatitude.getText().toString();
            String longtitude = inputLongtitude.getText().toString();
            String status = inputStatus.getText().toString();

            postData(nama, keterangan, kontributor, latitude, longtitude, status);
        });


        btnShowMap.setOnClickListener(view -> {
            String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longtitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
    }


//    @SuppressLint("NewApi")
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //get permisssion
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            }, 10);
//        } else {
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    Log.d("LOCATION ", "onSuccess: " + location);
//                    latitude = String.valueOf(location.getLatitude());
//                    longtitude = String.valueOf(location.getLongitude());
//                    inputLatitude.setText(latitude);
//                    inputLongtitude.setText(longtitude);
//                    Log.d("LATITUDE", "onSuccess: " + latitude);
//                    Log.d("LONGTITUDE", "onSuccess: " + longtitude);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setLoading() {
        progressDialog = new ProgressDialog(FormKuliner.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    public void postData(String nama, String keterangan, String kontributor, String latitude, String longtitude, String status) {
        setLoading();
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post(Apis.APIS_REQUEST_DATA)
                .addBodyParameter("aksi", "simpan")
                .addBodyParameter(Params.JSON_NAME, nama)
                .addBodyParameter(Params.JSON_KETERANGAN, keterangan)
                .addBodyParameter("kontributor", kontributor)
                .addBodyParameter("latitude", latitude)
                .addBodyParameter("longtitude", longtitude)
                .addBodyParameter("status", status)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        String statusPost = null;
                        try {
                            statusPost = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (statusPost.equals("success")) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    public void getLocation(View view) {
        gpsTracker = new GpsTracker(FormKuliner.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            inputLatitude.setText(String.valueOf(latitude));
            inputLongtitude.setText(String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
}