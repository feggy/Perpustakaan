package com.spesialiskp.perpustakaan.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UbahPetugasActivity extends AppCompatActivity {

    EditText username, nama, password, no_hp, alamat;
    TextView tvKeterangan;
    RadioGroup level;
    RadioButton rbAdmin, rbPetugas;
    Button btnSubmit, btnUpload;
    int PICK_IMAGE = 1;
    Bitmap bitmap;

    String strUsernameLama, strUsername, strUsernameBaru, strPasswordLama,  strPassword, strPasswordBaru, strNamaLama, strNama, strNamaBaru, strNo_hpLama, strNo_hp, strNo_hpBaru, strAlamatLama, strAlamat, strAlamatBaru, strLevelLama, strLevel, strLevelBaru, strFotoLama, strFoto, strFotoBaru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_petugas);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            strUsernameLama = bundle.getString("username");
            strNamaLama = bundle.getString("nama");
            strLevelLama = bundle.getString("level");
            strNo_hpLama = bundle.getString("no_hp");
            strAlamatLama = bundle.getString("alamat");
            strFotoLama = bundle.getString("foto");
        }

        username = findViewById(R.id.etIdAnggota);
        password = findViewById(R.id.etNamaAnggota);
        nama = findViewById(R.id.etNoHpAnggota);
        level = findViewById(R.id.rgLevel);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbPetugas = findViewById(R.id.rbPetugas);
        no_hp = findViewById(R.id.etNoHp);
        alamat = findViewById(R.id.etAlamat);
        btnUpload = findViewById(R.id.btnUpload);
        tvKeterangan = findViewById(R.id.tvKeteranganFoto);
        btnSubmit = findViewById(R.id.btnSubmit);

        username.setText(strUsernameLama);
        nama.setText(strNamaLama);
        no_hp.setText(strNo_hpLama);
        alamat.setText(strAlamatLama);
        tvKeterangan.setText(strFotoLama+".jpg");

        if (strLevelLama.equals("administrator")){
            level.check(R.id.rbAdmin);
            strLevel = "administrator";
        } else {
            level.check(R.id.rbPetugas);
            strLevel = "petugas";
        }
        level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbAdmin){
                    strLevel = "administrator";
                } else {
                    strLevel = "petugas";
                }
            }
        });

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
                ubahPetugas();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String imagePath = getPath(uri);
                String lastName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                tvKeterangan.setText(lastName);
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

    private void ubahPetugas() {
        strUsernameBaru = username.getText().toString().trim();
        strPasswordBaru = password.getText().toString().trim();
        strNamaBaru = nama.getText().toString().trim();
        strNo_hpBaru = no_hp.getText().toString().trim();
        strAlamatBaru = alamat.getText().toString().trim();

        if (!strUsernameBaru.equals(strUsernameLama)){
            strUsername = strUsernameBaru;
        } else {
            strUsername = strUsernameLama;
        }

        if (!strNamaBaru.equals(strNamaLama)){
            strNama = strNamaBaru;
        } else {
            strNama = strNamaLama;
        }

        if (!strNo_hpBaru.equals(strNo_hpLama)){
            strNo_hp = strNo_hpBaru;
        } else {
            strNo_hp = strNo_hpLama;
        }

        if (!strAlamatBaru.equals(strAlamatBaru)){
            strAlamat = strAlamatBaru;
        } else {
            strAlamat = strAlamatLama;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UBAH_PETUGAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("kode").equals("1")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), PetugasActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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

                params.put("usernamelama", strUsernameLama);
                params.put("username", strUsername);
                if (password != null){
                    params.put("password", strPasswordBaru);
                }
                params.put("nama", strNama);
                params.put("level", strLevel);
                params.put("no_hp", strNo_hp);
                params.put("alamat", strAlamat);
                params.put("foto", strFotoLama);
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
}
