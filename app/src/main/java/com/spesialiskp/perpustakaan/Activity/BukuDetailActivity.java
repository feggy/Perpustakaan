package com.spesialiskp.perpustakaan.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BukuDetailActivity extends AppCompatActivity {

    String kode_buku, judul_buku, pengarang, penerbit, tahun_buku, jumlah_buku, jenis_buku, lokasi_rak_buku, qrcode, tgl_masuk_buku;
    TextView vJudul_buku, vPengarang, vTahunBuku, vKodeBuku, vPenerbit, vJumlahBuku, vJenis, vLokasiRak, vTglMasuk;
    ImageView vQrcode;
    RelativeLayout vContent;
    LinearLayout progressBar;
    Button vSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_detail);

        init();
    }

    private void init() {
        initData();
        initView();
        initUI();
    }

    private void initUI() {
        vJudul_buku.setText(judul_buku);
        vPengarang.setText(pengarang);
        vTahunBuku.setText(tahun_buku);
        vKodeBuku.setText(kode_buku);
        vPenerbit.setText(penerbit);
        vJumlahBuku.setText("Jumlah: "+jumlah_buku);
        vJenis.setText("Jenis: "+jenis_buku);
        vLokasiRak.setText("Rak: "+lokasi_rak_buku);

        Locale locale = new Locale("id", "ID");
        SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dddd");
        Date tglMasuk = null;
        try {
            tglMasuk = myDate.parse(tgl_masuk_buku);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy", locale);
        vTglMasuk.setText("Tgl masuk: "+newDate.format(tglMasuk));

        if (!qrcode.equals("")){
            String imgURL = Constants.HTTP+"perpustakaan/uploads/" + qrcode + ".jpg";
            new DownloadImageTask(vQrcode).execute(imgURL);
        }

        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(BukuDetailActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Bitmap bitmap = viewToBitmap(vContent);
                    String sukses = saveImage(bitmap);
                    Log.e("sukses", sukses);
                    Toast.makeText(getApplicationContext(), "Gambar berhasil disimpan", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/RALibrary/RA_Buku");
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, qrcode+".jpg");
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

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void initView() {
        vJudul_buku = findViewById(R.id.vJudul_buku);
        vPengarang = findViewById(R.id.vPengarang);
        vTahunBuku = findViewById(R.id.vTahunBuku);
        vKodeBuku = findViewById(R.id.vKodeBuku);
        vPenerbit = findViewById(R.id.vPenerbit);
        vJumlahBuku = findViewById(R.id.vJumlahBuku);
        vJenis = findViewById(R.id.vJenis);
        vLokasiRak = findViewById(R.id.vLokasiRak);
        vTglMasuk = findViewById(R.id.vTglMasuk);
        vQrcode = findViewById(R.id.vQrcode);
        vContent = findViewById(R.id.vContent);
        vSave = findViewById(R.id.vSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            kode_buku = bundle.getString("kode_buku");
            judul_buku = bundle.getString("judul_buku");
            pengarang = bundle.getString("pengarang");
            penerbit = bundle.getString("penerbit");
            tahun_buku = bundle.getString("tahun_buku");
            jumlah_buku = bundle.getString("jumlah_buku");
            lokasi_rak_buku = bundle.getString("lokasi_rak_buku");
            qrcode = bundle.getString("qrcode");
            tgl_masuk_buku = bundle.getString("tgl_masuk_buku");
            jenis_buku = bundle.getString("jenis_buku");
        }
    }

    // UNTUK LIHAT IMAGE DARI URL
    // START
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlOfImage = strings[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
    //END
}
