package com.example.meita.rentalpelanggan.MenuUlasan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaftarUlasan extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<UlasanModel> ulasanModel;
    private DaftarUlasanAdapter adapter;
    private DatabaseReference mDatabase;
    ImageView ic_no_penilaian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_ulasan);
        setTitle("Penilaian Rental Kendaraan");
        Firebase.setAndroidContext(this);

        ic_no_penilaian= (ImageView)findViewById(R.id.ic_noPenilaian);
        ic_no_penilaian.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ulasanModel = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getUlasan();
    }

    public void getUlasan() {
        final String idRental = getIntent().getStringExtra("idRental");
        try {
            mDatabase.child("ulasan").orderByChild("idRental").equalTo(idRental).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            UlasanModel dataUlasan = postSnapshot.getValue(UlasanModel.class);

                            ulasanModel.add(dataUlasan);
                            adapter = new DaftarUlasanAdapter(getApplication(), ulasanModel);
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        ic_no_penilaian.setVisibility(View.VISIBLE);
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
