package com.example.meita.rentalpelanggan.MenuPemesanan;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by meita on 17/09/2017.
 */

public class PenyewaanModel implements Serializable{
    public String idPenyewaan, idKendaraan, idPelanggan, idRental, statusPenyewaan, tglPembuatanPenyewaan, tglSewa, tglKembali,
            keteranganKhusus, jamPenjemputan, alamatPenjemputan;
    public int jumlahKendaraan, jumlahHariPenyewaan;
    public double latitude_penjemputan, longitude_penjemputan, totalBiayaPembayaran;
    public String jamPengambilan, batasWaktuPembayaran, kategoriKendaraan, idRekeningRental;
    String idPembayaran, uriFotoBuktiPembayaran,
            bankPelanggan, namaPemilikRekeningPelanggan, nomorRekeningPelanggan, jumlahTransfer, waktuPembayaran;
    String alasanPembatalan;
    boolean statusUlasan;
    String idPemberitahuan, nilaiHalaman;
    String namaBankRental, namaRekeningRental, nomorRekeningRental, jumlahTransferPengembalian, uriBuktiPengembalian, waktuTransferPengembalian;
    String keteranganSisaPembayaran;

    public PenyewaanModel() {

    }

    public PenyewaanModel(String idPemberitahuan, String idPelanggan, String idRental, String idPenyewaan, String idKendaraan, String statusPenyewaan, String tglSewa,
                          String tglKembali, String nilaiHalaman) {
        this.idPemberitahuan = idPemberitahuan;
        this.idPelanggan = idPelanggan;
        this.idRental = idRental;
        this.idPenyewaan = idPenyewaan;
        this.idKendaraan = idKendaraan;
        this.statusPenyewaan = statusPenyewaan;
        this.tglSewa = tglSewa;
        this.tglKembali = tglKembali;
        this.nilaiHalaman = nilaiHalaman;
    }

    public PenyewaanModel(String idPenyewaan, String idKendaraan, String idPelanggan, String idRental, String statusPenyewaan,
                          String tglPembuatanPenyewaan, String tglSewa, String tglKembali, String keteranganKhusus, String jamPengambilan,
                          int jumlahKendaraan, int jumlahHariPenyewaan, double totalBiayaPembayaran, String batasWaktuPembayaran, String kategoriKendaraan,
                          String idRekeningRental, boolean statusUlasan) {
        this.idPenyewaan = idPenyewaan;
        this.idKendaraan = idKendaraan;
        this.idPelanggan = idPelanggan;
        this.idRental = idRental;
        this.statusPenyewaan = statusPenyewaan;
        this.tglPembuatanPenyewaan = tglPembuatanPenyewaan;
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

    public PenyewaanModel(String idPenyewaan, String idKendaraan, String idPelanggan, String idRental, String statusPenyewaan,
                          String tglPembuatanPenyewaan, String tglSewa, String tglKembali, String keteranganKhusus, String jamPenjemputan,
                          int jumlahKendaraan, double latitude_penjemputan, double longitude_penjemputan, String alamatPenjemputan,
                          int jumlahHariPenyewaan, double totalBiayaPembayaran, String batasWaktuPembayaran, String kategoriKendaraan, String idRekeningRental, boolean statusUlasan) {
        this.idPenyewaan = idPenyewaan;
        this.idKendaraan = idKendaraan;
        this.idPelanggan = idPelanggan;
        this.idRental = idRental;
        this.statusPenyewaan = statusPenyewaan;
        this.tglPembuatanPenyewaan = tglPembuatanPenyewaan;
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
        this.tglPembuatanPenyewaan = tglPembuatanPenyewaan;
        this.batasWaktuPembayaran = batasWaktuPembayaran;
        this.kategoriKendaraan = kategoriKendaraan;
        this.idRekeningRental = idRekeningRental;
        this.statusUlasan = statusUlasan;
    }

    public String getidPenyewaan() {
        return idPenyewaan;
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

    public String getstatusPenyewaan() {
        return statusPenyewaan;
    }

    public String gettglPembuatanPenyewaan() {
        return tglPembuatanPenyewaan;
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

    public String getNamaBankRental() {
        return namaBankRental;
    }

    public String getNamaRekeningRental() {
        return namaRekeningRental;
    }

    public String getNomorRekeningRental() {
        return nomorRekeningRental;
    }

    public String getJumlahTransferPengembalian() {
        return jumlahTransferPengembalian;
    }

    public String getUriBuktiPengembalian() {
        return uriBuktiPengembalian;
    }

    public String getWaktuTransferPengembalian() {
        return waktuTransferPengembalian;
    }

    public String getKeteranganSisaPembayaran() {
        return keteranganSisaPembayaran;
    }
}

