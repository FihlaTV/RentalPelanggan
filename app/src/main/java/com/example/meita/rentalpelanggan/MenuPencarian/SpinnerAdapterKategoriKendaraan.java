package com.example.meita.rentalpelanggan.MenuPencarian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.R;

import java.util.ArrayList;

/**
 * Created by aswanabidin on 7/28/17.
 */

public class SpinnerAdapterKategoriKendaraan extends ArrayAdapter<String> {

    int groupid;
    ArrayList<String> listKategori;
    LayoutInflater inflater;

    public SpinnerAdapterKategoriKendaraan(Context context, int groupid, int id, ArrayList<String> listKategori){
        super(context,id,listKategori);
        this.listKategori = listKategori;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }



    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = inflater.inflate(groupid,parent,false);
        TextView textView = (TextView) itemView.findViewById(R.id.txtspinner);
        textView.setText(listKategori.get(position));
        return itemView;
    }

    public View getDropDownView (int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }

}
