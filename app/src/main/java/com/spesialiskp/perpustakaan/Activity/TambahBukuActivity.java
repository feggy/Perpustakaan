package com.spesialiskp.perpustakaan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.spesialiskp.perpustakaan.Constants;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TambahBukuActivity extends AppCompatActivity {

    EditText etKodeBuku, etJudulBuku, etPengarangBuku, etPenerbitBuku, etTahunBuku, etJumlahBuku, etJenisBuku, etLokasiSimpan;
    TextView tvTglMasuk;
    Button btnSubmit;
    String kodeBuku;
    int banyakBuku;
    public final static int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/RAlibrary";
    Bitmap bitmap, bitmap2;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_buku);

        etKodeBuku = findViewById(R.id.etKodeBuku);
        etJudulBuku = findViewById(R.id.etJudulBuku);
        etPengarangBuku = findViewById(R.id.etPengarangBuku);
        etPenerbitBuku = findViewById(R.id.etPenerbitBuku);
        etTahunBuku = findViewById(R.id.etTahunBuku);
        etJumlahBuku = findViewById(R.id.etJumlahBuku);
        etJenisBuku = findViewById(R.id.etJenisBuku);
        etLokasiSimpan = findViewById(R.id.etLokasiBuku);
        tvTglMasuk = findViewById(R.id.tvTglMasuk);
        btnSubmit = findViewById(R.id.btnSubmit);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        tvTglMasuk.setText(simpleDateFormat.format(cal.getTime()));

        jumlahBuku();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahBuku();
            }
        });
    }

    private void jumlahBuku() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LIHAT_BUKU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("buku");
                            banyakBuku = jsonArray.length();
                            if (banyakBuku < 10){
                                kodeBuku = "B10100"+Integer.toString(banyakBuku+1);
                            } else {
                                kodeBuku = "B1010"+Integer.toString(banyakBuku+1);
                            }
                            etKodeBuku.setText(kodeBuku);
                            System.out.println("banyak buku "+banyakBuku+1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal menambah data", Toast.LENGTH_LONG).show();
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

    private void tambahBuku() {
        final String kode_buku = etKodeBuku.getText().toString().trim();
        final String judul_buku = etJudulBuku.getText().toString().trim();
        final String pengarang = etPengarangBuku.getText().toString().trim();
        final String penerbit = etPenerbitBuku.getText().toString().trim();
        final String tahun_buku = etTahunBuku.getText().toString().trim();
        final String jumlah_buku = etJumlahBuku.getText().toString().trim();
        final String jenis_buku = etJenisBuku.getText().toString().trim();
        final String lokasi_rak_buku = etLokasiSimpan.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_TAMBAH_BUKU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("kode").equals("0")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                alertQrCode();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal menambah data", Toast.LENGTH_LONG).show();
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

                try {
                    bitmap2 = TextToImageEncode(kodeBuku);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                params.put("kode_buku", kode_buku);
                params.put("judul_buku", judul_buku);
                params.put("pengarang", pengarang);
                params.put("penerbit", penerbit);
                params.put("tahun_buku", tahun_buku);
                params.put("jumlah_buku", jumlah_buku);
                params.put("jenis_buku", jenis_buku);
                params.put("lokasi_rak_buku", lokasi_rak_buku);
                params.put("qrcode", kode_buku);
                params.put("image", imageToString(bitmap2));

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void alertQrCode() {
        try {
            bitmap = TextToImageEncode(kodeBuku);
//            ivHasil.setImageBitmap(bitmap);
            path = saveImage(bitmap);
            View customLayout = LayoutInflater.from(TambahBukuActivity.this).inflate(R.layout.custom_qrcode, null);
            ImageView ivQr = customLayout.findViewById(R.id.ivQrcode);
//                        Button btnOk = customLayout.findViewById(R.id.btnOk);
            ivQr.setImageBitmap(bitmap);
            final AlertDialog.Builder builder = new AlertDialog.Builder(TambahBukuActivity.this)
                    .setView(customLayout)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "QR Code berhasil di simpan pada folder "+path, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });
            builder.show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
//            File f = new File(wallpaperDirectory, Calendar.getInstance()
//                    .getTimeInMillis() + ".jpg");
            String namaFoto = kodeBuku;
            File f = new File(wallpaperDirectory, namaFoto+".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
