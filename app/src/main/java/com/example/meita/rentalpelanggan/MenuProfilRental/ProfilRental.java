package com.example.meita.rentalpelanggan.MenuProfilRental;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.MenuStatusPemesanan.DetailPemesananStatus1;
import com.example.meita.rentalpelanggan.MenuUlasan.DaftarUlasan;
import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilRental extends AppCompatActivity {
    ImageView imageView;
    TextView textViewNamaRental, textViewAlamatRental, textViewTelpRental, textViewEmailRental;
    DatabaseReference mDatabase;
    Button buttonLihatPenilaian;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profil Rental");
        setContentView(R.layout.activity_profil_rental);

        imageView = (ImageView)findViewById(R.id.imageView);
        textViewNamaRental = (TextView)findViewById(R.id.textViewNamaRental);
        textViewAlamatRental = (TextView)findViewById(R.id.textViewAlamatRental);
        textViewTelpRental = (TextView)findViewById(R.id.textViewTelpRental);
        textViewEmailRental = (TextView)findViewById(R.id.textViewEmailRental);
        buttonLihatPenilaian = (Button)findViewById(R.id.btnLihatPenilaian);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        buttonLihatPenilaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idRental = getIntent().getStringExtra("idRental");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ProfilRental.this, DaftarUlasan.class);
                bundle.putString("idRental", idRental);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        infoRental();
    }

    public void infoRental() {
        final String idRental = getIntent().getStringExtra("idRental");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                textViewNamaRental.setText(dataRental.getNama_rental());
                textViewAlamatRental.setText(dataRental.getAlamat_rental());
                textViewTelpRental.setText(dataRental.getNotelfon_rental());
                Glide.with(getApplication()).load(dataRental.uriFotoProfil).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
