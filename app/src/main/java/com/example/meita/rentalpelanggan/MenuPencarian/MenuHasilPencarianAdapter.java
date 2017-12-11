package com.example.meita.rentalpelanggan.MenuPencarian;

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
import com.example.meita.rentalpelanggan.R;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.List;

/**
 * Created by meita on 29/08/2017.
 */

public class MenuHasilPencarianAdapter extends RecyclerView.Adapter<MenuHasilPencarianAdapter.ViewHolder> implements View.OnClickListener {

    private List<KendaraanModel> kendaraanModel;
    Context context;
    String tglSewaPencarian, tglKembaliPencarian;
    String jumlahKendaraanPencarian;

    public MenuHasilPencarianAdapter(Context context, List<KendaraanModel> kendaraanModel, String tglSewaPencarian, String tglKembaliPencarian, String jumlahKendaraanPencarian) {
        this.kendaraanModel = kendaraanModel;
        this.context = context;
        this.tglSewaPencarian = tglSewaPencarian;
        this.tglKembaliPencarian = tglKembaliPencarian;
        this.jumlahKendaraanPencarian = jumlahKendaraanPencarian;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_menu_hasil_pencarian, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final KendaraanModel dataKendaraan = kendaraanModel.get(position);
        holder.tipeKendaraan.setText(dataKendaraan.getTipeKendaraan());
        holder.lamaPenyewaan.setText(dataKendaraan.getLamaPenyewaan());
        holder.hargaSewa.setText("Rp."+ BaseActivity.rupiah().format(dataKendaraan.getHargaSewa()));
        ImageLoader.getInstance().loadImageOther(context, dataKendaraan.getUriFotoKendaraan().get(0), holder.fotoKendaraan);
        if (dataKendaraan.isSupir() == true ) {
            holder.textViewDenganSupir.setVisibility(View.VISIBLE);
            holder.imageChecklistSupirTrue.setVisibility(View.VISIBLE);
            holder.textViewTanpaSupir.setVisibility(View.GONE);
            holder.imageCheckListSupirFalse.setVisibility(View.GONE);
        } else {
            holder.textViewTanpaSupir.setVisibility(View.VISIBLE);
            holder.imageCheckListSupirFalse.setVisibility(View.VISIBLE);
            holder.textViewDenganSupir.setVisibility(View.GONE);
            holder.imageChecklistSupirTrue.setVisibility(View.GONE);
        }

        if (dataKendaraan.isBahanBakar() == true ) {
            holder.textViewDenganBBM.setVisibility(View.VISIBLE);
            holder.imageCheckListBBMTrue.setVisibility(View.VISIBLE);
            holder.textViewTanpaBBM.setVisibility(View.GONE);
            holder.imageCheckListBBMFalse.setVisibility(View.GONE);
        } else {
            holder.textViewTanpaBBM.setVisibility(View.VISIBLE);
            holder.imageCheckListBBMFalse.setVisibility(View.VISIBLE);
            holder.textViewDenganBBM.setVisibility(View.GONE);
            holder.imageCheckListBBMTrue.setVisibility(View.GONE);
        }

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Bundle bundle = new Bundle();
                    KendaraanModel data = kendaraanModel.get(position);
                    Intent intent = new Intent(context, MenuDetailKendaraan.class);
                    bundle.putString("idKendaraan", data.getIdKendaraan());
                    bundle.putString("idRental", data.getIdRental());
                    bundle.putString("kategoriKendaraan", data.getKategoriKendaraan());
                    bundle.putInt("jumlahKendaraan", data.getJumlahKendaraan());
                    bundle.putString("tglSewaPencarian", tglSewaPencarian);
                    bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
                    bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    KendaraanModel data = kendaraanModel.get(position);
                    Intent intent = new Intent(context, MenuDetailKendaraan.class);
                    bundle.putString("idKendaraan", data.getIdKendaraan());
                    bundle.putString("idRental", data.getIdRental());
                    bundle.putString("kategoriKendaraan", data.getKategoriKendaraan());
                    bundle.putInt("jumlahKendaraan", data.getJumlahKendaraan());
                    bundle.putString("tglSewaPencarian", tglSewaPencarian);
                    bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
                    bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kendaraanModel.size();
    }

    @Override
    public void onClick(View view) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView tipeKendaraan, lamaPenyewaan, hargaSewa,  textViewDenganSupir, textViewTanpaSupir, textViewDenganBBM, textViewTanpaBBM;
        public ImageView fotoKendaraan, imageChecklistSupirTrue, imageCheckListSupirFalse, imageCheckListBBMTrue, imageCheckListBBMFalse;
        private ItemClickListener clickListener;
        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            tipeKendaraan = (TextView) itemView.findViewById(R.id.tipe_kendaraan);
            lamaPenyewaan = (TextView) itemView.findViewById(R.id.lama_penyewaan);
            hargaSewa = (TextView) itemView.findViewById(R.id.harga_sewa);
            fotoKendaraan = (ImageView) itemView.findViewById(R.id.imageViewFotoKendaraan);
            textViewDenganSupir = (TextView)itemView.findViewById(R.id.txtViewSupir);
            textViewTanpaSupir = (TextView)itemView.findViewById(R.id.txtViewSupirFalse);
            imageChecklistSupirTrue = (ImageView)itemView.findViewById(R.id.icCheckListDenganSupir);
            imageCheckListSupirFalse = (ImageView)itemView.findViewById(R.id.icCheckListTanpaSupir);
            textViewDenganBBM = (TextView)itemView.findViewById(R.id.txtViewBBMTrue);
            textViewTanpaBBM = (TextView)itemView.findViewById(R.id.txtViewBBMFalse);

            imageCheckListBBMTrue = (ImageView)itemView.findViewById(R.id.icCheckListDenganBBM);
            imageCheckListBBMFalse = (ImageView)itemView.findViewById(R.id.icCheckListTanpaBBM);
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
