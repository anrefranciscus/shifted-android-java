package com.example.projectbootcamp.activities;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectbootcamp.R;
import com.example.projectbootcamp.enums.Apis;
import com.example.projectbootcamp.enums.Params;
import com.example.projectbootcamp.interfaces.RetrofitApiRegister;
import com.example.projectbootcamp.utils.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName, email, password, reTypePassword;
    Button btnRegister;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView redirectToLogin = (ImageView) findViewById(R.id.imgSwipeLeft);
        fullName = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        reTypePassword = findViewById(R.id.et_repassword);
        btnRegister = findViewById(R.id.btn_register);
        redirectToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginAcitivty.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(view -> {
            String inputName = fullName.getText().toString();
            String inputEmail = email.getText().toString();
            String inputPassword = password.getText().toString();

            Log.d("cek input", Params.JSON_NAME + inputName);
            Log.d("cek input", "email" + inputEmail);
            Log.d("cek input", "password" + inputPassword);

            if (inputName.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty()){
                checkAllField();
                Log.d("Data harus diisi semua", "harus diisi");
            }else {
                onCreateDialog();
//                new SendRegisterData().execute(inputName,inputEmail, inputPassword);
            }

        });
    }

    private boolean checkAllField(){
        String inputName = fullName.getText().toString();
        String inputEmail = email.getText().toString();
        String inputPassword = password.getText().toString();
        String inputReTypePassword = reTypePassword.getText().toString();

        boolean isValidName, isValidEmail, isValidPassword, isValidReTypePassword;
        if(inputEmail.isEmpty()){
            fullName.setError("Fullname is Requried");
            isValidName = false;
        }else {
            Log.d("Fullname :", inputName);
            isValidName = true;
        }
        if(inputEmail.isEmpty()){
            email.setError("Email is Required");
            isValidEmail = false;
        }else {
            Log.d("Email    :", inputEmail);
            isValidEmail = true;
        }
        if(inputPassword.isEmpty()){
            password.setError("Password is required");
            isValidPassword = false;
        }else {
            Log.d("Password :", inputPassword);
            isValidPassword = true;
        }

        if(inputReTypePassword.isEmpty()){
            reTypePassword.setError("Re Type Password is Required");
            isValidReTypePassword = false;
        }else {
            Log.d("Password    :", inputReTypePassword);
            isValidReTypePassword = true;
        }

        if(!inputPassword.equals(inputReTypePassword)){
            password.setError("Tidak sama dengan retype password");
            reTypePassword.setError("Tidak sama dengan Password");
            isValidPassword = false;
            isValidReTypePassword = false;
        }else {
            isValidPassword = true;
            isValidReTypePassword = true;
        }

        return isValidName && isValidEmail && isValidPassword && isValidReTypePassword;
    }

    public void onCreateDialog(){
        String inputName = fullName.getText().toString();
        String inputEmail = email.getText().toString();
        String inputPassword = password.getText().toString();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);

        alertDialogBuilder.setTitle("Register");

        alertDialogBuilder
                .setMessage("Apakah datamu sudah benar")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
//                        new SendRegisterData().execute(inputName,inputEmail, inputPassword);
//                        RegisterData(inputName, inputEmail, inputPassword);
          //              volleyPostDataRegister(inputName, inputEmail, inputPassword);
                        retrofitPostDataRegister(inputName, inputEmail, inputPassword);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    //POST REGISTER
    private class SendRegisterData extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog =  new ProgressDialog(RegisterActivity.this);
            if(progressDialog.isShowing()) progressDialog.dismiss();
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            String nama = strings[0];
            String email = strings[1];
            String password = strings[2];

            Log.d("cek input", Params.JSON_NAME + nama);
            Log.d("cek input", "email" + email);
            Log.d("cek input", "password" + password);

            JSONObject jsonObject = null;
            String input = Apis.API_REGISTER +
                    "?" + "nama=" + nama + "&" +"email=" + email + "&" + "password=" + password;
            String responseJSON = Network.getJSON(input);

            Log.d("RAW RESPONSE ", "response-json : " + responseJSON);

            Log.d("cek input", "url : " + input);

            try {
                jsonObject = new JSONObject(responseJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(progressDialog.isShowing()) progressDialog.dismiss();
            Log.d("message", "cek response" + jsonObject);

            String status = null;
            String message = "";

            try {
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");
            }catch (JSONException e){
                e.printStackTrace();
            }

            if (status.equals("success")){
                Log.d("STATUS REGISTER: ", "REGISTER SUCCESS");
            }else {
                Log.d("STATUS REGISTER: ", "REGISTER FAILED");
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean setLoading(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return false;
    };

    private void RegisterData(String nama, String email, String password) {
        setLoading();
        AndroidNetworking.post(Apis.API_REGISTER)
                .addBodyParameter(Params.JSON_NAME, nama)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cek response ", "onResponse : " + response);
                        String status = null;
                        String message = "";
                        try {
                            status = response.getString("status");
                            message = response.getString("message");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        if (status.equals("success")){
                            Log.d("STATUS REGISTER: ", "REGISTER SUCCESS");
                        }else {
                            Log.d("STATUS REGISTER: ", "REGISTER SUCCESS");
                        }
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), (CharSequence) error, Toast.LENGTH_SHORT).show();
                        Log.d("cek error", "onError: Failed" + error); //untuk log pada onerror
                    }
                });
    }


    private void volleyPostDataRegister(String nama, String email, String password){
        setLoading();
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = Apis.API_REGISTER;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        Log.d("cek status", "isi response" + response);
                        Log.d("cek status", "isi response" + message);

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")){
                            Log.d("STATUS LOGIN : ", "Login success");
                        }else {
                            Log.d("STATUS LOGIN : ", "Login failed");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },error -> Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(Params.JSON_NAME, nama);
                params.put(Params.JSON_EMAIL, email);
                params.put(Params.JSON_PASSWORD, password);
                return params;
            }
        };
        queue.add(postRequest);
    }



    private void retrofitPostDataRegister(String nama, String email, String password){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.3.168.178/flutter/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitApiRegister retrofitApiLogin = retrofit.create(RetrofitApiRegister.class);
        Call<String> call = retrofitApiLogin.STRING_CALL(nama, email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = Params.RESPONSE_MESSAGE;
                String message = Params.RESPONSE_MESSAGE;
                if(response.isSuccessful() && response.body() != null){
                    progressDialog.dismiss();
                    try {
                        JSONObject data = new JSONObject(response.body());
                        Log.d("data login", "onResponse" + data);
                        status = data.getString("status");
                        message = data.getString("message");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (status.equals("success")){
                        Log.d("STATUS LOGIN : ", "Login success");
                    }else {
                        Log.d("STATUS LOGIN : ", "Login failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("error", "onFailure" + t.getMessage());
            }
        });
    }

}