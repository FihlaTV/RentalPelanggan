package com.example.meita.rentalpelanggan.MenuStatusPemesanan;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.meita.rentalpelanggan.MenuPemesanan.PenyewaanModel;
import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meita on 2/16/2018.
 */

public class TabStatus7 extends Fragment {
    private RecyclerView recyclerView;
    private TabStatus7Adapter adapter;
    private List<PenyewaanModel> penyewaanModel;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    private String idPelanggan;
    ImageView imageViewNoOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_status7, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        imageViewNoOrder = (ImageView)v.findViewById(R.id.ic_noOrder);

        final FragmentActivity c = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        progressBar = (ProgressBar) v.findViewById(R.id.progress_circle);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);
        imageViewNoOrder.setVisibility(View.GONE);

        penyewaanModel = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();
        Firebase.setAndroidContext(getActivity());

        getDataPenyewaan();

        return v;
    }

    public void getDataPenyewaan() {
        try {
            String status2 = "menungguSisaPembayaran";
            mDatabase.child("penyewaanKendaraan").child(status2).orderByChild("idPelanggan").equalTo(idPelanggan).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        penyewaanModel.clear();
                        imageViewNoOrder.setVisibility(View.GONE);
                        for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            PenyewaanModel dataPemesanan = postSnapshot.getValue(PenyewaanModel.class);
                            penyewaanModel.add(dataPemesanan);
                            adapter = new TabStatus7Adapter(getActivity(), penyewaanModel);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);

                        }
                    } else {
                        imageViewNoOrder.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
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
