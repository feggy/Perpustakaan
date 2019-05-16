package com.spesialiskp.perpustakaan.Models;

public class Peminjaman {

    private String kode_peminjaman, nama, buku, tgl_pinjam, tgl_kembali, denda, status;

    public Peminjaman(String kode_peminjaman, String nama, String buku, String tgl_pinjam, String tgl_kembali, String denda, String status) {
        this.kode_peminjaman = kode_peminjaman;
        this.nama = nama;
        this.buku = buku;
        this.tgl_pinjam = tgl_pinjam;
        this.tgl_kembali = tgl_kembali;
        this.denda = denda;
        this.status = status;
    }

    public String getKode_peminjaman() {
        return kode_peminjaman;
    }

    public void setKode_peminjaman(String kode_peminjaman) {
        this.kode_peminjaman = kode_peminjaman;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBuku() {
        return buku;
    }

    public void setBuku(String buku) {
        this.buku = buku;
    }

    public String getTgl_pinjam() {
        return tgl_pinjam;
    }

    public void setTgl_pinjam(String tgl_pinjam) {
        this.tgl_pinjam = tgl_pinjam;
    }

    public String getTgl_kembali() {
        return tgl_kembali;
    }

    public void setTgl_kembali(String tgl_kembali) {
        this.tgl_kembali = tgl_kembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDenda() {
        return denda;
    }

    public void setDenda(String denda) {
        this.denda = denda;
    }
}
