package com.example.meita.rentalpelanggan.MenuStatusPemesanan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.Base.ImageLoader;
import com.example.meita.rentalpelanggan.MenuPemesanan.PemesananModel;
import com.example.meita.rentalpelanggan.MenuPencarian.ItemClickListener;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TabStatus5Adapter extends RecyclerView.Adapter<TabStatus5Adapter.ViewHolder> implements View.OnClickListener {
    private List<PemesananModel> pemesananModel;
    DatabaseReference mDatabase;
    Context context;

    public TabStatus5Adapter(Context context, List<PemesananModel> pemesananModel) {
        this.pemesananModel = pemesananModel;
        this.context = context;
    }


    @Override
    public TabStatus5Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tab_status5, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TabStatus5Adapter.ViewHolder holder, int position) {
        final PemesananModel dataPemesanan = pemesananModel.get(position);
        final String kategoriKendaraan = dataPemesanan.getKategoriKendaraan();
        final String idKendaraan = dataPemesanan.getIdKendaraan();
        final String idRental = dataPemesanan.getIdRental();
        final String idPelanggan = dataPemesanan.getIdPelanggan();
        final String statusPemesanan = dataPemesanan.getStatusPemesanan();
        holder.textViewStatusPemesanan.setText(dataPemesanan.getStatusPemesanan());
        holder.textViewTglSewa.setText(dataPemesanan.getTglSewa());
        holder.textViewTglKembali.setText(dataPemesanan.getTglKembali());
        holder.textViewTotalPembayaran.setText("Rp. "+ BaseActivity.rupiah().format(dataPemesanan.getTotalBiayaPembayaran()));
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, DetailPemesananStatus5.class);
                    bundle.putString("idPemesanan", dataPemesanan.getIdPemesanan());
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idRental", idRental);
                    bundle.putString("idPelanggan", idPelanggan);
                    bundle.putString("kategoriKendaraan", kategoriKendaraan);
                    bundle.putString("statusPemesanan", statusPemesanan);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, DetailPemesananStatus5.class);
                    bundle.putString("idPemesanan", dataPemesanan.getIdPemesanan());
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idRental", idRental);
                    bundle.putString("idPelanggan", idPelanggan);
                    bundle.putString("kategoriKendaraan", kategoriKendaraan);
                    bundle.putString("statusPemesanan", statusPemesanan);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
                holder.textViewTipeKendaraan.setText(dataKendaraan.getTipeKendaraan());
                ImageLoader.getInstance().loadImageOther(context, dataKendaraan.getUriFotoKendaraan().get(0), holder.fotoKendaraan);
                if (dataKendaraan.isSupir() == true ) {
                    holder.textViewDenganSupir.setVisibility(View.VISIBLE);
                    holder.checkListDenganSupir.setVisibility(View.VISIBLE);
                    holder.textViewTanpaSupir.setVisibility(View.GONE);
                    holder.checkListTanpaSupir.setVisibility(View.GONE);
                } else {
                    holder.textViewDenganSupir.setVisibility(View.GONE);
                    holder.checkListDenganSupir.setVisibility(View.GONE);
                    holder.textViewTanpaSupir.setVisibility(View.VISIBLE);
                    holder.checkListTanpaSupir.setVisibility(View.VISIBLE);
                }

                if (dataKendaraan.isBahanBakar() == true ) {
                    holder.textViewDenganBBM.setVisibility(View.VISIBLE);
                    holder.checkListDenganBBM.setVisibility(View.VISIBLE);
                    holder.textViewTanpaBBM.setVisibility(View.GONE);
                    holder.checkListTanpaBBM.setVisibility(View.GONE);
                } else {
                    holder.textViewDenganBBM.setVisibility(View.GONE);
                    holder.checkListDenganBBM.setVisibility(View.GONE);
                    holder.textViewTanpaBBM.setVisibility(View.VISIBLE);
                    holder.checkListTanpaBBM.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                holder.textViewNamaRental.setText(dataRental.getNama_rental());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pemesananModel.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ItemClickListener clickListener;
        public ImageView fotoKendaraan, checkListDenganSupir, checkListTanpaSupir, checkListDenganBBM, checkListTanpaBBM;
        public TextView textViewStatusPemesanan, textViewTglSewa, textViewTglKembali, textViewTipeKendaraan,
                textViewNamaRental, textViewDenganSupir, textViewTanpaSupir, textViewDenganBBM,
                textViewTanpaBBM, textViewTotalPembayaran;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            fotoKendaraan = (ImageView)itemView.findViewById(R.id.imageViewFotoKendaraan);
            checkListDenganSupir = (ImageView)itemView.findViewById(R.id.icCheckListDenganSupir);
            checkListTanpaSupir = (ImageView)itemView.findViewById(R.id.icCheckListTanpaSupir);
            checkListDenganBBM = (ImageView)itemView.findViewById(R.id.icCheckListDenganBBM);
            checkListTanpaBBM = (ImageView)itemView.findViewById(R.id.icCheckListTanpaBBM);
            textViewStatusPemesanan = (TextView)itemView.findViewById(R.id.textViewStatusPemesanan);
            textViewTglSewa = (TextView)itemView.findViewById(R.id.textViewTglSewa);
            textViewTglKembali = (TextView)itemView.findViewById(R.id.textViewTglKembali);
            textViewTipeKendaraan = (TextView)itemView.findViewById(R.id.textViewTipeKendaraan);
            textViewNamaRental = (TextView)itemView.findViewById(R.id.textViewRentalKendaraan);
            textViewDenganSupir = (TextView)itemView.findViewById(R.id.textViewDenganSupir);
            textViewTanpaSupir = (TextView)itemView.findViewById(R.id.textViewTanpaSupir);
            textViewDenganBBM = (TextView)itemView.findViewById(R.id.textViewDenganBBM);
            textViewTanpaBBM = (TextView)itemView.findViewById(R.id.textViewTanpaBBM);
            textViewTotalPembayaran = (TextView)itemView.findViewById(R.id.textViewTotalPembayaran);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }
}
