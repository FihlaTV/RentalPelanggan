package com.example.meita.rentalpelanggan.MenuPembayaran;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.MenuPencarian.ItemClickListener;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;

import java.util.List;

public class DaftarRekeningPembayaranAdapter extends RecyclerView.Adapter<DaftarRekeningPembayaranAdapter.ViewHolder> implements View.OnClickListener {

    private List<RentalModel> rentalModel;
    Context context;
    String idRental, idKendaraan, idPemesanan, kategoriKendaraan;

    public DaftarRekeningPembayaranAdapter(Context context, List<RentalModel> rentalModel) {
        this.rentalModel = rentalModel;
        this.context = context;
    }

    public DaftarRekeningPembayaranAdapter(List<RentalModel> rentalModel, Context context, String idRental, String idKendaraan, String idPemesanan, String kategoriKendaraan) {
        this.rentalModel = rentalModel;
        this.context = context;
        this.idRental = idRental;
        this.idKendaraan = idKendaraan;
        this.idPemesanan = idPemesanan;
        this.kategoriKendaraan = kategoriKendaraan;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_daftar_rekening_pembayaran, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RentalModel dataRental = rentalModel.get(position);
        holder.textViewNamaRekening.setText(dataRental.getNamaBank());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    RentalModel dataRental = rentalModel.get(position);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, DetailPembayaran.class);
                    bundle.putString("idRekening", dataRental.getIdRekening());
                    bundle.putString("idRental", idRental);
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idPemesanan", idPemesanan);
                    bundle.putString("kategoriKendaraan", kategoriKendaraan);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, DetailPembayaran.class);
                    bundle.putString("idRekening", dataRental.getIdRekening());
                    bundle.putString("idRental", idRental);
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idPemesanan", idPemesanan);
                    bundle.putString("kategoriKendaraan", kategoriKendaraan);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return rentalModel.size();
    }

    @Override
    public void onClick(View view) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textViewNamaRekening;
        private ItemClickListener clickListener;
        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            textViewNamaRekening = (TextView)itemView.findViewById(R.id.textViewNamaRekening);
        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getPosition(), true);
            return true;
        }

    }



}
