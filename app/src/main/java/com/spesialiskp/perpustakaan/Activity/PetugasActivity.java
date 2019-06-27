package com.spesialiskp.perpustakaan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.Models.Petugas;
import com.spesialiskp.perpustakaan.Adapter.PetugasAdapter;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Support.RecyclerItemClickListener;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PetugasActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    String nama, nomor, username, password, level, no_hp, alamat, foto;
    ArrayList<Petugas> petugasArrayList;
    PetugasAdapter petugasAdapter;
    String[] item ={"Ubah", "Hapus"};
    JSONObject data;
    JSONArray jsonArray;
    LinearLayout vCaution;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        vCaution = findViewById(R.id.vCaution);

        fab = findViewById(R.id.fabTambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PetugasActivity.this, TambahPetugasActivity.class));
            }
        });

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

        tampilData();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetugasActivity.this)
                        .setTitle("Opsi")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    try {
                                        data = jsonArray.getJSONObject(position);
                                        username = data.getString("username");
                                        nama = data.getString("nama");
                                        level = data.getString("level");
                                        no_hp = data.getString("no_hp");
                                        alamat = data.getString("alamat");
                                        foto = data.getString("foto");

                                        Intent i = new Intent(PetugasActivity.this, UbahPetugasActivity.class);
                                        i.putExtra("username", username);
                                        i.putExtra("nama", nama);
                                        i.putExtra("level", level);
                                        i.putExtra("no_hp", no_hp);
                                        i.putExtra("alamat", alamat);
                                        i.putExtra("foto", foto);
                                        startActivity(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (which == 1){
                                    try {
                                        data = jsonArray.getJSONObject(position);
                                        username = data.getString("username");
                                        foto = data.getString("foto");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    hapusData();
                                }
                            }
                        });
                builder.show();
            }
        }));
    }

    private void hapusData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Yakin?")
                .setMessage("Ya untuk hapus")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                Constants.URL_HAPUS_PETUGAS,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("kode").equals("1")) {
                                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                                startActivity(getIntent());
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
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("username", username);
                                params.put("foto", foto);

                                return params;
                            }
                        };
                        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    private void tampilData() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_PETUGAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            petugasArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("pengguna");

                            if (jsonArray.length() > 0){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    data = jsonArray.getJSONObject(i);

                                    nama = data.getString("nama");
                                    nomor = Integer.toString(i+1)+".";

                                    Petugas petugas = new Petugas(nomor, nama);
                                    petugasArrayList.add(petugas);
                                }
                                petugasAdapter = new PetugasAdapter(petugasArrayList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(petugasAdapter);
                            } else {
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
                });
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
