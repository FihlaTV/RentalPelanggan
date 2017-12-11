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

public class SpinnerAdapterJumlahKendaraan extends ArrayAdapter<String> {

    int groupid;
    ArrayList<String> listJumlahKendaraan;
    LayoutInflater inflater;

    public SpinnerAdapterJumlahKendaraan(Context context, int groupid, int id, ArrayList<String> listJumlahKendaraan){
        super(context,id,listJumlahKendaraan);
        this.listJumlahKendaraan = listJumlahKendaraan;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }



    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = inflater.inflate(groupid,parent,false);
        TextView textView = (TextView) itemView.findViewById(R.id.txtspinnerJumlahKendaraan);
        textView.setText(listJumlahKendaraan.get(position));
        return itemView;
    }

    public View getDropDownView (int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }

}
