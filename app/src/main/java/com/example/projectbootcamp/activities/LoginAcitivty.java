package com.example.projectbootcamp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.projectbootcamp.interfaces.RetrofitApiLogin;
import com.example.projectbootcamp.utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginAcitivty extends AppCompatActivity {
    public static final String API_LOGIN = "http://192.3.168.178/flutter/";
    //initialize SharedPreferences

    SharedPrefManager sharedPrefManager;

    Context mContext;

    EditText inputEmail, inputPassword;
    Button btnLogin;
    ImageView btnShowPassword;
    TextView textName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sharedPrefManager = new SharedPrefManager(this);

        textName = findViewById(R.id.header_name);
        inputEmail = findViewById(R.id.et_email);
        inputPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        ImageView redirectToRegister = (ImageView) findViewById(R.id.imgSwipeRight);
        redirectToRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {

            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                checkAllField();
                showSnackbar(view, "Email & Password is Required", Snackbar.LENGTH_SHORT);
            } else {
                fastNetWorkingPostDataLogin(email, password);
            }
        });

        if (sharedPrefManager.getSPStatusLogin()) {
            startActivity(new Intent(LoginAcitivty.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    private boolean checkAllField() {
        boolean emailIsValid, passwordIsValid;
        emailIsValid = false;
        passwordIsValid = false;
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.isEmpty()) {
            inputEmail.setError("Email is Required");
            emailIsValid = false;
        } else {
            Log.d("Email", email);
            emailIsValid = true;
        }
        if (password.isEmpty()) {
            inputPassword.setError("Password is Required");
            passwordIsValid = false;
        } else {
            Log.d("Password", password);
            passwordIsValid = true;
        }
        return emailIsValid && passwordIsValid;
    }

    private void showSnackbar(View view, String message, int duration) {

        final Snackbar snackBar = Snackbar.make(view, message, duration);
        snackBar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    public class getMessage {
        String status = null;
        String message = null;
    }

    private class SendLoginData extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginAcitivty.this);
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String email = strings[0];
            String password = strings[1];
            AndroidNetworking.post(Apis.API_LOGIN)
                    .addBodyParameter(Params.JSON_EMAIL, email)
                    .addBodyParameter(Params.JSON_PASSWORD, password)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            onPostExecute(jsonObject);
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d("cek error", "onError: Failed" + error); //untuk log pada onerror
                        }
                    });
            JSONObject jsonObject = new JSONObject();
            return jsonObject;
        }

        ;

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (progressDialog.isShowing()) progressDialog.dismiss();
            Log.d("message", "cek response" + jsonObject);

            String status = null;
            String message = Params.RESPONSE_MESSAGE;

            try {
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean setLoading() {
        progressDialog = new ProgressDialog(LoginAcitivty.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return false;
    }

    ;

    private void fastNetWorkingPostDataLogin(String email, String password) {
        setLoading();
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post(Apis.API_LOGIN)
                .addBodyParameter(Params.JSON_EMAIL, email)
                .addBodyParameter(Params.JSON_PASSWORD, password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cek response", "onResponse : " + response);
                        String status = Params.RESPONSE_STATUS;
                        String message = Params.RESPONSE_MESSAGE;
                        try {
                            status = response.getString("status");
                            message = response.getString("message");
                            JSONObject data = response.getJSONObject("data");
                            String nama = data.getString("nama");
                            String email = data.getString("email");
                            String image = data.getString("foto");

                            Log.d("cek nama", "nama : " + nama);
                            Log.d("cek email", "image : " + email);
                            Log.d("cek image", "image : " + image);

                            if (status.equals("success")) {
                                Log.d("STATUS LOGIN: ", "LOGIN SUCCESS");
                                // sharedPrefManager set data and isLogin
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_IMAGE, image);
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_STATUS_LOGIN, true);

                                startActivity(new Intent(LoginAcitivty.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                                // passsing data with intent
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                intent.putExtra("nama", nama);
//                                intent.putExtra("email", email);
//                                intent.putExtra("foto", image);
//                                startActivity(intent);
                            } else {
                                Log.d("STATUS LOGIN: ", "LOGIN FAILED");
                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(), (CharSequence) error, Toast.LENGTH_SHORT).show();
                        Log.e("cek error", "onError: Failed" + error); //untuk log pada onerror
                    }
                });
    }

    private void retrofitPostDataLogin(String email, String password) {
        progressDialog = new ProgressDialog(LoginAcitivty.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_LOGIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitApiLogin retrofitApiLogin = retrofit.create(RetrofitApiLogin.class);
        Call<String> call = retrofitApiLogin.STRING_CALL(email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = Params.RESPONSE_STATUS;
                String message = Params.RESPONSE_MESSAGE;
                if (response.isSuccessful() && response.body() != null) {
                    progressDialog.dismiss();
                    try {
                        JSONObject data = new JSONObject(response.body());
                        Log.d("data login", "onResponse" + data);
                        status = data.getString("status");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (status.equals("success")) {
                        Log.d("STATUS LOGIN : ", "Login success");
                    } else {
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

    private void volleyPostDataLogin(String email, String password) {
        setLoading();
        RequestQueue queue = Volley.newRequestQueue(LoginAcitivty.this);
        String url = Apis.API_LOGIN;

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
                        if (status.equals("success")) {
                            Log.d("STATUS LOGIN : ", "Login success");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("STATUS LOGIN : ", "Login failed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Params.JSON_EMAIL, email);
                params.put(Params.JSON_PASSWORD, password);
                return params;
            }
        };
        queue.add(postRequest);
    }
}