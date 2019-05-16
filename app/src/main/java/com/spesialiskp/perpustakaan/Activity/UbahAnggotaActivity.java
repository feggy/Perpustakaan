package com.spesialiskp.perpustakaan.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Constants;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UbahAnggotaActivity extends AppCompatActivity {

    String strId, strNama, strNo_hp, strAlamat, strTgl_masuk, strTgl_on_kartu, strTgl_off_kartu, strFoto, strMasuk, strOn, postOn, strOff, postOff, bulanOn, bulanOff, tglOn, tglOff, tahunOn, tahunOff, strTgl_On, strTgl_Off;
    EditText etId, etNama, etNo_hp, etAlamat, etTgl_on_kartu, etTgl_off_kartu;
    TextView tvFoto, tvTgl_masuk;
    Button btnUpload, btnSubmit;
    int PICK_IMAGE = 1;
    Bitmap bitmap;
    DatePickerDialog.OnDateSetListener dateTgl_on_kartu, dateTgl_off_kartu;
    int year1, year2, month1, month2, day1, day2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_anggota);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            strId = bundle.getString("id");
            strNama = bundle.getString("nama");
            strNo_hp = bundle.getString("no_hp");
            strAlamat = bundle.getString("alamat");
            strTgl_masuk = bundle.getString("tgl_masuk");
            strTgl_on_kartu = bundle.getString("tgl_on_kartu");
            strTgl_off_kartu = bundle.getString("tgl_off_kartu");
            strFoto = bundle.getString("foto");
        }

        etId = findViewById(R.id.etIdAnggota);
        etNama = findViewById(R.id.etNamaAnggota);
        etNo_hp = findViewById(R.id.etNoHpAnggota);
        etAlamat = findViewById(R.id.etAlamat);
        etTgl_on_kartu = findViewById(R.id.etAwalPakai);
        etTgl_off_kartu = findViewById(R.id.etAkhirPakai);
        tvTgl_masuk = findViewById(R.id.tvTglDaftarAnggota);
        tvFoto = findViewById(R.id.tvKeteranganFoto);
        btnUpload = findViewById(R.id.btnUpload);
        btnSubmit = findViewById(R.id.btnSubmit);

        String[] splitTgl_masuk = strTgl_masuk.split(" ");
        String tgl_masuk = splitTgl_masuk[0];
        String[] splitTanggal_masuk = tgl_masuk.split("-");
        String tanggal_masuk = splitTanggal_masuk[2];
        String bulan_masuk = splitTanggal_masuk[1];
        String tahun_masuk = splitTanggal_masuk[0];
        strMasuk = tanggal_masuk + "-" + bulan_masuk + "-" + tahun_masuk;

        String[] splitTgl_on_kartu = strTgl_on_kartu.split(" ");
        String tgl_on_kartu = splitTgl_on_kartu[0];
        String[] splitTanggal_on_kartu = tgl_on_kartu.split("-");
        final String tanggal_on_kartu = splitTanggal_on_kartu[2];
        final String bulan_on_kartu = splitTanggal_on_kartu[1];
        final String tahun_on_kartu = splitTanggal_on_kartu[0];
        strOn = tanggal_on_kartu + "-" + bulan_on_kartu + "-" + tahun_on_kartu;
        strTgl_On = tahun_on_kartu+"-"+bulan_on_kartu+"-"+tanggal_on_kartu;

        String[] splitTgl_off_kartu = strTgl_off_kartu.split(" ");
        String tgl_off_kartu = splitTgl_off_kartu[0];
        String[] splitTanggal_off_kartu = tgl_off_kartu.split("-");
        final String tanggal_off_kartu = splitTanggal_off_kartu[2];
        final String bulan_off_kartu = splitTanggal_off_kartu[1];
        final String tahun_off_kartu = splitTanggal_off_kartu[0];
        strOff = tanggal_off_kartu + "-" + bulan_off_kartu + "-" + tahun_off_kartu;
        strTgl_Off = tahunOff+"-"+bulan_off_kartu+"-"+tanggal_off_kartu;

        etId.setText(strId);
        etNama.setText(strNama);
        etNo_hp.setText(strNo_hp);
        etAlamat.setText(strAlamat);
        tvTgl_masuk.setText(strMasuk);
        etTgl_on_kartu.setText(strOn);
        etTgl_off_kartu.setText(strOff);
        tvFoto.setText(strFoto+".jpg");

//        etTgl_on_kartu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Calendar calendar = Calendar.getInstance();
//                int year1 = Integer.parseInt(tahun_on_kartu);
//                int month1 = Integer.parseInt(bulan_on_kartu)-1;
//                int day1 = Integer.parseInt(tanggal_on_kartu);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        UbahAnggotaActivity.this,
//                        dateTgl_on_kartu = new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                if (month < 10){
//                                    bulanOn = "0"+Integer.toString(month+1);
//                                } else {
//                                    bulanOn = Integer.toString(month+1);
//                                }
//
//                                if (dayOfMonth < 10){
//                                    tglOn = "0"+Integer.toString(dayOfMonth);
//                                } else {
//                                    tglOn = Integer.toString(dayOfMonth);
//                                }
//
//                                tahunOn = Integer.toString(year);
//                                strOn = tglOn+"-"+bulanOn+"-"+year;
//                                etTgl_on_kartu.setText(strOn);
////                                Toast.makeText(UbahAnggotaActivity.this, "postOn"+postOn, Toast.LENGTH_LONG).show();
//                            }
//                        },
//                        year1, month1, day1);
//                dialog.show();
//            }
//        });

//        etTgl_off_kartu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Calendar calendar = Calendar.getInstance();
//                int year2 = Integer.parseInt(tahun_off_kartu);
//                int month2 = Integer.parseInt(bulan_off_kartu)-1;
//                int day2 = Integer.parseInt(tanggal_off_kartu);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        UbahAnggotaActivity.this,
//                        dateTgl_off_kartu = new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                if (month < 10){
//                                    bulanOff = "0"+Integer.toString(month);
//                                } else {
//                                    bulanOff = Integer.toString(month);
//                                }
//
//                                if (dayOfMonth < 10){
//                                    tglOff = "0"+Integer.toString(dayOfMonth);
//                                } else {
//                                    tglOff = Integer.toString(dayOfMonth);
//                                }
//
//                                tahunOff = Integer.toString(year);
//                                strOff = tglOff+"-"+bulanOff+"-"+year;
//                                etTgl_off_kartu.setText(strOff);
////                                Toast.makeText(UbahAnggotaActivity.this, "postOff"+postOff, Toast.LENGTH_LONG).show();
//                            }
//                        },
//                        year2, month2, day2);
//                dialog.show();
//            }
//        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooseIntent = Intent.createChooser(getIntent, "pilih foto");
                chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooseIntent, PICK_IMAGE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahAnggota();
            }
        });
    }

    private void ubahAnggota() {
        String namaBaru = etNama.getText().toString().trim();
        String nohpBaru = etNo_hp.getText().toString().trim();
        String alamatBaru = etAlamat.getText().toString().trim();
        String onBaru = etTgl_on_kartu.getText().toString().trim();
        String offBaru = etTgl_off_kartu.getText().toString().trim();

        String[] splitOn = onBaru.split("-");
        String[] splitOff = offBaru.split("-");

        final String postNama, postNohp, postAlamat, postOn, postOff;

        String cekOn = splitOn[2]+"-"+splitOn[1]+"-"+splitOn[0];
        String cekOff = splitOff[2]+"-"+splitOff[1]+"-"+splitOff[0];

        if (!cekOn.equals(strTgl_On)){
            postOn = cekOn;
        } else {
            postOn = strTgl_On;
        }

        if (!cekOff.equals(strTgl_Off)){
            postOff = cekOff;
        } else {
            postOff = strTgl_Off;
        }

        if (!namaBaru.equals(strNama)){
            postNama = namaBaru;
        } else {
            postNama = strNama;
        }

        if (!nohpBaru.equals(strNo_hp)){
            postNohp = nohpBaru;
        } else {
            postNohp = strNo_hp;
        }

        if (!alamatBaru.equals(strAlamat)){
            postAlamat = alamatBaru;
        } else {
            postAlamat = strAlamat;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UBAH_ANGGOTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("kode").equals("0")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), AnggotaActivity.class));
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

                params.put("id", strId);
                params.put("nama", postNama);
                params.put("no_hp", postNohp);
                params.put("alamat", postAlamat);
                params.put("tgl_on_kartu", postOn);
                params.put("tgl_off_kartu", postOff);
                params.put("foto", strFoto);
                if (bitmap != null){
                    params.put("image", imageToString(bitmap));
                }
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String imagePath = getPath(uri);
                String lastName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                tvFoto.setText(lastName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}


