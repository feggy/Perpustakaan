package com.spesialiskp.perpustakaan.Models;

public class Petugas {
    String nomor, nama;

    public Petugas(String nomor, String nama) {
        this.nomor = nomor;
        this.nama = nama;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
