package com.example.meita.rentalpelanggan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.meita.rentalpelanggan.Autentifikasi.Login;
import com.example.meita.rentalpelanggan.Base.DeviceToken;
import com.example.meita.rentalpelanggan.MenuPemberitahuan.MenuPemberitahuan;
import com.example.meita.rentalpelanggan.MenuPencarian.MenuPencarian;
import com.example.meita.rentalpelanggan.MenuStatusPemesanan.MenuStatusPemesanan;
import com.example.meita.rentalpelanggan.MenuProfilPelanggan.MenuProfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    String halamanStatus;
    String idPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_cari);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idPelanggan = user.getUid();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        int halamanStatus1 = getIntent().getIntExtra("halamanStatus", 0);
        int halamanStatus2 = getIntent().getIntExtra("halamanStatus2", 1);
        int halamanStatus3 = getIntent().getIntExtra("halamanStatus3", 2);

        if (findViewById(R.id.content_frame) != null && halamanStatus1 != 0) {
            MenuStatusPemesanan menuStatusPemesanan = new MenuStatusPemesanan();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, menuStatusPemesanan).commit();
        } else if (findViewById(R.id.content_frame) != null && halamanStatus2 != 1) {
            Bundle bundle=new Bundle();
            bundle.putInt("valueHalamanStatus2", 2);
            MenuStatusPemesanan menuStatusPemesanan = new MenuStatusPemesanan();
            menuStatusPemesanan.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, menuStatusPemesanan).commit();
        } else if (findViewById(R.id.content_frame) != null && halamanStatus3 != 2) {
            Bundle bundle=new Bundle();
            bundle.putInt("valueHalamanStatus3", 3);
            MenuStatusPemesanan menuStatusPemesanan = new MenuStatusPemesanan();
            menuStatusPemesanan.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, menuStatusPemesanan).commit();
        }

        initData();
    }

    private void initData(){
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            signOut();
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String token = FirebaseInstanceId.getInstance().getToken();
            DeviceToken.getInstance().addDeviceToken(mDatabase, idPelanggan, token);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void displaySelectedScreen (int itemId) {
        Fragment fragment = null; // membuat objek dari kelas fragment

        switch (itemId) {
            case R.id.nav_cari:
                fragment = new MenuPencarian();
                break;
            case R.id.nav_penyewaanku:
                fragment = new MenuStatusPemesanan();
                break;
            case R.id.nav_pemberitahuan:
                fragment = new MenuPemberitahuan();
                break;
            case R.id.nav_profilku:
                fragment = new MenuProfil();
                break;
            case R.id.nav_tentang:
                fragment = new MenuTentangAplikasi();
                break;
            case R.id.nav_keluar:
                signOut();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
//        Intent intent = new Intent(this, AutentifikasiTelepon.class);
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
