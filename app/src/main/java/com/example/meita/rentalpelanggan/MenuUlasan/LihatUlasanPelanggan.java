package com.example.meita.rentalpelanggan.MenuUlasan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    TextView textViewUlasan, textViewWaktuUlasan;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_ulasan_pelanggan);
        setTitle("Ulasan Anda");

        rb_kualitas_pelayanan = (RatingBar)findViewById(R.id.rb_kualitas_pelayanan);
        rb_kualitas_kendaraan = (RatingBar)findViewById(R.id.rb_kualitas_kendaraan);
        textViewUlasan = (TextView) findViewById(R.id.textViewUlasan);
        textViewWaktuUlasan = (TextView) findViewById(R.id.textViewWaktuUlasan);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String idRental = getIntent().getStringExtra("idRental");
        final String idPelanggan = getIntent().getStringExtra("idPelanggan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        tampilUlasan();
    }

    private void tampilUlasan(){
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        mDatabase.child("ulasan").child(idPenyewaan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UlasanModel dataUlasan = dataSnapshot.getValue(UlasanModel.class);
                    try {
                        rb_kualitas_kendaraan.setRating(dataUlasan.getRatingKendaraan());
                        rb_kualitas_pelayanan.setRating(dataUlasan.getRatingPelayanan());
                        textViewUlasan.setText(dataUlasan.getUlasan());
                        textViewWaktuUlasan.setText(dataUlasan.getWaktuUlasan());
                    }catch (Exception e){

                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
