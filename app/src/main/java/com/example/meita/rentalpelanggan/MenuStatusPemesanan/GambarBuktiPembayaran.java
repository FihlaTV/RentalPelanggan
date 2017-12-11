package com.example.meita.rentalpelanggan.MenuStatusPemesanan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambar_bukti_pembayaran);
        setTitle("Bukti Pembayaran");

        imageViewBuktiPembayaran = (ImageView)findViewById(R.id.imageViewBuktiPembayaran);

        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("pemesananKendaraan").child("menungguKonfirmasiRental").child(idPemesanan).child("pembayaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PembayaranModel dataPembayaran = dataSnapshot.getValue(PembayaranModel.class);
                Glide.with(getApplication()).load(dataPembayaran.getUriFotoBuktiPembayaran()).into(imageViewBuktiPembayaran);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
