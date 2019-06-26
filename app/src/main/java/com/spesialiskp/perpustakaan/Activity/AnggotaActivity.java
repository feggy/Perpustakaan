package com.spesialiskp.perpustakaan.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Adapter.AnggotaAdapter;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.Models.Anggota;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnggotaActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    String id_anggota, nama, no_hp, alamat, tgl_masuk, tgl_on_kartu, tgl_off_kartu, foto;
    ImageView foto_profil, ivEdit, ivDelete;
    ArrayList<Anggota> anggotaArrayList;
    AnggotaAdapter anggotaAdapter;
    JSONObject data;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        foto_profil = findViewById(R.id.potoprofil);

        FloatingActionButton fab = findViewById(R.id.fabTambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TambahAnggotaActivity.class));
            }
        });

        tampilData();
    }

    private void tampilData() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_ANGGOTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            anggotaArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("anggota");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                data = jsonArray.getJSONObject(i);

                                id_anggota = data.getString("id_anggota");
                                nama = data.getString("nama");
                                foto = data.getString("foto");
                                no_hp = data.getString("no_hp");
                                alamat = data.getString("alamat");
                                tgl_masuk = data.getString("tgl_masuk");
                                tgl_on_kartu = data.getString("tgl_on_kartu");
                                tgl_off_kartu = data.getString("tgl_off_kartu");

                                Anggota anggota = new Anggota(id_anggota, nama, foto, no_hp, alamat, tgl_masuk, tgl_on_kartu, tgl_off_kartu);
                                anggotaArrayList.add(anggota);
                            }
                            anggotaAdapter = new AnggotaAdapter(anggotaArrayList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(anggotaAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
