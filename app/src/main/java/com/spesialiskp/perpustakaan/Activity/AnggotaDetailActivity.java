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

public class AnggotaDetailActivity extends AppCompatActivity {

    TextView vNama, vAlamat, vNoHp, vIdAnggota, vTglMasuk, vTglBerakhir;
    ImageView vQrcode;
    Button vSave;
    RelativeLayout vContent;
    String Nama, Alamat, NoHp, IdAnggota, TglMasuk, TglBerakhir;
    public final static int QRcodeWidth = 500;
    LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota_detail);

        init();
    }

    private void init() {
        initView();
        initData();
        initUI();
    }

    private void initUI() {
        vNama.setText(Nama);
        vAlamat.setText(Alamat);
        vNoHp.setText(NoHp);
        vIdAnggota.setText(": "+IdAnggota);

        Locale locale = new Locale("id", "ID");
        SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dddd");
        Date tglPinjam = null;
        Date tglKembali = null;
        try {
            tglPinjam = myDate.parse(TglMasuk);
            tglKembali = myDate.parse(TglBerakhir);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy", locale);
        vTglMasuk.setText(": "+newDate.format(tglPinjam));
        vTglBerakhir.setText(": "+newDate.format(tglKembali));

        try {
            Bitmap bitmap = TextToImageEncode(IdAnggota);
            vQrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(AnggotaDetailActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
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
            Nama = bundle.getString("nama");
            Alamat = bundle.getString("alamat");
            NoHp = bundle.getString("no_hp");
            IdAnggota = bundle.getString("id_anggota");
            TglMasuk = bundle.getString("tgl_on_kartu");
            TglBerakhir = bundle.getString("tgl_off_kartu");
        }
    }

    private void initView() {
        vNama = findViewById(R.id.vNama);
        vAlamat = findViewById(R.id.vAlamat);
        vNoHp = findViewById(R.id.vNoHp);
        vIdAnggota = findViewById(R.id.vIdAnggota);
        vTglMasuk = findViewById(R.id.vTglMasuk);
        vTglBerakhir = findViewById(R.id.vTglBerakhir);
        vQrcode = findViewById(R.id.vQrcode);
        vSave = findViewById(R.id.vSave);
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
                Environment.getExternalStorageDirectory() + "/RALibrary/RA_Anggota");
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, IdAnggota+".jpg");
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
