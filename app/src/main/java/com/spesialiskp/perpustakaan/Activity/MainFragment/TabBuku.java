package com.spesialiskp.perpustakaan.Activity.MainFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Adapter.BukuAdapter;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.Models.Buku;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabBuku extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    SearchView searchView;
    RecyclerView recyclerView;
    LinearLayout vCaution;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_buku, container, false);

//        searchView = view.findViewById(R.id.search);
//        searchView.setFocusable(true);
//        searchView.setFocusableInTouchMode(true);
//        searchView.setQueryHint("Cari buku apa?");

        recyclerView = view.findViewById(R.id.recyclerView);
        vCaution = view.findViewById(R.id.vCaution);
        progressBar = view.findViewById(R.id.progressBar);

        /*FloatingActionButton fab = view.findViewById(R.id.fabBuku);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TambahBukuActivity.class));
            }
        });*/

        tampilData();

        return view;
    }

    private void tampilData() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_BUKU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList bukuArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("buku");

                            if (jsonArray.length() > 0){
                                progressBar.setVisibility(View.GONE);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    String kode = data.getString("kode_buku");
                                    String judul = data.getString("judul_buku");

                                    Buku buku = new Buku(kode, judul, "","","","","","","");
                                    bukuArrayList.add(buku);
                                }
                                BukuAdapter bukuAdapter = new BukuAdapter(bukuArrayList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(bukuAdapter);
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
                        Toast.makeText(view.getContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }
}
