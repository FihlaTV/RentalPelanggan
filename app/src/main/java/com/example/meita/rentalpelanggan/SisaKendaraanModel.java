package com.example.meita.rentalpelanggan;

/**
 * Created by meita on 12/20/2017.
 */

public class SisaKendaraanModel {
    String idCekSisa, tglSewa, tglKembali, idKendaraan;
    int sisaKendaraan;

    public SisaKendaraanModel(){}

    public SisaKendaraanModel(String idCekSisa, String tglSewa, String tglKembali, String idKendaraan, int sisaKendaraan) {
        this.idCekSisa = idCekSisa;
        this.tglSewa = tglSewa;
        this.tglKembali = tglKembali;
        this.idKendaraan = idKendaraan;
        this.sisaKendaraan = sisaKendaraan;
    }

    public String getIdCekSisa() {
        return idCekSisa;
    }

    public String getTglSewa() {
        return tglSewa;
    }

    public String getTglKembali() {
        return tglKembali;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public int getSisaKendaraan() {
        return sisaKendaraan;
    }
}
