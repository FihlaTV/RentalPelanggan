package com.example.meita.rentalpelanggan.MenuStatusPemesanan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GambarBuktiPembayaran extends AppCompatActivity {

    ImageView imageViewBuktiPembayaran;
    DatabaseReference mDatabase;
    ProgressBar progress_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambar_bukti_pembayaran);
        setTitle("Bukti Pembayaran");
        progress_circle = (ProgressBar)findViewById(R.id.progress_circle);

        imageViewBuktiPembayaran = (ImageView)findViewById(R.id.imageViewBuktiPembayaran);

        progress_circle.setVisibility(View.VISIBLE);
        imageViewBuktiPembayaran.setVisibility(View.GONE);

        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPenyewaan = getIntent().getStringExtra("statusPenyewaan");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        try {
            mDatabase.child("penyewaanKendaraan").child(statusPenyewaan).child(idPenyewaan).child("pembayaran").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progress_circle.setVisibility(View.GONE);
                    imageViewBuktiPembayaran.setVisibility(View.VISIBLE);
                    PembayaranModel dataPembayaran = dataSnapshot.getValue(PembayaranModel.class);
                    Glide.with(getApplication()).load(dataPembayaran.getUriFotoBuktiPembayaran()).into(imageViewBuktiPembayaran);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }


    }
}
