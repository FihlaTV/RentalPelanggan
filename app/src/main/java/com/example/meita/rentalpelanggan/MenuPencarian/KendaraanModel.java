package com.example.meita.rentalpelanggan.MenuPencarian;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by meita on 29/08/2017.
 */

public class KendaraanModel implements Serializable {
    public String idRental, idKendaraan, kategoriKendaraan, tipeKendaraan, fasilitasKendaraan,
            lamaPenyewaan, jumlahPenumpang, areaPemakaian;
    public int jumlahKendaraan;
    boolean supir, bahanBakar;
    double hargaSewa;
    ArrayList<String> uriFotoKendaraan = new ArrayList<>();

    public KendaraanModel() {

    }


    public void setUriFotoKendaraan(ArrayList<String> uriFotoKendaraan) {
        this.uriFotoKendaraan = uriFotoKendaraan;
    }

    public ArrayList<String> getUriFotoKendaraan() {
        return uriFotoKendaraan;
    }

    public String getIdRental() {
        return idRental;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public String getKategoriKendaraan() {
        return kategoriKendaraan;
    }

    public String getTipeKendaraan() {
        return tipeKendaraan;
    }

    public String getFasilitasKendaraan() {
        return fasilitasKendaraan;
    }

    public double getHargaSewa() {
        return hargaSewa;
    }

    public String getLamaPenyewaan() {
        return lamaPenyewaan;
    }

    public String getJumlahPenumpang() {
        return jumlahPenumpang;
    }

    public Integer getJumlahKendaraan() {
        return jumlahKendaraan;
    }

    public boolean isSupir() {
        return supir;
    }

    public String getAreaPemakaian() {
        return areaPemakaian;
    }

    public boolean isBahanBakar() {
        return bahanBakar;
    }
}

