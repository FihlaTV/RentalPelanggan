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
import android.widget.Toast;

import com.example.meita.rentalpelanggan.MenuPemesanan.PemesananModel;
import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TabStatus1 extends Fragment {
    private RecyclerView recyclerView;
    private TabStatus1Adapter adapter;
    private List<PemesananModel> pemesananModel;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    String idPelanggan;
    ImageView ic_noOrder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_status1, container, false);
        Firebase.setAndroidContext(getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        ic_noOrder = (ImageView)v.findViewById(R.id.ic_noOrder);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final FragmentActivity c = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        progressBar = (ProgressBar)v.findViewById(R.id.progress_circle);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);
        ic_noOrder.setVisibility(View.GONE);
        pemesananModel = new ArrayList<>();


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();

        getDataPemesanan();

        return v;
    }

    public void getDataPemesanan() {
        try {
            String status1 = "belumBayar";
            mDatabase.child("pemesananKendaraan").child(status1).orderByChild("idPelanggan").equalTo(idPelanggan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            PemesananModel dataPemesanan = postSnapshot.getValue(PemesananModel.class);
                            pemesananModel.add(dataPemesanan);
                            adapter = new TabStatus1Adapter(getActivity(), pemesananModel);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                            ic_noOrder.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        ic_noOrder.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
