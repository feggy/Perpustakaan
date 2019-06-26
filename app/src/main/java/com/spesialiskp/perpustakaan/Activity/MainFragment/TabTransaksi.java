package com.spesialiskp.perpustakaan.Activity.MainFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Activity.PeminjamanActivity;
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
    JSONObject data;
    String id, tgl_kembali;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_transaksi, container, false);

        /*BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
        dialog.setContentView(view);
        dialog.show();*/

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fabTransaksi);

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

    String[] item = {"Pengembalian Buku", "Perpanjang"};
    private void rcKlik() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Pilihan")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    try {
                                        data = jsonArray.getJSONObject(position);
                                        id = data.getString("kode_pinjam");

                                        ubahStatus();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        data = jsonArray.getJSONObject(position);
                                        id = data.getString("kode_pinjam");

                                        String tgl_ = data.getString("tgl_kembali");
                                        String[] split = tgl_.split(" ");
                                        tgl_kembali = split[0];

                                        perpanjang();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                builder.show();
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

    private void ubahStatus() {
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
                params.put("status", "dikembalikan");

                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }

    private void fabKlik() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.show(getChildFragmentManager(), filterDialog.getTag());
            }
        });
    }

    private void tampilData() {
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
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(peminjamanAdapter);
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
