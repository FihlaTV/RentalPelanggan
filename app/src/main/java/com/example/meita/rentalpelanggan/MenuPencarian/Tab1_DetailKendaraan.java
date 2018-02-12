package com.example.meita.rentalpelanggan.MenuPencarian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.Base.ImageLoader;
import com.example.meita.rentalpelanggan.MenuPemesanan.RincianPenyewaan;
import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by meita on 29/08/2017.
 */

public class Tab1_DetailKendaraan extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    Button btnPesan;
    String idPelanggan;
    TextView tipeKendaraan, rentalKendaraan, hargaSewa, lamaSewa,
            kebijakanPemesanan, kebijakanPembatalan, kebijakanPenggunaan;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Detail Kendaraan");
        View v = inflater.inflate(R.layout.fragment_tab1_detailkendaraan, container, false);

        btnPesan = (Button) v.findViewById(R.id.btnPesan);
        tipeKendaraan = (TextView) v.findViewById(R.id.tipe_kendaraan);
        rentalKendaraan = (TextView) v.findViewById(R.id.nama_rental);
        hargaSewa = (TextView) v.findViewById(R.id.harga_sewa);
        lamaSewa = (TextView) v.findViewById(R.id.lama_penyewaan);
        kebijakanPemesanan = (TextView) v.findViewById(R.id.kebijakanPemesanan);
        kebijakanPembatalan = (TextView) v.findViewById(R.id.kebijakanPembatalan);
        kebijakanPenggunaan = (TextView) v.findViewById(R.id.kebijakanPenggunaan);
        image = (ImageView) v.findViewById(R.id.imageViewFotoKendaraan);

        Bundle bundle = getArguments();
        final String idRental = bundle.getString("idRental");
        final String idKendaraan = bundle.getString("idKendaraan");
        final String kategoriKendaraan = bundle.getString("kategoriKendaraan");
        final String tglSewaPencarian = bundle.getString("tglSewaPencarian");
        final String tglKembaliPencarian = bundle.getString("tglKembaliPencarian");
        final String jumlahKendaraanPencarian = bundle.getString("jumlahKendaraanPencarian");
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();

        final FragmentActivity c = getActivity();
        Firebase.setAndroidContext(getActivity());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), RincianPenyewaan.class);
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

        infoRental();
        infoKendaraan();
        return v;
    }

    public void infoRental() {
        Bundle bundle = getArguments();
        final String idRental = bundle.getString("idRental");
        mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                rentalKendaraan.setText(dataRental.getNama_rental());
                kebijakanPemesanan.setText(dataRental.getKebijakan_pemesanan_rental());
                kebijakanPembatalan.setText(dataRental.getKebijakan_pembatalan_rental());
                kebijakanPenggunaan.setText(dataRental.getKebijakan_pemesanan_rental());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void infoKendaraan() {
        Bundle bundle = getArguments();
        final String idKendaraan = bundle.getString("idKendaraan");
        final String kategoriKendaraan = bundle.getString("kategoriKendaraan");
        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
                tipeKendaraan.setText(kendaraan.getTipeKendaraan());
                hargaSewa.setText("Rp." + BaseActivity.rupiah().format(kendaraan.getHargaSewa()));
                lamaSewa.setText(kendaraan.getLamaPenyewaan());
                ImageLoader.getInstance().loadImageOther(getActivity(), kendaraan.getUriFotoKendaraan().get(0), image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}