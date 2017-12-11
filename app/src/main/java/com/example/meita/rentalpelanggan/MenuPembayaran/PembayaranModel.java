package com.example.meita.rentalpelanggan.MenuPembayaran;

/**
 * Created by meita on 10/27/2017.
 */

public class PembayaranModel {
    String idPembayaran, idRekeningRental, uriFotoBuktiPembayaran,
            bankPelanggan, namaPemilikRekeningPelanggan, nomorRekeningPelanggan, jumlahTransfer, waktuPembayaran;

    public PembayaranModel() {

    }

    public PembayaranModel(String idPembayaran, String idRekeningRental, String uriFotoBuktiPembayaran,
                           String bankPelanggan, String namaPemilikRekeningPelanggan, String nomorRekeningPelanggan,
                           String jumlahTransfer, String waktuPembayaran) {
        this.idPembayaran = idPembayaran;
        this.idRekeningRental = idRekeningRental;
        this.uriFotoBuktiPembayaran = uriFotoBuktiPembayaran;
        this.bankPelanggan = bankPelanggan;
        this.namaPemilikRekeningPelanggan = namaPemilikRekeningPelanggan;
        this.nomorRekeningPelanggan = nomorRekeningPelanggan;
        this.jumlahTransfer = jumlahTransfer;
        this.waktuPembayaran = waktuPembayaran;
    }

    public String getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(String idPembayaran) {
        this.idPembayaran = idPembayaran;
    }

    public String getIdRekeningRental() {
        return idRekeningRental;
    }

    public void setIdRekeningRental(String idRekeningRental) {
        this.idRekeningRental = idRekeningRental;
    }

    public String getUriFotoBuktiPembayaran() {
        return uriFotoBuktiPembayaran;
    }

    public void setUriFotoBuktiPembayaran(String uriFotoBuktiPembayaran) {
        this.uriFotoBuktiPembayaran = uriFotoBuktiPembayaran;
    }

    public String getBankPelanggan() {
        return bankPelanggan;
    }

    public void setBankPelanggan(String bankPelanggan) {
        this.bankPelanggan = bankPelanggan;
    }

    public String getNamaPemilikRekeningPelanggan() {
        return namaPemilikRekeningPelanggan;
    }

    public void setNamaPemilikRekeningPelanggan(String namaPemilikRekeningPelanggan) {
        this.namaPemilikRekeningPelanggan = namaPemilikRekeningPelanggan;
    }

    public String getNomorRekeningPelanggan() {
        return nomorRekeningPelanggan;
    }

    public void setNomorRekeningPelanggan(String nomorRekeningPelanggan) {
        this.nomorRekeningPelanggan = nomorRekeningPelanggan;
    }

    public String getJumlahTransfer() {
        return jumlahTransfer;
    }

    public void setJumlahTransfer(String jumlahTransfer) {
        this.jumlahTransfer = jumlahTransfer;
    }

    public String getWaktuPembayaran() {
        return waktuPembayaran;
    }
}
