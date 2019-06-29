package com.spesialiskp.perpustakaan.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.spesialiskp.perpustakaan.Support.RecyclerItemClickListener;
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
    LinearLayout vCaution;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        foto_profil = findViewById(R.id.potoprofil);
        vCaution = findViewById(R.id.vCaution);
        fab = findViewById(R.id.fabTambah);

        tampilData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TambahAnggotaActivity.class));
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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(position);
                    Log.e("klikItem", jsonObject.toString());

                    Intent i = new Intent(AnggotaActivity.this, AnggotaDetailActivity.class);
                    i.putExtra("id_anggota", jsonObject.getString("id_anggota"));
                    i.putExtra("nama", jsonObject.getString("nama"));
                    i.putExtra("no_hp", jsonObject.getString("no_hp"));
                    i.putExtra("alamat", jsonObject.getString("alamat"));
                    i.putExtra("tgl_masuk", jsonObject.getString("tgl_masuk"));
                    i.putExtra("tgl_on_kartu", jsonObject.getString("tgl_on_kartu"));
                    i.putExtra("tgl_off_kartu", jsonObject.getString("tgl_off_kartu"));
                    i.putExtra("foto", jsonObject.getString("foto"));
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    private void tampilData() {
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

                            if (jsonArray.length() > 0){
                                progressBar.setVisibility(View.GONE);
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
                            } else {
                                progressBar.setVisibility(View.GONE);
                                vCaution.setVisibility(View.VISIBLE);
                            }
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
