package com.spesialiskp.perpustakaan.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.spesialiskp.perpustakaan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransaksiDetailActivity extends AppCompatActivity {

    TextView vNama, vJudulBuku, vKodeBuku, vStatus, vTglPinjam, vTglKembali, vKodePinjam;
    ImageView vQrCode;
    Button btnSave;
    RelativeLayout vContent;
    LinearLayout progressBar;
    String kode_pinjam, id_anggota, kode_buku, tgl_pinjam, tgl_kembali, tgl_perpanjang, status, denda, nama, judul_buku;
    public final static int QRcodeWidth = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_detail);

        init();
    }

    private void init() {
        initView();
        initData();
        initUI();
    }

    private void initUI() {
        vNama.setText(nama+" (id: "+id_anggota+")");
        vJudulBuku.setText(judul_buku);
        vKodePinjam.setText("Kode Pinjam: "+kode_pinjam);
        vKodeBuku.setText("Kode buku: "+kode_buku);
        vStatus.setText("Status: "+status);

        Locale locale = new Locale("id", "ID");
        SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dddd");
        Date tglPinjam = null;
        Date tglKembali = null;
        try {
            tglPinjam = myDate.parse(tgl_pinjam);
            tglKembali = myDate.parse(tgl_kembali);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy", locale);
        vTglPinjam.setText("Tgl pinjam: "+newDate.format(tglPinjam));
        vTglKembali.setText("Tgl kembali: "+newDate.format(tglKembali));

        try {
            Bitmap bitmap = TextToImageEncode(kode_pinjam);
            vQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(TransaksiDetailActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
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

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            kode_pinjam = bundle.getString("kode_pinjam");
            id_anggota = bundle.getString("id_anggota");
            kode_buku = bundle.getString("kode_buku");
            tgl_pinjam = bundle.getString("tgl_pinjam");
            tgl_kembali = bundle.getString("tgl_kembali");
            tgl_perpanjang = bundle.getString("tgl_perpanjang");
            status = bundle.getString("status");
            denda = bundle.getString("denda");
            nama = bundle.getString("nama");
            judul_buku = bundle.getString("judul_buku");
        }

    }

    private void initView() {
        vNama = findViewById(R.id.vNama);
        vJudulBuku = findViewById(R.id.vJudulBuku);
        vKodeBuku = findViewById(R.id.vKodeBuku);
        vKodePinjam = findViewById(R.id.vKodePinjam);
        vStatus = findViewById(R.id.vStatus);
        vTglPinjam = findViewById(R.id.vtglPinjam);
        vTglKembali = findViewById(R.id.vTglKembali);
        vQrCode = findViewById(R.id.vQrcode);
        btnSave = findViewById(R.id.vSave);
        vContent = findViewById(R.id.vContent);
        progressBar = findViewById(R.id.progressBar);
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

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "/RALibrary/Peminjaman");
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, kode_pinjam+".jpg");
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
}
