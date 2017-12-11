package com.example.meita.rentalpelanggan.MenuPemesanan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.R;

import java.util.ArrayList;

/**
 * Created by meita on 10/24/2017.
 */

public class SpinnerAdapterJam extends ArrayAdapter<String> {
    int groupid;
    ArrayList<String> listJam;
    LayoutInflater inflater;

    public SpinnerAdapterJam(Context context, int groupid, int id, ArrayList<String> listJam){
        super(context,id,listJam);
        this.listJam = listJam;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = inflater.inflate(groupid,parent,false);
        TextView textView = (TextView) itemView.findViewById(R.id.txtspinnerJam);
        textView.setText(listJam.get(position));
        return itemView;
    }

    public View getDropDownView (int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }
}
