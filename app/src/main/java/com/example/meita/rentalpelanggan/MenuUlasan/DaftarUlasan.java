package com.example.meita.rentalpelanggan.MenuUlasan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_ulasan);
        Firebase.setAndroidContext(this);

        recyclerView = (RecyclerView) findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        UlasanModel dataUlasan = postSnapshot.getValue(UlasanModel.class);

                        ulasanModel.add(dataUlasan);
                        adapter = new DaftarUlasanAdapter(getApplication(), ulasanModel);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
}
