package com.example.meita.rentalpelanggan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.meita.rentalpelanggan.Autentifikasi.AutentifikasiTelepon;
import com.example.meita.rentalpelanggan.Autentifikasi.Login;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser()!=null){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    //startActivity(new Intent(SplashScreen.this,Login.class));
                    startActivity(new Intent(SplashScreen.this, AutentifikasiTelepon.class));
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
