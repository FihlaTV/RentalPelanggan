package com.example.meita.rentalpelanggan.MenuPencarian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Base.AdapterImagePager;
import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MenuPemesanan.RincianPesanan;
import com.example.meita.rentalpelanggan.MenuProfilRental.ProfilRental;
import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by meita on 29/08/2017.
 */

public class Tab2_DetailKendaraan extends Fragment implements OnMapReadyCallback{

    private DatabaseReference mDatabase;
    Context context;
    TextView tipeKendaraan, jmlPenumpang, hargaSewa, fasilitas, namaRental, lokasiRental, telponRental;
    private GoogleMap mMap;
    ImageView fotoKendaraan;
    Button buttonPesan, buttonProfilRental;
    ViewPager viewPager;
    private AdapterImagePager mViewImagePager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Detail Kendaraan");
        View v = inflater.inflate(R.layout.fragment_tab2_detailkendaraan, container, false);

        tipeKendaraan = (TextView)v.findViewById(R.id.tipe_kendaraan);
        jmlPenumpang = (TextView)v.findViewById(R.id.jml_penumpang);
        hargaSewa = (TextView)v.findViewById(R.id.harga_sewa);
        fasilitas = (TextView)v.findViewById(R.id.deskripsiFasilitas);
        namaRental = (TextView)v.findViewById(R.id.nama_rental);
        lokasiRental = (TextView)v.findViewById(R.id.lokasi_rental);
        telponRental = (TextView)v.findViewById(R.id.noTelponRental);
        fotoKendaraan = (ImageView)v.findViewById(R.id.imageViewFotoKendaraan);
        buttonPesan = (Button)v.findViewById(R.id.btnPesan);
        buttonProfilRental = (Button)v.findViewById(R.id.btnProfilRental);
        viewPager = (ViewPager)v.findViewById(R.id.viewPager);


        Bundle bundle = getArguments();
        final String idRental = bundle.getString("idRental");
        final String idKendaraan = bundle.getString("idKendaraan");
        final String kategoriKendaraan = bundle.getString("kategoriKendaraan");
        final String tglSewaPencarian = bundle.getString("tglSewaPencarian");
        final String tglKembaliPencarian = bundle.getString("tglKembaliPencarian");
        final String jumlahKendaraanPencarian = bundle.getString("jumlahKendaraanPencarian");

        final FragmentActivity c = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        Firebase.setAndroidContext(getActivity());

        ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapRental)).getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), RincianPesanan.class);
                bundle.putString("idKendaraan", idKendaraan);
                bundle.putString("idRental", idRental);
                bundle.putString("kategoriKendaraan", kategoriKendaraan);
                bundle.putString("tglSewaPencarian", tglSewaPencarian);
                bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
                bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        buttonProfilRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), ProfilRental.class);
                bundle.putString("idKendaraan", idKendaraan);
                bundle.putString("idRental", idRental);
                bundle.putString("kategoriKendaraanPencarian", kategoriKendaraan);
                bundle.putString("tglSewaPencarian", tglSewaPencarian);
                bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
                bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        infoKendaraan();
        infoRentalKendaraan();
        loadFotoKendaraan();
        return v;
    }

    public void infoKendaraan() {
        Bundle bundle = getArguments();
        final String idKendaraan = bundle.getString("idKendaraan");
        final String kategoriKendaraan = bundle.getString("kategoriKendaraan");

        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
                tipeKendaraan.setText(kendaraan.getTipeKendaraan());
                jmlPenumpang.setText(kendaraan.getJumlahPenumpang());
                fasilitas.setText(kendaraan.getFasilitasKendaraan());
                hargaSewa.setText("Rp." + BaseActivity.rupiah().format(kendaraan.getHargaSewa()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void infoRentalKendaraan() {
        Bundle bundle = getArguments();
        final String idRental = bundle.getString("idRental");

        mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                RentalModel rental = dataSnapshot.getValue(RentalModel.class);
                namaRental.setText(rental.getNama_rental());
                lokasiRental.setText(rental.getAlamat_rental());
                telponRental.setText(rental.getNotelfon_rental());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadFotoKendaraan() {
        Bundle bundle = getArguments();
        final String idKendaraan = bundle.getString("idKendaraan");
        final String kategoriKendaraan = bundle.getString("kategoriKendaraan");
        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
                if (dataKendaraan.getUriFotoKendaraan().size() > 0) {
                    fotoKendaraan.setVisibility(View.GONE);
                    mViewImagePager = new AdapterImagePager(getActivity(), dataKendaraan.getUriFotoKendaraan());
                    viewPager.setAdapter(mViewImagePager);
                } else {
                    viewPager.setVisibility(View.GONE);
                    fotoKendaraan.setImageResource(R.drawable.no_image);
                }
                mViewImagePager = new AdapterImagePager(getActivity(), dataKendaraan.getUriFotoKendaraan());
                viewPager.setAdapter(mViewImagePager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getArguments();
        final String idRental = bundle.getString("idRental");
        mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                RentalModel rental = dataSnapshot.getValue(RentalModel.class);
                LatLng lokasiRental = new LatLng(rental.getLatitude(), rental.getLongitude());
                mMap.addMarker(new MarkerOptions().position(lokasiRental).title(rental.getNama_rental()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(rental.getLatitude(), rental.getLongitude()), 14.0f));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        LatLng lokasiRental = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(lokasiRental).title(namaRental));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiRental));
    }

}
