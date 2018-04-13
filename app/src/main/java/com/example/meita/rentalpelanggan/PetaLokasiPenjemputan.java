package com.example.meita.rentalpelanggan;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MenuPemesanan.PenyewaanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetaLokasiPenjemputan extends AppCompatActivity implements OnMapReadyCallback {
    TextView textViewLokasiPenjemputanValue;
    private GoogleMap mMap;
    Context context;
    DatabaseReference mDatabase;
    private Fragment mMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_lokasi_penjemputan);
        setTitle("Peta Lokasi Penjemputan");
        textViewLokasiPenjemputanValue = (TextView)findViewById(R.id.textViewLokasiPenjemputanValue);

//        Firebase.setAndroidContext(getApplication());
//
//        FragmentManager fm = getSupportFragmentManager();
//        mMapFragment = fm.findFragmentById(R.id.mapLokasiPenjemputan);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLokasiPenjemputan);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        infoPenyewaan();

    }

    public void infoPenyewaan() {
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPenyewaan = getIntent().getStringExtra("statusPenyewaan");
        try {
            mDatabase.child("penyewaanKendaraan").child(statusPenyewaan).child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel infoPenyewaan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewLokasiPenjemputanValue.setText(infoPenyewaan.getAlamatPenjemputan());

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPenyewaan = getIntent().getStringExtra("statusPenyewaan");
        try {
            mDatabase.child("penyewaanKendaraan").child(statusPenyewaan).child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel penyewaanModel = dataSnapshot.getValue(PenyewaanModel.class);
                        LatLng lokasiPenjemputan = new LatLng(penyewaanModel.getLatitude_penjemputan(), penyewaanModel.getLongitude_penjemputan());
                        mMap.addMarker(new MarkerOptions().position(lokasiPenjemputan).title("Lokasi Penjemputan"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(penyewaanModel.getLatitude_penjemputan(), penyewaanModel.getLongitude_penjemputan()), 14.0f));
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
