package com.spesialiskp.perpustakaan.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spesialiskp.perpustakaan.Models.Petugas;
import com.spesialiskp.perpustakaan.R;

import java.util.ArrayList;

public class PetugasAdapter extends RecyclerView.Adapter<PetugasAdapter.PetugasAdapterHolder> {
    private ArrayList<Petugas> dataList;

    public PetugasAdapter(ArrayList<Petugas> dataList){
        this.dataList =dataList;
    }

    @NonNull
    @Override
    public PetugasAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_petugas, viewGroup, false);
        return new PetugasAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetugasAdapterHolder petugasAdapterHolder, int i) {
        petugasAdapterHolder.listNama.setText(dataList.get(i).getNama());
        petugasAdapterHolder.listNomor.setText(dataList.get(i).getNomor());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class PetugasAdapterHolder extends RecyclerView.ViewHolder {
        private TextView listNama, listNomor;

        public PetugasAdapterHolder(@NonNull View itemView) {
            super(itemView);
            listNama = itemView.findViewById(R.id.tvNama);
            listNomor = itemView.findViewById(R.id.tvNomor);
        }
    }
}
