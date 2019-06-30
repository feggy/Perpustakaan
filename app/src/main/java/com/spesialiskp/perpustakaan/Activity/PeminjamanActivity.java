package com.spesialiskp.perpustakaan.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PeminjamanActivity extends AppCompatActivity {

    EditText etKodeBuku, etJudulBuku, etIdAnggota, etKodePinjam;
    String kodePinjam;
    String postKodeBuku, postIdAnggota;
    Button btnSubmit;
    RelativeLayout vKodeBuku;
    LinearLayout lny1, lny2;
    private int RCode = 111;
    int banyakTransaksi;
    ProgressBar progressBar;

    String kodeBuku = "";
    String idAnggota = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);

        init();
        jumlahBuku();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            kodeBuku = bundle.getString("hasil_scan");
            if (kodeBuku != null){
                cekJudulBuku();
            } else {
                kodeBuku = bundle.getString("kode_buku");
                if (kodeBuku != null) {
                    cekJudulBuku();
                }
            }

            idAnggota = bundle.getString("hasil_scan2");
            if (idAnggota != null){
                cekAnggota();
            }
            Log.e("cekBundle", kodeBuku + " " + idAnggota);
        }
    }

    private void init() {
        initView();
        initUI();
    }

    private void initUI() {

        etKodeBuku.setText(kodeBuku);

        etKodeBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(PeminjamanActivity.this, new String[] {Manifest.permission.CAMERA}, RCode);
                } else {
                    startActivity(new Intent(PeminjamanActivity.this, ScanBarcodeActivity.class));
                }
            }
        });

        etIdAnggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(PeminjamanActivity.this, new String[] {Manifest.permission.CAMERA}, RCode);
                } else {
                    Intent i = new Intent(PeminjamanActivity.this, ScanBarcode4Activity.class);
                    i.putExtra("kode_buku", kodeBuku);
                    startActivity(i);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    private void initView() {
        etKodeBuku = findViewById(R.id.etKodeBuku);
        etJudulBuku = findViewById(R.id.etJudulBuku);
        etIdAnggota = findViewById(R.id.etIdAnggota);
        btnSubmit = findViewById(R.id.btnSubmit);
        vKodeBuku = findViewById(R.id.vKodebuku);
        etKodePinjam = findViewById(R.id.etKodePinjam);
        lny1 = findViewById(R.id.lny1);
        lny2 = findViewById(R.id.lny2);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(PeminjamanActivity.this, ScanBarcodeActivity.class));
        }
    }

    private void jumlahBuku() {
        cekKondisi(true);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_PEMINJAMAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("peminjaman");
                            Log.e("peminjaman", jsonArray.toString());
                            banyakTransaksi = jsonArray.length();
                            if (banyakTransaksi < 10){
                                kodePinjam = "TR10100"+Integer.toString(banyakTransaksi+1);
                            } else {
                                kodePinjam = "TR1010"+Integer.toString(banyakTransaksi+1);
                            }
                            etKodePinjam.setText(kodePinjam);
                            Log.e("Kode Pinjam",  ""+kodePinjam);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal menambah data", Toast.LENGTH_LONG).show();
                        }
                        cekKondisi(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void cekJudulBuku() {
        cekKondisi(true);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_TRANSAKSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String judul_buku = jsonObject.getString("judul_buku");
                            System.out.println(judul_buku);
                            if (!judul_buku.equals("null")){
                                etKodeBuku.setText(kodeBuku);
                                etJudulBuku.setText(jsonObject.getString("judul_buku"));
                                lny2.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Buku tidak ditemukan", Toast.LENGTH_LONG).show();
                                etJudulBuku.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cekKondisi(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("kode_buku", kodeBuku);

                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void cekAnggota(){
        cekKondisi(true);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_ANGGOTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("anggota");
                            Log.e("cekAnggota", jsonArray.toString());

                            etIdAnggota.setText(idAnggota);

                            if (jsonArray.length() == 0){
                                AlertDialog.Builder builder = new AlertDialog.Builder(PeminjamanActivity.this)
                                        .setTitle("Oops")
                                        .setMessage("Id anggota tidak terdaftar")
                                        .setPositiveButton("OKE", null);
                                builder.show();
                            } else {
                                btnSubmit.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cekKondisi(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_anggota", idAnggota);

                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void simpanData() {
        postKodeBuku = etKodeBuku.getText().toString().trim();
        postIdAnggota = etIdAnggota.getText().toString().trim();

        cekKondisi(true);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_TRANSAKSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("kode").equals("0")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                        cekKondisi(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("kode_pinjam", kodePinjam);
                params.put("id_anggota", postIdAnggota);
                params.put("kode_buku", postKodeBuku);

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void cekKondisi(boolean b){
        if (b == true){
            progressBar.setVisibility(View.VISIBLE);
            lny1.setVisibility(View.GONE);
        } else if (b == false) {
            progressBar.setVisibility(View.GONE);
            lny1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
