package com.example.meita.rentalpelanggan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MenuTentangAplikasi extends Fragment {
    Button abc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("About Fragment");
        View v = inflater.inflate(R.layout.fragment_menu_tentang_aplikasi, container, false);
        abc = (Button)v.findViewById(R.id.btnIntentActivity);

        return v;
    }
}
