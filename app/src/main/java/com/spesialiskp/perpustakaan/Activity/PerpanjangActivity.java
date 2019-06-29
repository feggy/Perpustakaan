package com.spesialiskp.perpustakaan.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PerpanjangActivity extends AppCompatActivity {

    EditText etKodePinjam, etPerpanjang;
    TextView vNama, vKodeBuku, vJudulBuku, vTglPinjam, vTglKembali, vTelat, vDenda;
    LinearLayout vDetail;
    Button btnSubmit;
    String hasilScan, tglPerpanjang, status;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter, datePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perpanjang);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void init() {
        initView();
        initUI();
    }

    private void initUI() {
        etKodePinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(PerpanjangActivity.this, new String[] {Manifest.permission.CAMERA}, 101);
                } else {
                    startActivity(new Intent(PerpanjangActivity.this, ScanBarcode3Activity.class));
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            hasilScan = bundle.getString("hasil_scan");

            cekDetail();
        }

        etPerpanjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perpanjang();
            }
        });
    }

    private void initView() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        datePost = new SimpleDateFormat("yyyy-MM-dd");

        etKodePinjam = findViewById(R.id.etKodePinjam);
        vNama = findViewById(R.id.vNama);
        vKodeBuku = findViewById(R.id.vKodeBuku);
        vJudulBuku = findViewById(R.id.vJudulBuku);
        vTglPinjam = findViewById(R.id.vtglPinjam);
        vTglKembali = findViewById(R.id.vTglKembali);
        vTelat = findViewById(R.id.vTelat);
        vDenda = findViewById(R.id.vDenda);
        vDetail = findViewById(R.id.vDetail);
        btnSubmit = findViewById(R.id.btnSubmit);
        etPerpanjang = findViewById(R.id.etPerpanjang);
    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Log.e("newDate", datePost.format(newDate.getTime()));

                tglPerpanjang = datePost.format(newDate.getTime());

                etPerpanjang.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void perpanjang() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PERPANJANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        alertDialog("Sukses", "Berhasil memperpanjang waktu pengembalian buku", PerpanjangActivity.this);
//                        Toast.makeText(getApplicationContext(), "Berhasil memperpanjang pengembalian buku", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("kode_pinjam", hasilScan);
                params.put("tgl_kembali", tglPerpanjang);

                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void cekDetail() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_PEMINJAMAN_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("peminjaman");
                            Log.e("data", jsonArray.toString());

                            if (jsonArray.length() > 0){

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    status = data.getString("status");

                                    etKodePinjam.setText(hasilScan);

                                    if (status.equals("Selesai")){
                                        alertDialog("Oops", "Id transaksi "+hasilScan+" ini telah selesai, anda tidak dapat memperpanjang masa peminjaman", null);
                                    } else {
                                        vDetail.setVisibility(View.VISIBLE);
                                    }


                                    vNama.setText(data.getString("nama"));
                                    vKodeBuku.setText(data.getString("kode_buku"));
                                    vJudulBuku.setText(data.getString("judul_buku"));

                                    Locale locale = new Locale("id", "ID");
                                    SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dddd");
                                    Date tglPinjam = null;
                                    Date tglKembali = null;
                                    try {
                                        tglPinjam = myDate.parse(data.getString("tgl_pinjam"));
                                        tglKembali = myDate.parse(data.getString("tgl_kembali"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy", locale);
                                    vTglPinjam.setText(newDate.format(tglPinjam));
                                    vTglKembali.setText(newDate.format(tglKembali));

                                    SimpleDateFormat sdf = new SimpleDateFormat("y-MM-dd");
                                    Date now = Calendar.getInstance().getTime();
                                    String waktu_sekarang = sdf.format(now);
                                    int denda = 0;
                                    long sisahari = 0;
                                    try {
                                        Date hariKembali = sdf.parse(data.getString("tgl_kembali"));
                                        Date hariIni = sdf.parse(waktu_sekarang);

                                        long sisa = hariIni.getTime() - hariKembali.getTime();
                                        sisahari = sisa/(1000*60*60*24);
                                        denda = 500*Math.round(sisahari);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (sisahari > 0){
                                        vTelat.setText((int)sisahari+" hari");
                                        vDenda.setText("Rp. "+denda);
                                    }
                                }
                            }else {
                                alertDialog("Oops", "Data peminjaman buku tidak di temukan", null);
                                /*final AlertDialog.Builder builder = new AlertDialog.Builder(PerpanjangActivity.this)
                                        .setTitle("Oops")
                                        .setMessage("Data peminjaman tidak ditemukan")
                                        .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                builder.show();*/
                                vDetail.setVisibility(View.GONE);
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

                params.put("kode_pinjam", hasilScan);

                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void alertDialog(String title, String message, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(PerpanjangActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context == null){
                            dialog.dismiss();
                        } else {
                            startActivity(new Intent(context, PerpanjangActivity.class));
                        }
                    }
                });
        builder.show();
    }
}
