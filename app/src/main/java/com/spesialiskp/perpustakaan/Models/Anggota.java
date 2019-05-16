package com.spesialiskp.perpustakaan.Models;

public class Anggota {
    String id_anggota, nama_anggota, foto_anggota, nohp_anggota, alamat_anggota, tgl_masuk_anggota, tgl_on_kartu, tgl_off_kartu;

    public Anggota(String id_anggota, String nama_anggota, String foto_anggota, String nohp_anggota, String alamat_anggota, String tgl_masuk_anggota, String tgl_on_kartu, String tgl_off_kartu) {
        this.id_anggota = id_anggota;
        this.nama_anggota = nama_anggota;
        this.foto_anggota = foto_anggota;
        this.nohp_anggota = nohp_anggota;
        this.alamat_anggota = alamat_anggota;
        this.tgl_masuk_anggota = tgl_masuk_anggota;
        this.tgl_on_kartu = tgl_on_kartu;
        this.tgl_off_kartu = tgl_off_kartu;
    }

    public String getId_anggota() {
        return id_anggota;
    }

    public void setId_anggota(String id_anggota) {
        this.id_anggota = id_anggota;
    }

    public String getNama_anggota() {
        return nama_anggota;
    }

    public void setNama_anggota(String nama_anggota) {
        this.nama_anggota = nama_anggota;
    }

    public String getFoto_anggota() {
        return foto_anggota;
    }

    public void setFoto_anggota(String foto_anggota) {
        this.foto_anggota = foto_anggota;
    }

    public String getNohp_anggota() {
        return nohp_anggota;
    }

    public void setNohp_anggota(String nohp_anggota) {
        this.nohp_anggota = nohp_anggota;
    }

    public String getAlamat_anggota() {
        return alamat_anggota;
    }

    public void setAlamat_anggota(String alamat_anggota) {
        this.alamat_anggota = alamat_anggota;
    }

    public String getTgl_masuk_anggota() {
        return tgl_masuk_anggota;
    }

    public void setTgl_masuk_anggota(String tgl_masuk_anggota) {
        this.tgl_masuk_anggota = tgl_masuk_anggota;
    }

    public String getTgl_on_kartu() {
        return tgl_on_kartu;
    }

    public void setTgl_on_kartu(String tgl_on_kartu) {
        this.tgl_on_kartu = tgl_on_kartu;
    }

    public String getTgl_off_kartu() {
        return tgl_off_kartu;
    }

    public void setTgl_off_kartu(String tgl_off_kartu) {
        this.tgl_off_kartu = tgl_off_kartu;
    }
}
