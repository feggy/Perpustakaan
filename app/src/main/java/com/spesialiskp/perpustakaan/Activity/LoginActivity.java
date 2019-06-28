package com.spesialiskp.perpustakaan.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;
import com.spesialiskp.perpustakaan.Support.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnLogin;
    ProgressDialog progressDialog;
    String username, password;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        int ALL_PERMISSIONS = 101;
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);

        if (SharedPrefManager.getInstance(this).isLogin()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        etUser = findViewById(R.id.etIdAnggota);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        username = etUser.getText().toString().trim();
        password = etPass.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.setMessage("Mohon tunggu...");
                            progressDialog.show();
                            JSONObject jsonObject = new JSONObject(response);
                            if (!username.equals("") || !password.equals("")){
                                if (jsonObject.getString("kode").equals("1")) {
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    jsonObject.getString("username"),
                                                    jsonObject.getString("password"),
                                                    jsonObject.getString("nama"),
                                                    jsonObject.getString("level"),
                                                    jsonObject.getString("no_hp"),
                                                    jsonObject.getString("alamat"),
                                                    jsonObject.getString("foto")
                                            );
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                //jika koneksi tidak ada untuk melakukan post
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
