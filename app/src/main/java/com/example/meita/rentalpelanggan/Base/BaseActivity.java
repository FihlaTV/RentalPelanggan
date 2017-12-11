package com.example.meita.rentalpelanggan.Base;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by meita on 11/14/2017.
 */

public class BaseActivity {

    public static NumberFormat rupiah(){
        Locale localeID = new Locale("in", "ID");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(localeID);

        return numberFormat;
    }
}
