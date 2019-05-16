package com.spesialiskp.perpustakaan.Models;

public class Buku {
    String kode_buku, judul_buku, pengarang_buku, penerbit_buku, tahun_buku, jumlah_buku, jenis_buku, lokasi_buku, tgl_masuk_buku;

    public Buku(String kode_buku, String judul_buku, String pengarang_buku, String penerbit_buku, String tahun_buku, String jumlah_buku, String jenis_buku, String lokasi_buku, String tgl_masuk_buku) {
        this.kode_buku = kode_buku;
        this.judul_buku = judul_buku;
        this.pengarang_buku = pengarang_buku;
        this.penerbit_buku = penerbit_buku;
        this.tahun_buku = tahun_buku;
        this.jumlah_buku = jumlah_buku;
        this.jenis_buku = jenis_buku;
        this.lokasi_buku = lokasi_buku;
        this.tgl_masuk_buku = tgl_masuk_buku;
    }

    public String getKode_buku() {
        return kode_buku;
    }

    public void setKode_buku(String kode_buku) {
        this.kode_buku = kode_buku;
    }

    public String getJudul_buku() {
        return judul_buku;
    }

    public void setJudul_buku(String judul_buku) {
        this.judul_buku = judul_buku;
    }

    public String getPengarang_buku() {
        return pengarang_buku;
    }

    public void setPengarang_buku(String pengarang_buku) {
        this.pengarang_buku = pengarang_buku;
    }

    public String getPenerbit_buku() {
        return penerbit_buku;
    }

    public void setPenerbit_buku(String penerbit_buku) {
        this.penerbit_buku = penerbit_buku;
    }

    public String getTahun_buku() {
        return tahun_buku;
    }

    public void setTahun_buku(String tahun_buku) {
        this.tahun_buku = tahun_buku;
    }

    public String getJumlah_buku() {
        return jumlah_buku;
    }

    public void setJumlah_buku(String jumlah_buku) {
        this.jumlah_buku = jumlah_buku;
    }

    public String getJenis_buku() {
        return jenis_buku;
    }

    public void setJenis_buku(String jenis_buku) {
        this.jenis_buku = jenis_buku;
    }

    public String getLokasi_buku() {
        return lokasi_buku;
    }

    public void setLokasi_buku(String lokasi_buku) {
        this.lokasi_buku = lokasi_buku;
    }

    public String getTgl_masuk_buku() {
        return tgl_masuk_buku;
    }

    public void setTgl_masuk_buku(String tgl_masuk_buku) {
        this.tgl_masuk_buku = tgl_masuk_buku;
    }
}
