package com.example.meita.rentalpelanggan.MenuPencarian;

/**
 * Created by meita on 17/09/2017.
 */

public class RentalModel {
    public String uriFotoProfil;
    public String nama_pemilik, nama_rental, alamat_rental, notelfon_rental,
            kebijakan_sewa_rental, kebijakan_pemesanan_rental,
            kebijakan_pembatalan_rental, idRekening, namaBank,
            namaPemilikBank, nomorRekeningBank;
    public double latitude, longitude;

    public RentalModel() {}


    public RentalModel(String uriFotoProfil, String nama_pemilik, String nama_rental, String alamat_rental, String notelfon_rental,
                      String kebijakan_sewa_rental, String kebijakan_pemesanan_rental, String kebijakan_pembatalan_rental,
                      double latitude, double longitude) {
        this.uriFotoProfil = uriFotoProfil;
        this.nama_pemilik = nama_pemilik;
        this.nama_rental = nama_rental;
        this.alamat_rental = alamat_rental;
        this.notelfon_rental = notelfon_rental;
        this.kebijakan_sewa_rental = kebijakan_sewa_rental;
        this.kebijakan_pemesanan_rental = kebijakan_pemesanan_rental;
        this.kebijakan_pembatalan_rental = kebijakan_pembatalan_rental;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RentalModel(String idRekening, String namaPemilikBank, String nomorRekeningBank, String namaBank) {
        this.idRekening = idRekening;
        this.namaBank = namaBank;
        this.namaPemilikBank = namaPemilikBank;
        this.nomorRekeningBank = nomorRekeningBank;
    }

    public String getUriFotoProfil() {
        return uriFotoProfil;
    }

    public void setUriFotoProfil(String uriFotoProfil) {
        this.uriFotoProfil = uriFotoProfil;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getNama_rental() {
        return nama_rental;
    }

    public void setNama_rental(String nama_rental) {
        this.nama_rental = nama_rental;
    }

    public String getAlamat_rental() {
        return alamat_rental;
    }

    public void setAlamat_rental(String alamat_rental) {
        this.alamat_rental = alamat_rental;
    }

    public String getNotelfon_rental() {
        return notelfon_rental;
    }

    public void setNotelfon_rental(String notelfon_rental) {
        this.notelfon_rental = notelfon_rental;
    }

    public String getKebijakan_sewa_rental() {
        return kebijakan_sewa_rental;
    }

    public void setKebijakan_sewa_rental(String kebijakan_sewa_rental) {
        this.kebijakan_sewa_rental = kebijakan_sewa_rental;
    }

    public String getKebijakan_pemesanan_rental() {
        return kebijakan_pemesanan_rental;
    }

    public void setKebijakan_pemesanan_rental(String kebijakan_pemesanan_rental) {
        this.kebijakan_pemesanan_rental = kebijakan_pemesanan_rental;
    }

    public String getKebijakan_pembatalan_rental() {
        return kebijakan_pembatalan_rental;
    }

    public void setKebijakan_pembatalan_rental(String kebijakan_pembatalan_rental) {
        this.kebijakan_pembatalan_rental = kebijakan_pembatalan_rental;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public void setNamaBank(String namaBank) {
        this.namaBank = namaBank;
    }

    public String getNamaPemilikBank() {
        return namaPemilikBank;
    }

    public void setNamaPemilikBank(String namaPemilikBank) {
        this.namaPemilikBank = namaPemilikBank;
    }

    public String getNomorRekeningBank() {
        return nomorRekeningBank;
    }

    public void setNomorRekeningBank(String nomorRekeningBank) {
        this.nomorRekeningBank = nomorRekeningBank;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIdRekening() {
        return idRekening;
    }
}