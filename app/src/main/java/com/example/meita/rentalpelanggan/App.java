package com.example.meita.rentalpelanggan;

import android.app.Application;
import android.content.Intent;

/**
 * Created by meita on 3/13/2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, BackgroundService.class));
    }
}
