package com.spesialiskp.perpustakaan.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.spesialiskp.perpustakaan.Models.Peminjaman;
import com.spesialiskp.perpustakaan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PeminjamanAdapter extends RecyclerView.Adapter<PeminjamanAdapter.PeminjamanAdapterHolder> {

    private ArrayList<Peminjaman> dataList;

    public PeminjamanAdapter(ArrayList<Peminjaman> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public PeminjamanAdapter.PeminjamanAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_history, viewGroup, false);
        return new PeminjamanAdapterHolder(view);
    }

    float sisahari;
    @Override
    public void onBindViewHolder(@NonNull PeminjamanAdapter.PeminjamanAdapterHolder peminjamanAdapterHolder, int i) {

        peminjamanAdapterHolder.kode_pinjam.setText("#"+dataList.get(i).getKode_peminjaman());
        peminjamanAdapterHolder.nama.setText(dataList.get(i).getNama());
        peminjamanAdapterHolder.buku.setText(dataList.get(i).getBuku());

        String tgl1 = dataList.get(i).getTgl_pinjam();
        String[] split1 = tgl1.split(" ");
        String tgl2 = dataList.get(i).getTgl_kembali();
        String[] split2 = tgl2.split(" ");

        String[] split3 = split1[0].split("-");
        String[] split4 = split2[0].split("-");

        String tgl_pinjam = split3[2]+"-"+split3[1]+"-"+split3[0];
        String tgl_kembali = split4[2]+"-"+split4[1]+"-"+split4[0];

        String status = dataList.get(i).getStatus();

//        Date date = Calendar.getInstance().getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-y");
//        String datenow = simpleDateFormat.format(date);
//
//        Date start = new Date(datenow);
//        Date end = new Date(split4[2]+"-"+split4[1]+"-"+split4[0]);
//        long s = start.getTime() - end.getTime();
//        int selisih = (int) s;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-y");
        Date now = Calendar.getInstance().getTime();
        String waktu_sekarang = sdf.format(now);
        int denda = 0;

        try {
            Date hariKembali = sdf.parse(tgl_kembali);
            Date hariIni = sdf.parse(waktu_sekarang);

            long sisa = hariIni.getTime() - hariKembali.getTime();
            sisahari = sisa/(1000*60*60*24);

            if (sisahari > 0 && status.equals("dipinjam")){
                denda = 500*Math.round(sisahari);
            } else {
                denda = 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("PeminjamanAdapter", "Tgl kembali = "+tgl_kembali+" Tgl hari ini = "+waktu_sekarang+", sisa hari = "+sisahari+" Denda = "+denda);

        peminjamanAdapterHolder.tgl_pinjam.setText(tgl_pinjam);
        peminjamanAdapterHolder.tgl_kembali.setText(tgl_kembali);
        peminjamanAdapterHolder.denda.setText("Denda\nRp. "+denda+"\n\nTerlambat "+(int) sisahari+" hari");
        peminjamanAdapterHolder.status.setText(status);

        if (denda == 0) {
            peminjamanAdapterHolder.denda.setVisibility(View.GONE);
        }

        if (status.equals("dikembalikan")) {
            peminjamanAdapterHolder.cardView.setCardBackgroundColor(peminjamanAdapterHolder.itemView.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class PeminjamanAdapterHolder extends RecyclerView.ViewHolder {
        private TextView kode_pinjam, nama, buku, tgl_pinjam, tgl_kembali, denda, status;
        private CardView cardView;

        public PeminjamanAdapterHolder(@NonNull View itemView) {
            super(itemView);
            kode_pinjam = itemView.findViewById(R.id.tvId);
            nama = itemView.findViewById(R.id.tvNama);
            buku = itemView.findViewById(R.id.tvBuku);
            tgl_pinjam = itemView.findViewById(R.id.tvPinjam);
            tgl_kembali = itemView.findViewById(R.id.tvKembali);
            denda = itemView.findViewById(R.id.tvDenda);
            status = itemView.findViewById(R.id.tvStatus);
            cardView = itemView.findViewById(R.id.vProses);
        }
    }
}
