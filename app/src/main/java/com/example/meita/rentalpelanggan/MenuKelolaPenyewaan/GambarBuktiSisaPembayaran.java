package com.example.meita.rentalpelanggan.MenuKelolaPenyewaan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.meita.rentalpelanggan.MenuPembayaran.PembayaranModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GambarBuktiSisaPembayaran extends AppCompatActivity {

    ImageView imageViewBuktiPembayaran;
    DatabaseReference mDatabase;
    ProgressBar progress_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambar_bukti_pembayaran);
        setTitle("Bukti Sisa Pembayaran");
        progress_circle = (ProgressBar)findViewById(R.id.progress_circle);

        imageViewBuktiPembayaran = (ImageView)findViewById(R.id.imageViewBuktiPembayaran);

        progress_circle.setVisibility(View.VISIBLE);
        imageViewBuktiPembayaran.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPenyewaan = getIntent().getStringExtra("statusPenyewaan");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        try {
            mDatabase.child("penyewaanKendaraan").child(statusPenyewaan).child(idPenyewaan).child("pembayaran").child("sisaPembayaran").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        progress_circle.setVisibility(View.GONE);
                        imageViewBuktiPembayaran.setVisibility(View.VISIBLE);
                        PembayaranModel dataPembayaran = dataSnapshot.getValue(PembayaranModel.class);
                        Glide.with(getApplication()).load(dataPembayaran.getUriFotoBuktiPembayaran()).into(imageViewBuktiPembayaran);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
