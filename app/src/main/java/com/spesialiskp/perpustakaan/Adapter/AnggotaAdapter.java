package com.spesialiskp.perpustakaan.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.spesialiskp.perpustakaan.Activity.AnggotaActivity;
import com.spesialiskp.perpustakaan.Activity.AnggotaDetailActivity;
import com.spesialiskp.perpustakaan.Activity.UbahAnggotaActivity;
import com.spesialiskp.perpustakaan.Constants.Constants;
import com.spesialiskp.perpustakaan.Models.Anggota;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Volley.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.AnggotaAdapterHolder> {

    private ArrayList<Anggota> dataList;

    public AnggotaAdapter(ArrayList<Anggota> dataList){
        this.dataList = dataList;
    }

    View view;
    ProgressDialog progressDialog;

    @NonNull
    @Override
    public AnggotaAdapter.AnggotaAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.list_anggota, viewGroup, false);

        progressDialog = new ProgressDialog(view.getContext());

        return new AnggotaAdapterHolder(view);
    }

    String id_anggota, foto_anggota;

    @Override
    public void onBindViewHolder(@NonNull final AnggotaAdapter.AnggotaAdapterHolder anggotaAdapterHolder, final int i) {

        anggotaAdapterHolder.id_anggota.setText(dataList.get(i).getId_anggota());
        anggotaAdapterHolder.nama_anggota.setText(dataList.get(i).getNama_anggota());
        String fp = anggotaAdapterHolder.foto_anggota = dataList.get(i).getFoto_anggota();
        if (!fp.equals("")){
            String imgURL = Constants.HTTP+"perpustakaan/uploads/" + fp + ".jpg";
            new DownloadImageTask(anggotaAdapterHolder.foto_anggotaa).execute(imgURL);
        }

        final String id = dataList.get(i).getId_anggota();
        final String nama = dataList.get(i).getNama_anggota();
        final String no_hp = dataList.get(i).getNohp_anggota();
        final String alamat = dataList.get(i).getAlamat_anggota();
        final String tgl_masuk = dataList.get(i).getTgl_masuk_anggota();
        final String tgl_on_kartu = dataList.get(i).getTgl_on_kartu();
        final String tgl_off_kartu = dataList.get(i).getTgl_off_kartu();
        final String foto = dataList.get(i).getFoto_anggota();

        anggotaAdapterHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Mohon tunggu sebentar...");
                progressDialog.show();

                dataList.clear();
                anggotaAdapterHolder.vContent.setVisibility(View.GONE);

                Intent i = new Intent(v.getContext(), AnggotaDetailActivity.class);
                i.putExtra("id_anggota", id);
                i.putExtra("nama", nama);
                i.putExtra("no_hp", no_hp);
                i.putExtra("alamat", alamat);
                i.putExtra("tgl_masuk", tgl_masuk);
                i.putExtra("tgl_on_kartu", tgl_on_kartu);
                i.putExtra("tgl_off_kartu", tgl_off_kartu);
                i.putExtra("foto", foto);
                v.getContext().startActivity(i);
            }
        });

        anggotaAdapterHolder.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UbahAnggotaActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("no_hp", no_hp);
                intent.putExtra("alamat", alamat);
                intent.putExtra("tgl_masuk", tgl_masuk);
                intent.putExtra("tgl_on_kartu", tgl_on_kartu);
                intent.putExtra("tgl_off_kartu", tgl_off_kartu);
                intent.putExtra("foto", foto);

                v.getContext().startActivity(intent);
            }
        });
        anggotaAdapterHolder.cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                        .setTitle("Yakin?")
                        .setMessage("Ya untuk hapus")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                id_anggota = dataList.get(i).getId_anggota();
                                foto_anggota = dataList.get(i).getFoto_anggota();

                                StringRequest stringRequest = new StringRequest(
                                        Request.Method.POST,
                                        Constants.URL_HAPUS_ANGGOTA,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.getString("kode").equals("0")) {
                                                        Toast.makeText(v.getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(v.getContext(), "Error", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(v.getContext(), "Gagal terhubung ke server", Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();

                                        params.put("id_anggota", id_anggota);
                                        params.put("foto", foto_anggota);

                                        return params;
                                    }
                                };
                                RequestHandler.getInstance(v.getContext()).addToRequestQueue(stringRequest);
                                v.getContext().startActivity(new Intent(v.getContext(), AnggotaActivity.class));
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class AnggotaAdapterHolder extends RecyclerView.ViewHolder {
        private TextView id_anggota, nama_anggota;
        public String foto_anggota;
        ImageView foto_anggotaa, ivEdit, ivDelete;
        CardView cv, cv1, cv2;
        ProgressBar progressBar;
        RelativeLayout vContent;
        public AnggotaAdapterHolder(@NonNull View itemView) {
            super(itemView);
            id_anggota = itemView.findViewById(R.id.tvNomor);
            nama_anggota = itemView.findViewById(R.id.tvNama);
            foto_anggotaa = itemView.findViewById(R.id.potoprofil);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            cv = itemView.findViewById(R.id.cv);
            cv1 = itemView.findViewById(R.id.cv1);
            cv2 = itemView.findViewById(R.id.cv2);
            progressBar = itemView.findViewById(R.id.progressBar);
            vContent = itemView.findViewById(R.id.vContent);
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
