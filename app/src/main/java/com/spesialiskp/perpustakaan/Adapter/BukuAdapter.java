package com.spesialiskp.perpustakaan.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spesialiskp.perpustakaan.Models.Buku;
import com.spesialiskp.perpustakaan.R;

import java.util.ArrayList;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuAdapterHolder> {

    private ArrayList<Buku> dataList;

    public BukuAdapter(ArrayList<Buku> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BukuAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_buku, viewGroup, false);
        return new BukuAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BukuAdapterHolder bukuAdapterHolder, int i) {
        bukuAdapterHolder.kode_buku.setText("KODE "+dataList.get(i).getKode_buku());
        bukuAdapterHolder.judul_buku.setText(dataList.get(i).getJudul_buku());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class BukuAdapterHolder extends RecyclerView.ViewHolder {
        private TextView kode_buku, judul_buku;
        public BukuAdapterHolder(@NonNull View itemView) {
            super(itemView);
            kode_buku = itemView.findViewById(R.id.tvKode);
            judul_buku = itemView.findViewById(R.id.tvJudul);
        }
    }
}
