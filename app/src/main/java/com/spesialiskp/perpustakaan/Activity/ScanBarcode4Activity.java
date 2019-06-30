package com.spesialiskp.perpustakaan.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;
import com.spesialiskp.perpustakaan.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanBarcode4Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private String TAG = "Zxing";
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode2);

        mScannerView = new ZXingScannerView(this);

        mScannerView = findViewById(R.id.vScanner);
        mScannerView.setAutoFocus(true);
        mScannerView.startCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e(TAG, rawResult.getText()); // Prints scan results
        Log.e(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

        Bundle bundle = new Bundle();

        Bundle bundleKodeBuku = getIntent().getExtras();
        String kodeBuku = "";
        if (bundleKodeBuku != null) {
            kodeBuku = bundleKodeBuku.getString("kode_buku");
        }
        String hasilScan2 = rawResult.getText();
        bundle.putString("kode_buku", kodeBuku);
        bundle.putString("hasil_scan2", hasilScan2);
        Intent i = new Intent(ScanBarcode4Activity.this, PeminjamanActivity.class);
        i.putExtras(bundle);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
