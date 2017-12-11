package com.example.meita.rentalpelanggan.MenuPemesanan;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by meita on 17/09/2017.
 */

public class PemesananModel implements Serializable{
    public String idPemesanan, idKendaraan, idPelanggan, idRental, statusPemesanan, tglPembuatanPesanan, tglSewa, tglKembali,
            keteranganKhusus, jamPenjemputan, alamatPenjemputan;
    public int jumlahKendaraan, jumlahHariPenyewaan;
    public double latitude_penjemputan, longitude_penjemputan, totalBiayaPembayaran;
    public String jamPengambilan, batasWaktuPembayaran, kategoriKendaraan, idRekeningRental;
    String idPembayaran, uriFotoBuktiPembayaran,
            bankPelanggan, namaPemilikRekeningPelanggan, nomorRekeningPelanggan, jumlahTransfer, waktuPembayaran;
    String alasanPembatalan;
    boolean statusUlasan;

    public PemesananModel() {

    }

    public PemesananModel(String idPemesanan, String idKendaraan, String idPelanggan, String idRental, String statusPemesanan,
                          String tglPembuatanPesanan, String tglSewa, String tglKembali, String keteranganKhusus, String jamPengambilan,
                          int jumlahKendaraan, int jumlahHariPenyewaan, double totalBiayaPembayaran, String batasWaktuPembayaran, String kategoriKendaraan,
                          String idRekeningRental, boolean statusUlasan) {
        this.idPemesanan = idPemesanan;
        this.idKendaraan = idKendaraan;
        this.idPelanggan = idPelanggan;
        this.idRental = idRental;
        this.statusPemesanan = statusPemesanan;
        this.tglPembuatanPesanan = tglPembuatanPesanan;
        this.tglSewa = tglSewa;
        this.tglKembali = tglKembali;
        this.keteranganKhusus = keteranganKhusus;
        this.jamPengambilan = jamPengambilan;
        this.jumlahKendaraan = jumlahKendaraan;
        this.jumlahHariPenyewaan = jumlahHariPenyewaan;
        this.totalBiayaPembayaran = totalBiayaPembayaran;
        this.batasWaktuPembayaran = batasWaktuPembayaran;
        this.kategoriKendaraan = kategoriKendaraan;
        this.idRekeningRental = idRekeningRental;
        this.statusUlasan = statusUlasan;
    }

    public PemesananModel(String idPemesanan, String idKendaraan, String idPelanggan, String idRental, String statusPemesanan,
                          String tglPembuatanPesanan, String tglSewa, String tglKembali, String keteranganKhusus, String jamPenjemputan,
                          int jumlahKendaraan, double latitude_penjemputan, double longitude_penjemputan, String alamatPenjemputan,
                          int jumlahHariPenyewaan, double totalBiayaPembayaran, String batasWaktuPembayaran, String kategoriKendaraan, String idRekeningRental, boolean statusUlasan) {
        this.idPemesanan = idPemesanan;
        this.idKendaraan = idKendaraan;
        this.idPelanggan = idPelanggan;
        this.idRental = idRental;
        this.statusPemesanan = statusPemesanan;
        this.tglPembuatanPesanan = tglPembuatanPesanan;
        this.tglSewa = tglSewa;
        this.tglKembali = tglKembali;
        this.keteranganKhusus = keteranganKhusus;
        this.jamPenjemputan = jamPenjemputan;
        this.jumlahKendaraan = jumlahKendaraan;
        this.latitude_penjemputan = latitude_penjemputan;
        this.longitude_penjemputan = longitude_penjemputan;
        this.alamatPenjemputan = alamatPenjemputan;
        this.jumlahHariPenyewaan = jumlahHariPenyewaan;
        this.totalBiayaPembayaran = totalBiayaPembayaran;
        this.tglPembuatanPesanan = tglPembuatanPesanan;
        this.batasWaktuPembayaran = batasWaktuPembayaran;
        this.kategoriKendaraan = kategoriKendaraan;
        this.idRekeningRental = idRekeningRental;
        this.statusUlasan = statusUlasan;
    }

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public String getIdRental() {
        return idRental;
    }

    public String getStatusPemesanan() {
        return statusPemesanan;
    }

    public String getTglPembuatanPesanan() {
        return tglPembuatanPesanan;
    }

    public String getTglSewa() {
        return tglSewa;
    }

    public String getTglKembali() {
        return tglKembali;
    }

    public String getKeteranganKhusus() {
        return keteranganKhusus;
    }

    public String getJamPenjemputan() {
        return jamPenjemputan;
    }

    public String getAlamatPenjemputan() {
        return alamatPenjemputan;
    }

    public int getJumlahKendaraan() {
        return jumlahKendaraan;
    }

    public int getJumlahHariPenyewaan() {
        return jumlahHariPenyewaan;
    }

    public double getLatitude_penjemputan() {
        return latitude_penjemputan;
    }

    public double getLongitude_penjemputan() {
        return longitude_penjemputan;
    }

    public double getTotalBiayaPembayaran() {
        return totalBiayaPembayaran;
    }

    public String getJamPengambilan() {
        return jamPengambilan;
    }

    public String getBatasWaktuPembayaran() {
        return batasWaktuPembayaran;
    }

    public String getKategoriKendaraan() {
        return kategoriKendaraan;
    }

    public String getIdRekeningRental() {
        return idRekeningRental;
    }

    public String getIdPembayaran() {
        return idPembayaran;
    }

    public String getUriFotoBuktiPembayaran() {
        return uriFotoBuktiPembayaran;
    }

    public String getBankPelanggan() {
        return bankPelanggan;
    }

    public String getNamaPemilikRekeningPelanggan() {
        return namaPemilikRekeningPelanggan;
    }

    public String getNomorRekeningPelanggan() {
        return nomorRekeningPelanggan;
    }

    public String getJumlahTransfer() {
        return jumlahTransfer;
    }

    public String getWaktuPembayaran() {
        return waktuPembayaran;
    }

    public String getAlasanPembatalan() {
        return alasanPembatalan;
    }

    public boolean isStatusUlasan() {
        return statusUlasan;
    }
}

