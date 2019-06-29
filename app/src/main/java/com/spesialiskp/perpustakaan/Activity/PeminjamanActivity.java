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
    String hasilScan, kodePinjam;
    String postKodeBuku, postIdAnggota;
    Button btnSubmit;
    RelativeLayout vKodeBuku;
    private int RCode = 111;
    int banyakTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);

        etKodeBuku = findViewById(R.id.etKodeBuku);
        etJudulBuku = findViewById(R.id.etJudulBuku);
        etIdAnggota = findViewById(R.id.etIdAnggota);
        btnSubmit = findViewById(R.id.btnSubmit);
        vKodeBuku = findViewById(R.id.vKodebuku);
        etKodePinjam = findViewById(R.id.etKodePinjam);

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
                    startActivity(new Intent(PeminjamanActivity.this, ScanBarcode4Activity.class));
                }
            }
        });

        jumlahBuku();
        cekJudulBuku();
        cekAnggota();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
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

    private void cekAnggota(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String idAnggota = bundle.getString("hasil_scan2");
            etIdAnggota.setText(idAnggota);

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_LIHAT_ANGGOTA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("anggota");

                                if (jsonArray.length() == 0){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PeminjamanActivity.this)
                                            .setTitle("Oops")
                                            .setMessage("Id anggota tidak terdaftar")
                                            .setPositiveButton("OKE", null);
                                    builder.show();
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
    }

    private void cekJudulBuku() {
        Bundle bundle = getIntent().getExtras();
        if (getIntent().getExtras() != null){
            hasilScan = bundle.getString("hasil_scan");
            etKodeBuku.setText(hasilScan);

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
                                    etJudulBuku.setText(jsonObject.getString("judul_buku"));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Buku tidak ditemukan", Toast.LENGTH_LONG).show();
                                    etJudulBuku.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

                    params.put("kode_buku", hasilScan);

                    return params;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

//            if (!hasilScan.equals("")) {
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Harap isi terlebih dahulu kode buku untuk menampilkan judul buku", Toast.LENGTH_LONG).show();
//            }
        }
    }

    private void simpanData() {
        postKodeBuku = etKodeBuku.getText().toString().trim();
        postIdAnggota = etIdAnggota.getText().toString().trim();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
