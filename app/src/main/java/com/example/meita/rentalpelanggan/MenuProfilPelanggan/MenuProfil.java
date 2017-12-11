package com.example.meita.rentalpelanggan.MenuProfilPelanggan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class MenuProfil extends Fragment {
    ImageView imageView;
    TextView textViewIdentitasPelanggan, textViewNamaLengkap, textViewAlamatLengkap,
            textViewNomorTelpon, textViewEmail;
    Button buttonUbahProfile;
    private String idPelanggan, emailPelanggan;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private Uri imgUri;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Profil Pelanggan");
        View v = inflater.inflate(R.layout.fragment_menu_profil, container, false);

        imageView = (ImageView)v.findViewById(R.id.imageView);
        textViewIdentitasPelanggan = (TextView)v.findViewById(R.id.textViewIdentitasPelanggan);
        textViewNamaLengkap = (TextView)v.findViewById(R.id.textViewNamaLengkap);
        textViewAlamatLengkap = (TextView)v.findViewById(R.id.textViewAlamatLengkap);
        textViewNomorTelpon = (TextView)v.findViewById(R.id.textViewNomorTelpon);
        textViewEmail = (TextView)v.findViewById(R.id.textViewEmail);
        buttonUbahProfile = (Button) v.findViewById(R.id.buttonUbahProfile);

        buttonUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UbahProfilPelanggan.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();
        emailPelanggan = user.getEmail();

        infoPelanggan();

        return v;
    }

    public void infoPelanggan() {
        mDatabase.child("pelanggan").child(idPelanggan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PelangganModel dataPelanggan = dataSnapshot.getValue(PelangganModel.class);
                Glide.with(getActivity()).load(dataPelanggan.getUriFotoPelanggan()).into(imageView);
                textViewIdentitasPelanggan.setText(dataPelanggan.getNoIdentitas());
                textViewNamaLengkap.setText(dataPelanggan.getNamaLengkap());
                textViewAlamatLengkap.setText(dataPelanggan.getAlamat());
                textViewNomorTelpon.setText(dataPelanggan.getNoTelp());
                textViewEmail.setText(emailPelanggan);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
