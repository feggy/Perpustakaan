package com.spesialiskp.perpustakaan.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Adapter.PeminjamanAdapter;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.Dialog.FilterDialog;
import com.spesialiskp.perpustakaan.Models.Peminjaman;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaksiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CardView fab;
    JSONArray jsonArray;
    JSONObject data;
    String start, end;
    LinearLayout vCaution;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab_transaksi);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fabTransaksi);
        vCaution = findViewById(R.id.vCaution);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    if (fab.isShown()) fab.setVisibility(View.GONE);
                } else {
                    if (!fab.isShown()) fab.setVisibility(View.VISIBLE);
                }
            }
        });

        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");

        tampilData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.show(getSupportFragmentManager(), "FILTER_DIALOG");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void tampilData() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PEMINJAMAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList arrayList = new ArrayList();
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("peminjaman");

                            if (jsonArray.length() > 0){
                                progressBar.setVisibility(View.GONE);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    data = jsonArray.getJSONObject(i);

                                    String kode = data.getString("kode_pinjam");
                                    String nama = data.getString("nama");
                                    String buku = data.getString("judul_buku");
                                    String tgl_pinjam = data.getString("tgl_pinjam");
                                    String tgl_kembali = data.getString("tgl_kembali");
                                    String denda = data.getString("denda");
                                    String status = data.getString("status");

                                    Peminjaman peminjaman = new Peminjaman(kode, nama, buku, tgl_pinjam, tgl_kembali, denda, status);
                                    arrayList.add(peminjaman);
                                }
                                PeminjamanAdapter peminjamanAdapter = new PeminjamanAdapter(arrayList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(peminjamanAdapter);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                vCaution.setVisibility(View.VISIBLE);
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("start", start);
                params.put("end", end);

                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
