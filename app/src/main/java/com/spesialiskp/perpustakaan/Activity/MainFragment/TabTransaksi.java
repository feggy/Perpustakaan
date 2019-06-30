package com.spesialiskp.perpustakaan.Activity.MainFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.kimkevin.cachepot.CachePot;
import com.spesialiskp.perpustakaan.Activity.PeminjamanActivity;
import com.spesialiskp.perpustakaan.Activity.TransaksiDetailActivity;
import com.spesialiskp.perpustakaan.Adapter.PeminjamanAdapter;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.Dialog.FilterDialog;
import com.spesialiskp.perpustakaan.Models.Peminjaman;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Support.RecyclerItemClickListener;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TabTransaksi extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View view;
    RecyclerView recyclerView;
    CardView fab;
    JSONArray jsonArray;
    JSONObject data, jsonObject;
    String id, tgl_kembali;
    LinearLayout vCaution;
    ProgressBar progressBar;
    RelativeLayout vContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_transaksi, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fabTransaksi);
        vCaution = view.findViewById(R.id.vCaution);
        progressBar = view.findViewById(R.id.progressBar);
        vContent = view.findViewById(R.id.vContent);

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
        fabKlik();
        rcKlik();

        return view;
    }

    private void rcKlik() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                try {
                    vContent.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    jsonObject = jsonArray.getJSONObject(position);
                    Log.e("klikItem", jsonObject.toString());

                    String kode_pinjam = jsonObject.getString("kode_pinjam");
                    String id_anggota = jsonObject.getString("id_anggota");
                    String kode_buku = jsonObject.getString("kode_buku");
                    String tgl_pinjam = jsonObject.getString("tgl_pinjam");
                    String tgl_kembali = jsonObject.getString("tgl_kembali");
                    String tgl_perpanjang = jsonObject.getString("tgl_perpanjang");
                    String status = jsonObject.getString("status");
                    String denda = jsonObject.getString("denda");
                    String nama = jsonObject.getString("nama");
                    String judul_buku = jsonObject.getString("judul_buku");

                    Intent i = new Intent(getContext(), TransaksiDetailActivity.class);
                    i.putExtra("kode_pinjam", kode_pinjam);
                    i.putExtra("id_anggota", id_anggota);
                    i.putExtra("kode_buku", kode_buku);
                    i.putExtra("tgl_pinjam", tgl_pinjam);
                    i.putExtra("tgl_kembali", tgl_kembali);
                    i.putExtra("tgl_perpanjang", tgl_perpanjang);
                    i.putExtra("status", status);
                    i.putExtra("denda", denda);
                    i.putExtra("nama", nama);
                    i.putExtra("judul_buku", judul_buku);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void perpanjang() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PERPANJANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Buku kode peminjaman " + id + " berhasil diperpanjang", Toast.LENGTH_LONG).show();
                        startActivity(getActivity().getIntent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("kode_pinjam", id);
                params.put("tgl_kembali", tgl_kembali);

                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }

    /*private void ubahStatus() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PENGEMBALIAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view.getContext(), "Buku kode peminjaman " + id + " berhasil dikembalikan", Toast.LENGTH_LONG).show();
                        startActivity(getActivity().getIntent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("kode_pinjam", id);
                params.put("status", "Selesai");

                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }*/

    private void fabKlik() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.show(getChildFragmentManager(), "FILTER_DIALOG");
            }
        });
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
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("peminjaman");

                            if (jsonArray.length() > 0){
                                progressBar.setVisibility(View.GONE);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    data = jsonArray.getJSONObject(i);
                                    Log.e("tampilDataa", data.toString());

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
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(peminjamanAdapter);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                vCaution.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }

    private void transaksi() {
        String[] item = {"Peminjaman", "Pengembalian", "Perpanjang"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Pilihan")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if (which == 0){
                           startActivity(new Intent(getContext(), PeminjamanActivity.class));
                       } else if (which == 1){
                           Toast.makeText(getContext(), "Coming soon!", Toast.LENGTH_LONG).show();
                       } else if (which == 2){
                           Toast.makeText(getContext(), "Coming soon!", Toast.LENGTH_LONG).show();
                       }
                    }
                });
        builder.show();
    }
}
