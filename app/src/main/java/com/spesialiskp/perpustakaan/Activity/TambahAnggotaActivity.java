package com.spesialiskp.perpustakaan.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TambahAnggotaActivity extends AppCompatActivity {

    EditText id, nama, alamat, no_hp, awal_pakai, akhir_pakai;
    Button btnUpload, btnSubmit;
    TextView tvKeterangan_foto, tvTglDaftar;
    DatePickerDialog.OnDateSetListener tgl_awal, tgl_akhir;
    int PICK_IMAGE = 1;
    int width, height;
    Bitmap bitmap;
    String strId, strNama, strNohp, strAlamat, strFoto, tglAkhir, bulanAkhir, tglAwal, bulanAwal, postAkhir, postAwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_anggota);

        id = findViewById(R.id.etIdAnggota);
        nama = findViewById(R.id.etNamaAnggota);
        no_hp = findViewById(R.id.etNoHpAnggota);
        alamat = findViewById(R.id.etAlamat);
        awal_pakai = findViewById(R.id.etAwalPakai);
        akhir_pakai = findViewById(R.id.etAkhirPakai);
        btnUpload = findViewById(R.id.btnUpload);
        tvKeterangan_foto = findViewById(R.id.tvKeteranganFoto);
        tvTglDaftar = findViewById(R.id.tvTglDaftarAnggota);
        btnSubmit = findViewById(R.id.btnSubmit);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        tvTglDaftar.setText(sdf.format(cal.getTime()));

        postAwal = "";
        postAkhir = "";

        Random rand = new Random();
        strId = "ID11450"+ rand.nextInt(1000);
        id.setText(strId);

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


        awal_pakai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TambahAnggotaActivity.this,
                        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        tgl_awal,
                        year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tgl_awal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month < 9){
                    bulanAwal = "0"+(month+1);
                } else {
                    bulanAwal = Integer.toString(month+1);
                }

                if (dayOfMonth < 10){
                    tglAwal = "0"+(dayOfMonth);
                } else {
                    tglAwal = Integer.toString(dayOfMonth);
                }

                postAwal = year+"-"+bulanAwal+"-"+tglAwal;
                String set = tglAwal+"-"+bulanAwal+"-"+year;
                awal_pakai.setText(set);
            }
        };

        akhir_pakai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TambahAnggotaActivity.this,
                        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        tgl_akhir,
                        year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tgl_akhir = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month < 9){
                    bulanAkhir = "0"+(month+1);
                } else {
                    bulanAkhir = Integer.toString(month+1);
                }

                if (dayOfMonth < 10){
                    tglAkhir = "0"+(dayOfMonth);
                } else {
                    tglAkhir = Integer.toString(dayOfMonth);
                }

                postAkhir = year+"-"+bulanAkhir+"-"+tglAkhir;
                String set = tglAkhir+"-"+bulanAkhir+"-"+year;
                akhir_pakai.setText(set);
            }
        };

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("wh", width+"x"+height);
                if (width > 2048 || height > 2048){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahAnggotaActivity.this)
                            .setTitle("Oops")
                            .setMessage("Size image terlalu besar, mohon upload foto dengan ukuran yang lebih kecil")
                            .setPositiveButton("OKE", null);
                    builder.show();
                } else {
                    tambahAnggota();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String imagePath = getPath(uri);
                String lastName = imagePath.substring(imagePath.lastIndexOf('/') + 1);

                width = bitmap.getWidth();
                height = bitmap.getHeight();
                Log.e("Width Height", width+" x "+height);

                tvKeterangan_foto.setText(lastName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*private void idAnggota(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_ANGGOTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("anggota");
                            Random rand = new Random();
                            strId = "ID11450"+ rand.nextInt(1000);
                            Log.e("id_anggota", strId);
                            id.setText(strId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }*/

    private void tambahAnggota() {
//        strId = id.getText().toString().trim();
        strNama = nama.getText().toString().trim();
        strNohp = no_hp.getText().toString().trim();
        strAlamat = alamat.getText().toString().trim();

        if (bitmap == null){
            strFoto = "";
        }

        if (!strNama.isEmpty() && !strNohp.isEmpty() && !strAlamat.isEmpty() & !postAwal.isEmpty() & !postAkhir.isEmpty()){
            Log.e("tambahAnggota", strNama+" "+strNohp+" "+strAlamat+" "+postAwal+" "+postAkhir);

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_TAMBAH_ANGGOTA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.e("tambahAnggota", jsonObject.toString());
                                if (jsonObject.getString("kode").equals("1")) {
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

                    params.put("id_anggota", strId);
                    params.put("nama", strNama);
                    params.put("no_hp", strNohp);
                    params.put("alamat", strAlamat);
                    params.put("awal_pakai", postAwal);
                    params.put("akhir_pakai", postAkhir);
                    if (bitmap != null){
                        params.put("image", imageToString(bitmap));
                    }

                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Kolom harus di isi semua", Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Log.e("Width Height", bitmap.getWidth()+" "+bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}
