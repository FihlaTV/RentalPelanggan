package com.example.meita.rentalpelanggan;

/**
 * Created by meita on 11/1/2017.
 */

public class PengembalianDanaModel {
    String alasanPembatalan,namaBankRental, namaRekeningRental, nomorRekeningRental, jumlahTransferPengembalian, uriBuktiPengembalian, waktuTransferPengembalian;

    public void PengembalianDanaModel() {}

    public PengembalianDanaModel(String alasanPembatalan, String namaBankRental, String namaRekeningRental, String nomorRekeningRental, String jumlahTransferPengembalian, String uriBuktiPengembalian, String waktuTransferPengembalian) {
        this.alasanPembatalan = alasanPembatalan;
        this.namaBankRental = namaBankRental;
        this.namaRekeningRental = namaRekeningRental;
        this.nomorRekeningRental = nomorRekeningRental;
        this.jumlahTransferPengembalian = jumlahTransferPengembalian;
        this.uriBuktiPengembalian = uriBuktiPengembalian;
        this.waktuTransferPengembalian = waktuTransferPengembalian;
    }

    public String getAlasanPembatalan() {
        return alasanPembatalan;
    }

    public void setAlasanPembatalan(String alasanPembatalan) {
        this.alasanPembatalan = alasanPembatalan;
    }

    public String getNamaBankRental() {
        return namaBankRental;
    }

    public void setNamaBankRental(String namaBankRental) {
        this.namaBankRental = namaBankRental;
    }

    public String getNamaRekeningRental() {
        return namaRekeningRental;
    }

    public void setNamaRekeningRental(String namaRekeningRental) {
        this.namaRekeningRental = namaRekeningRental;
    }

    public String getNomorRekeningRental() {
        return nomorRekeningRental;
    }

    public void setNomorRekeningRental(String nomorRekeningRental) {
        this.nomorRekeningRental = nomorRekeningRental;
    }

    public String getJumlahTransferPengembalian() {
        return jumlahTransferPengembalian;
    }

    public void setJumlahTransferPengembalian(String jumlahTransferPengembalian) {
        this.jumlahTransferPengembalian = jumlahTransferPengembalian;
    }

    public String getUriBuktiPengembalian() {
        return uriBuktiPengembalian;
    }

    public void setUriBuktiPengembalian(String uriBuktiPengembalian) {
        this.uriBuktiPengembalian = uriBuktiPengembalian;
    }

    public String getWaktuTransferPengembalian() {
        return waktuTransferPengembalian;
    }

    public void setWaktuTransferPengembalian(String waktuTransferPengembalian) {
        this.waktuTransferPengembalian = waktuTransferPengembalian;
    }
}
