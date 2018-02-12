package com.example.meita.rentalpelanggan.MenuUlasan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DaftarUlasanAdapter extends RecyclerView.Adapter<DaftarUlasanAdapter.ViewHolder> {
    private List<UlasanModel> ulasanModel;
    DatabaseReference mDatabase;
    Context context;

    public DaftarUlasanAdapter(Context context, List<UlasanModel> ulasanModel) {
        this.ulasanModel = ulasanModel;
        this.context = context;
    }

    @Override
    public DaftarUlasanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_daftar_ulasan, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final DaftarUlasanAdapter.ViewHolder holder, int position) {
        final UlasanModel dataUlasan = ulasanModel.get(position);
        holder.textViewUlasan.setText(dataUlasan.getUlasan());
        holder.rb_kualitas_kendaraan.setRating(dataUlasan.getRatingKendaraan());
        holder.rb_kualitas_pelayanan.setRating(dataUlasan.getRatingPelayanan());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String idPelanggan = dataUlasan.getIdPelanggan();
        mDatabase.child("pelanggan").child(idPelanggan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PelangganModel dataPelanggan = dataSnapshot.getValue(PelangganModel.class);
                holder.textViewNamaPelanggan.setText(dataPelanggan.getNamaLengkap());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String idKendaraan = dataUlasan.getIdKendaraan();
        String kategoriKendaraan = dataUlasan.getIdKategori();
        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
                holder.textViewTipeKendaraan.setText(dataKendaraan.getTipeKendaraan());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ulasanModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNamaPelanggan, textViewTipeKendaraan, textViewUlasan;
        RatingBar rb_kualitas_pelayanan, rb_kualitas_kendaraan;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            textViewTipeKendaraan = (TextView)itemView.findViewById(R.id.textViewTipeKendaraan);
            textViewNamaPelanggan = (TextView)itemView.findViewById(R.id.textViewNamaPelanggan);
            textViewUlasan = (TextView)itemView.findViewById(R.id.textViewUlasan);
            rb_kualitas_pelayanan = (RatingBar)itemView.findViewById(R.id.rb_kualitas_pelayanan);
            rb_kualitas_kendaraan = (RatingBar)itemView.findViewById(R.id.rb_kualitas_kendaraan);
        }

    }
}

