package com.spesialiskp.perpustakaan.Activity.MainFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class TabDashboard extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    TextView tvJmlhAnggota, tvJmlhBuku, tvJmlhPeminjaman, tvJmlhKembali;
    ProgressBar progressBar;
    LinearLayout vTampilan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_dashboard, container, false);

        tvJmlhAnggota = view.findViewById(R.id.tvJmlhAnggota);
        tvJmlhBuku = view.findViewById(R.id.tvJmlhBuku);
        tvJmlhPeminjaman = view.findViewById(R.id.tvJmlhPeminjaman);
        tvJmlhKembali = view.findViewById(R.id.tvJmlhKembali);
        progressBar = view.findViewById(R.id.progressBar);
        vTampilan = view.findViewById(R.id.vTampilan);

        tampilData();

        return view;
    }

    private void tampilData() {
        progressBar.setVisibility(View.VISIBLE);
        vTampilan.setVisibility(View.INVISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DASHBOARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        vTampilan.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvJmlhAnggota.setText(jsonObject.getString("anggota"));
                            tvJmlhBuku.setText(jsonObject.getString("buku"));
                            tvJmlhPeminjaman.setText(jsonObject.getString("sedangdipinjam"));
                            tvJmlhKembali.setText(jsonObject.getString("sudahdikembalikan"));
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
