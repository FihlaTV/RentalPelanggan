package com.example.meita.rentalpelanggan.MenuUlasan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LihatUlasanPelanggan extends AppCompatActivity {
    RatingBar rb_kualitas_pelayanan, rb_kualitas_kendaraan;
    TextView textViewUlasan;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_ulasan_pelanggan);

        rb_kualitas_pelayanan = (RatingBar)findViewById(R.id.rb_kualitas_pelayanan);
        rb_kualitas_kendaraan = (RatingBar)findViewById(R.id.rb_kualitas_kendaraan);
        textViewUlasan = (TextView) findViewById(R.id.textViewUlasan);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String idRental = getIntent().getStringExtra("idRental");
        final String idPelanggan = getIntent().getStringExtra("idPelanggan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
//        idPemesanan = getIntent().getStringExtra("idPemesanan");
//        idKendaraan = getIntent().getStringExtra("idKendaraan");
//        idRental = getIntent().getStringExtra("idRental");
//        idPelanggan = getIntent().getStringExtra("idPelanggan");
//        kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        tampilUlasan();
    }

    private void tampilUlasan(){
        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        mDatabase.child("ulasan").child(idPemesanan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UlasanModel dataUlasan = dataSnapshot.getValue(UlasanModel.class);
                    try {
                        rb_kualitas_kendaraan.setRating(dataUlasan.getRatingKendaraan());
                        rb_kualitas_pelayanan.setRating(dataUlasan.getRatingPelayanan());
                        textViewUlasan.setText(dataUlasan.getUlasan());
                    }catch (Exception e){

                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
