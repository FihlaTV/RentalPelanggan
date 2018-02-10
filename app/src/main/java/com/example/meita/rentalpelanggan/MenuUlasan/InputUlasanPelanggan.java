package com.example.meita.rentalpelanggan.MenuUlasan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.R;
import com.example.meita.rentalpelanggan.Utils.ShowAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputUlasanPelanggan extends AppCompatActivity {
    RatingBar rb_kualitas_pelayanan, rb_kualitas_kendaraan;
    EditText editTextUlasan;
    Button buttonSimpanUlasan;
    private DatabaseReference mDatabase;
    String idPelanggan;
    FirebaseAuth auth;
    float ratingKendaraan;
    float ratingPelayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_ulasan_pelanggan);
        setTitle("Penilaian Pelanggan");

        rb_kualitas_pelayanan = (RatingBar)findViewById(R.id.rb_kualitas_pelayanan);
        rb_kualitas_kendaraan = (RatingBar)findViewById(R.id.rb_kualitas_kendaraan);
        editTextUlasan = (EditText)findViewById(R.id.editTextUlasan);
        buttonSimpanUlasan = (Button)findViewById(R.id.btnSimpanUlasan);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();

        setRatingBar();

        buttonSimpanUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekKolomIsian()) {
                    simpanUlasan();
                    finish();
                }
            }
        });

    }
    private void setRatingBar(){
        rb_kualitas_kendaraan.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 5: {
                        // txtNilaiProduk.setText(getResources().getString(R.string.lbl_sangat_bagus));
                        break;
                    }
                    case 4: {
                        // txtNilaiProduk.setText(getResources().getString(R.string.lbl_bagus));
                        break;
                    }
                    case 3: {
                        //   txtNilaiProduk.setText(getResources().getString(R.string.lbl_biasa));
                        break;
                    }
                    case 2: {
                        //   txtNilaiProduk.setText(getResources().getString(R.string.lbl_buruk));
                        break;
                    }
                    case 1: {
                        //   txtNilaiProduk.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                    default: {
                        ratingBar.setRating(1);
                        //   txtNilaiProduk.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                }
            }
        });

        rb_kualitas_pelayanan.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 5: {
                        //txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_sangat_bagus));
                        break;
                    }
                    case 4: {
                        //txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_bagus));
                        break;
                    }
                    case 3: {
                        //txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_biasa));
                        break;
                    }
                    case 2: {
                        //   txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_buruk));
                        break;
                    }
                    case 1: {
                        // txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                    default: {
                        ratingBar.setRating(1);
                        // txtNilaiPelayanan.setText(getResources().getString(R.string.lbl_sangat_buruk));
                        break;
                    }
                }
            }
        });
    }

//    private void submitData(){
//        String ulasanText = editTextUlasan.getText().toString();
//        buatUlasan(ulasanText);
//    }

    private void simpanUlasan(){
        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String idRental = getIntent().getStringExtra("idRental");
        final String idPelanggan = getIntent().getStringExtra("idPelanggan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        String ulasanText = editTextUlasan.getText().toString();
        String waktuPembuatanUlasan = DateFormat.getDateTimeInstance().format(new Date());
        String idUlasan = mDatabase.push().getKey();
        ratingKendaraan = rb_kualitas_kendaraan.getRating();
        ratingPelayanan = rb_kualitas_pelayanan.getRating();

        boolean statusUlasan = true;

        UlasanModel dataUlasan = new UlasanModel(kategoriKendaraan, idPelanggan, idRental, idUlasan, idPemesanan,
                idKendaraan, ulasanText, ratingKendaraan, ratingPelayanan, waktuPembuatanUlasan);
//        Map<String, Object> dataUlasan = new HashMap<>();
//        dataUlasan.put("waktuUlasan", waktuUlasan);
//        dataUlasan.put("ulasan", ulasan);
//        dataUlasan.put("ratingKendaraan", rb_kualitas_kendaraan.getRating());
//        dataUlasan.put("ratingPelayanan", rb_kualitas_pelayanan.getRating());
//        dataUlasan.put("idUlasan", ulasanId);
//        dataUlasan.put("idPelanggan", idPelanggan);
//        dataUlasan.put("idPemilik", idRental);
//        dataUlasan.put("idPemesanan", idPemesanan);
//        dataUlasan.put("idKategori", kategoriKendaraan);
//        dataUlasan.put("idKendaraan", idKendaraan);

        mDatabase.child("ulasan").child(idPemesanan).setValue(dataUlasan);
        mDatabase.child("pemesananKendaraan").child("selesai").child(idPemesanan).child("statusUlasan").setValue(statusUlasan);
        Intent intent = new Intent(InputUlasanPelanggan.this, MainActivity.class);
        startActivity(intent);
        buatPemberitahuan();
    }

    private void buatPemberitahuan() {
        String idPemberitahuan = mDatabase.push().getKey();
        final String idRental = getIntent().getStringExtra("idRental");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        //int valueHalaman1 = 0;
        String valueHalaman1 = "penilaian";
        String statusPemesanan1 = "selesai";
        HashMap<String, Object> dataNotif = new HashMap<>();
        dataNotif.put("idPemberitahuan", idPemberitahuan);
        dataNotif.put("idRental", idRental);
        dataNotif.put("idKendaraan", idKendaraan);
        dataNotif.put("tglSewa", tglSewaPencarian);
        dataNotif.put("tglKembalian", tglKembaliPencarian);
        dataNotif.put("nilaiHalaman", valueHalaman1);
        dataNotif.put("statusPemesanan", statusPemesanan1);
        dataNotif.put("idPelanggan", idPelanggan);
        dataNotif.put("idPemesanan", idPemesanan);
        mDatabase.child("pemberitahuan").child("rental").child("penilaian").child(idRental).child(idPemberitahuan).setValue(dataNotif);
        //mDatabase.child("pemberitahuan").child("rental").child("belumBayar").child(idRental).child(idPemberitahuan).child("nilaiHalaman").setValue(valueHalaman);
    }

    public boolean cekKolomIsian() {
        boolean sukses;
        if (TextUtils.isEmpty(editTextUlasan.getText().toString())) {
            sukses = false;
            ShowAlertDialog.showAlert("Lengkapi Seluruh Kolom Isian", this);
        } else {
            sukses = true;
        }
        return sukses;
    }
}
