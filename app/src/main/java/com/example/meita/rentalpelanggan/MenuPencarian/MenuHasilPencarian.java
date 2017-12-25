package com.example.meita.rentalpelanggan.MenuPencarian;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPemesanan.PemesananModel;
import com.example.meita.rentalpelanggan.R;
import com.example.meita.rentalpelanggan.SisaKendaraanModel;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class MenuHasilPencarian extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MenuHasilPencarianAdapter adapter;
    private List<KendaraanModel> kendaraanModel;
    private DatabaseReference mDatabase;
    Date tglSewaPencarian, tglKembaliPencarian, tglSewaDipesan, tglKembaliDipesan;
    int jmlKendaraan, jmlKendaraanPencarian, jmlKendaraanDipesan, sum, hargaAwal, hargaAkhir;
    String idKendaraanChecking;
    TextView kategoriToolbar, tglToolbar, jumlahKendaraanToolBar;
    ProgressBar progressBar;
    Button buttonFilter, buttonTerdekat;
    ImageView kendaraanTidakTersedia;
    LinearLayout linearLayoutListKendaraan;
    ActionBar actionBar;
    Toolbar toolbar;
    int jmlKendaraanSeluruh;
    int jumlahSisaKendaraan;
    String idCekSisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hasil_pencarian);
        Firebase.setAndroidContext(this);

        recyclerView = (RecyclerView) findViewById(R.id.listViewKendaraan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kendaraanModel = new ArrayList<>();
        kategoriToolbar = (TextView) findViewById(R.id.kategoriKendaraan);
        tglToolbar = (TextView) findViewById(R.id.tglSewa);
        jumlahKendaraanToolBar = (TextView)findViewById(R.id.jumlahKendaraan);
        buttonFilter = (Button) findViewById(R.id.btn_filter);
        buttonTerdekat = (Button) findViewById(R.id.btn_terdekat);
        kendaraanTidakTersedia = (ImageView) findViewById(R.id.ic_kendaran_noavailable);
        linearLayoutListKendaraan = (LinearLayout) findViewById(R.id.linearLayoutListKendaraan);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);
        kendaraanTidakTersedia.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");

        String tgl = tanggalSewaPencarian + " - " + tanggalKembaliPencarian;
        kategoriToolbar.setText(kategoriKendaraanPencarian);
        tglToolbar.setText(tgl);
        jumlahKendaraanToolBar.setText(jumlahKendaraanPencarian);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                showDialogFilterPencarian();
            }
        });

        kendaraanModel.clear();
        getHasilPencarian();
    }

    public void showDialogFilterPencarian() {
        final Dialog dialog = new Dialog(MenuHasilPencarian.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_filter);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(layoutParams);

        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar1);
        final TextView txthargaAwal = (TextView) dialog.findViewById(R.id.txt_harga_awal);
        final TextView txthargaAkhir = (TextView) dialog.findViewById(R.id.txt_harga_akhir);
        final CheckBox denganSopir = (CheckBox) dialog.findViewById(R.id.checkBoxDenganSupir);
        final CheckBox tanpaSopir = (CheckBox) dialog.findViewById(R.id.checkBoxTanpaSupir);
        final CheckBox denganBahanBakar = (CheckBox) dialog.findViewById(R.id.checkBoxDenganBahanBakar);
        final CheckBox tanpaBahanBakar = (CheckBox) dialog.findViewById(R.id.checkBoxTanpaBahanBakar);
        Button btnYa = (Button) dialog.findViewById(R.id.btn_filter_ya);
        Button btnTidak = (Button) dialog.findViewById(R.id.btn_filter_tidak);


        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                if ((maxValue.intValue() < 2900000 && maxValue.intValue() != 0) || (maxValue.intValue() < 2900000)) {
                    txthargaAwal.setText(String.valueOf(minValue));
                    txthargaAkhir.setText(String.valueOf(maxValue));
                    hargaAwal = minValue.intValue();
                    hargaAkhir = maxValue.intValue();
                    denganSopir.setEnabled(false);
                    tanpaSopir.setEnabled(false);
                    denganBahanBakar.setEnabled(false);
                    tanpaBahanBakar.setEnabled(false);
                } else {
                    denganSopir.setEnabled(true);
                    tanpaSopir.setEnabled(true);
                    denganBahanBakar.setEnabled(true);
                    tanpaBahanBakar.setEnabled(true);
                }


            }
        });

        denganSopir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (denganSopir.isChecked()) {
                    tanpaSopir.setEnabled(false);
                    denganBahanBakar.setEnabled(false);
                    tanpaBahanBakar.setEnabled(false);
                    rangeSeekbar.setEnabled(false);
                } else if (!denganSopir.isChecked()) {
                    tanpaSopir.setEnabled(true);
                    denganBahanBakar.setEnabled(true);
                    tanpaBahanBakar.setEnabled(true);
                    rangeSeekbar.setEnabled(true);
                }
            }
        });

        tanpaSopir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (tanpaSopir.isChecked()) {
                    denganSopir.setEnabled(false);
                    denganBahanBakar.setEnabled(false);
                    tanpaBahanBakar.setEnabled(false);
                    rangeSeekbar.setEnabled(false);
                } else if (!denganSopir.isChecked()) {
                    denganSopir.setEnabled(true);
                    denganBahanBakar.setEnabled(true);
                    tanpaBahanBakar.setEnabled(true);
                    rangeSeekbar.setEnabled(true);
                }
            }
        });

        denganBahanBakar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (denganBahanBakar.isChecked()) {
                    denganSopir.setEnabled(false);
                    tanpaSopir.setEnabled(false);
                    tanpaBahanBakar.setEnabled(false);
                    rangeSeekbar.setEnabled(false);
                } else if (!denganSopir.isChecked()) {
                    denganSopir.setEnabled(true);
                    tanpaSopir.setEnabled(true);
                    tanpaBahanBakar.setEnabled(true);
                    rangeSeekbar.setEnabled(true);
                }
            }
        });

        tanpaBahanBakar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (tanpaBahanBakar.isChecked()) {
                    denganSopir.setEnabled(false);
                    tanpaSopir.setEnabled(false);
                    denganBahanBakar.setEnabled(false);
                    rangeSeekbar.setEnabled(false);
                } else if (!denganSopir.isChecked()) {
                    denganSopir.setEnabled(true);
                    tanpaSopir.setEnabled(true);
                    denganBahanBakar.setEnabled(true);
                    rangeSeekbar.setEnabled(true);
                }
            }
        });

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((hargaAkhir < 2900000 && hargaAkhir != 0) || (hargaAkhir < 2900000)) {
                    Toast.makeText(dialog.getContext(), String.valueOf(hargaAwal) + " - " + String.valueOf(hargaAkhir), Toast.LENGTH_SHORT).show();
                    getFilterHarga();
                    dialog.dismiss();
                } else if (denganSopir.isChecked()) {
                    getFilterDenganSopir();
                    dialog.dismiss();
                } else if (tanpaSopir.isChecked()) {
                    getFilterTanpaSopir();
                    dialog.dismiss();
                } else if (denganBahanBakar.isChecked()) {
                    getFilterDenganBahanBakar();
                    dialog.dismiss();
                } else if (tanpaBahanBakar.isChecked()) {
                    getFilterTanpaBahanBakar();
                    dialog.dismiss();
                }

            }
        });

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });

        dialog.show();

    }

    public void getHasilPencarian() {
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian"); //1
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final LinkedHashSet<KendaraanModel> lhs = new LinkedHashSet<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tglSewaPencarian = format.parse(tanggalSewaPencarian);
            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() { //2
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //3
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //4
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);//5
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final String id = kendaraan.getIdKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekSisaKendaraan");
                        Query query = ref.orderByChild("idKendaraan").equalTo(id);
                        query.addValueEventListener(new com.firebase.client.ValueEventListener() { //6
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) { //7
                                    for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //8
                                        SisaKendaraanModel sisaModel = postSnapshot.getValue(SisaKendaraanModel.class); //9
                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                        idCekSisa = sisaModel.getIdCekSisa();
                                        final int sisaKendaraan = sisaModel.getSisaKendaraan();

                                        try {
                                            tglSewaDipesan = format.parse(sisaModel.getTglSewa());
                                            tglKembaliDipesan = format.parse(sisaModel.getTglKembali());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if ((tglSewaPencarian.before(tglKembaliDipesan) || //10
                                                tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) ||
                                                tglKembaliPencarian.equals(tglSewaDipesan)) ||
                                                tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                            if (sisaKendaraan >= jmlKendaraanPencarian || jmlKendaraanModel == jmlKendaraanPencarian) { //11
                                                kendaraanModel.add(kendaraan); //12
                                                Toast.makeText(getApplicationContext(), "id " + id + " di add ke adapter", Toast.LENGTH_LONG).show();
                                            } else { //13
                                                kendaraanModel.remove(kendaraan); //14
                                                Toast.makeText(getApplicationContext(), "id " + id + " di remove karna kurang", Toast.LENGTH_LONG).show();
                                                break;
                                            }
                                        } else { //15
                                            if (jmlKendaraanModel >= jmlKendaraanPencarian || jmlKendaraanModel == jmlKendaraanPencarian) { //16
                                                kendaraanModel.add(kendaraan); //17
                                                Toast.makeText(getApplicationContext(), "id " + id + " di add krna tglnya beda sama yang di cek dan tersedia", Toast.LENGTH_LONG).show();
                                            } else { //18
                                                kendaraanModel.remove(kendaraan); //19
                                                Toast.makeText(getApplicationContext(), "id " + id + " tglnya beda sama pencarian tapi diremove krna ga cukup", Toast.LENGTH_LONG).show();
                                                break;
                                            }
                                        }
                                    } // end looping sisa cek //20
                                } else { //21
                                    if (jmlKendaraanModel > jmlKendaraanPencarian || jmlKendaraanModel == jmlKendaraanPencarian) { //22
                                        kendaraanModel.add(kendaraan);//23
                                        Toast.makeText(getApplicationContext(), "id " + id + " tidak ada di cek sisa dan kendaraan trsedia", Toast.LENGTH_LONG).show();
                                    } else { //24
                                        kendaraanModel.remove(kendaraan);//25
                                        Toast.makeText(getApplicationContext(), "id " + id + " tidak ada di cek sisa tapi di remove karna kendaraan ga cukup", Toast.LENGTH_LONG).show();
                                    }
                                }
                                lhs.addAll(kendaraanModel); //26
                                kendaraanModel.clear();
                                kendaraanModel.addAll(lhs);
//                                if (kendaraanModel.isEmpty()) {
//                                    progressBar.setVisibility(View.GONE);
//                                    linearLayoutListKendaraan.setVisibility(View.GONE);
//                                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
//                                }
                                adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
                    } // end looping kendaraan //27
                } else { //28
                    progressBar.setVisibility(View.GONE); //29
                    linearLayoutListKendaraan.setVisibility(View.GONE);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "ga ada data kendaraan", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        lhs.addAll(kendaraanModel);
//        kendaraanModel.clear();
//        kendaraanModel.addAll(lhs);



//        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
//        recyclerView.setAdapter(adapter);
    } //30

    public void getFilterHarga() {
        kendaraanModel.clear();
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final ArrayList<Integer> listJumlah = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tglSewaPencarian = format.parse(tanggalSewaPencarian);
            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).orderByChild("hargaSewa").startAt(hargaAwal).endAt(hargaAkhir).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    kendaraanTidakTersedia.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                        idKendaraanChecking = kendaraan.getIdKendaraan();
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
                        Query query = ref.orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan());
                        query.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();

                                    try {
                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) || tglKembaliPencarian.equals(tglSewaDipesan))
                                            || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                        listJumlah.add(jmlKendaraanDipesan);
                                        sum = 0;
                                        for (int i = 0; i < listJumlah.size(); i++) {
                                            sum += listJumlah.get(i);
                                            jmlKendaraanDipesan = sum;
                                        }
                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                        //int abc = jmlKendaraanModel;
                                        if (jmlKendaraanModel < a) {
                                            Toast.makeText(getApplicationContext(), "REMOVE DI EKSEKUSI", Toast.LENGTH_SHORT).show();
                                            kendaraanModel.remove(kendaraan);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(kendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(null);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // breakpoint addValueEventListener query child kendaraan
        progressBar.setVisibility(View.GONE);
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        adapter.notifyDataSetChanged();
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        //progress bar berhenti ketika cardview muncul

    }

    public void getFilterDenganSopir() {
        kendaraanModel.clear();
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final ArrayList<Integer> listJumlah = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tglSewaPencarian = format.parse(tanggalSewaPencarian);
            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).orderByChild("supir").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    kendaraanTidakTersedia.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                        idKendaraanChecking = kendaraan.getIdKendaraan();
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
                        Query query = ref.orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan());
                        query.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();

                                    try {
                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) || tglKembaliPencarian.equals(tglSewaDipesan))
                                            || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                        listJumlah.add(jmlKendaraanDipesan);
                                        sum = 0;
                                        for (int i = 0; i < listJumlah.size(); i++) {
                                            sum += listJumlah.get(i);
                                            jmlKendaraanDipesan = sum;
                                        }
                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                        //int abc = jmlKendaraanModel;
                                        if (jmlKendaraanModel < a) {
                                            Toast.makeText(getApplicationContext(), "REMOVE DI EKSEKUSI", Toast.LENGTH_SHORT).show();
                                            kendaraanModel.remove(kendaraan);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(kendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(null);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // breakpoint addValueEventListener query child kendaraan
        progressBar.setVisibility(View.GONE);
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        adapter.notifyDataSetChanged();
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    public void getFilterTanpaSopir() {
        kendaraanModel.clear();
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final ArrayList<Integer> listJumlah = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tglSewaPencarian = format.parse(tanggalSewaPencarian);
            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).orderByChild("supir").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    kendaraanTidakTersedia.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                        idKendaraanChecking = kendaraan.getIdKendaraan();
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
                        Query query = ref.orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan());
                        query.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();

                                    try {
                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) || tglKembaliPencarian.equals(tglSewaDipesan))
                                            || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                        listJumlah.add(jmlKendaraanDipesan);
                                        sum = 0;
                                        for (int i = 0; i < listJumlah.size(); i++) {
                                            sum += listJumlah.get(i);
                                            jmlKendaraanDipesan = sum;
                                        }
                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                        //int abc = jmlKendaraanModel;
                                        if (jmlKendaraanModel < a) {
                                            Toast.makeText(getApplicationContext(), "REMOVE DI EKSEKUSI", Toast.LENGTH_SHORT).show();
                                            kendaraanModel.remove(kendaraan);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(kendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(null);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // breakpoint addValueEventListener query child kendaraan
        progressBar.setVisibility(View.GONE);
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        adapter.notifyDataSetChanged();
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    public void getFilterDenganBahanBakar() {
        kendaraanModel.clear();
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final ArrayList<Integer> listJumlah = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tglSewaPencarian = format.parse(tanggalSewaPencarian);
            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).orderByChild("bahanBakar").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    kendaraanTidakTersedia.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                        idKendaraanChecking = kendaraan.getIdKendaraan();
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
                        Query query = ref.orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan());
                        query.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();

                                    try {
                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) || tglKembaliPencarian.equals(tglSewaDipesan))
                                            || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                        listJumlah.add(jmlKendaraanDipesan);
                                        sum = 0;
                                        for (int i = 0; i < listJumlah.size(); i++) {
                                            sum += listJumlah.get(i);
                                            jmlKendaraanDipesan = sum;
                                        }
                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                        //int abc = jmlKendaraanModel;
                                        if (jmlKendaraanModel < a) {
                                            Toast.makeText(getApplicationContext(), "REMOVE DI EKSEKUSI", Toast.LENGTH_SHORT).show();
                                            kendaraanModel.remove(kendaraan);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(kendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(null);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // breakpoint addValueEventListener query child kendaraan
        progressBar.setVisibility(View.GONE);
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        adapter.notifyDataSetChanged();
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    public void getFilterTanpaBahanBakar() {
        kendaraanModel.clear();
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final ArrayList<Integer> listJumlah = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tglSewaPencarian = format.parse(tanggalSewaPencarian);
            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).orderByChild("bahanBakar").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    kendaraanTidakTersedia.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                        idKendaraanChecking = kendaraan.getIdKendaraan();
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
                        Query query = ref.orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan());
                        query.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();

                                    try {
                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) || tglKembaliPencarian.equals(tglSewaDipesan))
                                            || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                        listJumlah.add(jmlKendaraanDipesan);
                                        sum = 0;
                                        for (int i = 0; i < listJumlah.size(); i++) {
                                            sum += listJumlah.get(i);
                                            jmlKendaraanDipesan = sum;
                                        }
                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                        //int abc = jmlKendaraanModel;
                                        if (jmlKendaraanModel < a) {
                                            Toast.makeText(getApplicationContext(), "REMOVE DI EKSEKUSI", Toast.LENGTH_SHORT).show();
                                            kendaraanModel.remove(kendaraan);
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(kendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(null);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // breakpoint addValueEventListener query child kendaraan
        progressBar.setVisibility(View.GONE);
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        adapter.notifyDataSetChanged();
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // INI YANG PAKE CHILD COUNT
//      mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            if (dataSnapshot.exists()) {
//                progressBar.setVisibility(View.GONE);
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
//                    jmlKendaraan = kendaraan.getJumlahKendaraan();
//                    final String id = kendaraan.getIdKendaraan();
//                    final int jmlKendaraanModel = jmlKendaraan;
//
//                    Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
//                    Query query = ref.orderByChild("idKendaraan").equalTo(id);
//                    query.addValueEventListener(new com.firebase.client.ValueEventListener() {
//                        @Override
//                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                long size = dataSnapshot.getChildrenCount();
//                                if (size>1) {
//                                    for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                        PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
//                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                                        String dicek = id;
//                                        jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();
//                                        try {
//                                            tglSewaDipesan = format.parse(pemesanan.getTglSewa());
//                                            tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        if ((tglSewaPencarian.before(tglKembaliDipesan)
//                                                || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan)
//                                                || tglKembaliPencarian.equals(tglSewaDipesan))
//                                                || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
//                                            int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
//                                            if (jmlKendaraanModel == a || jmlKendaraanModel > a) {
//                                                kendaraanModel.add(kendaraan);
//                                                adapter.notifyDataSetChanged();
//                                                Toast.makeText(getApplicationContext(), "add id "+id, Toast.LENGTH_LONG).show();
//                                                break;
//                                            } else {
//                                                kendaraanModel.remove(kendaraan);
//                                                adapter.notifyDataSetChanged();
//                                                Toast.makeText(getApplicationContext(), "remove id "+id, Toast.LENGTH_LONG).show();
//                                                break;
//                                            }
//
//                                        }
//                                    }
//                                } else if (size==1) {
//                                    PemesananModel pemesanan = dataSnapshot.getValue(PemesananModel.class);
//                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();
//                                    String dicek = id;
//                                    try {
//                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
//                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if ((tglSewaPencarian.before(tglKembaliDipesan)
//                                            || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan)
//                                            || tglKembaliPencarian.equals(tglSewaDipesan))
//                                            || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
//                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
//                                        if (jmlKendaraanModel == a || jmlKendaraanModel > a) {
//                                            kendaraanModel.add(kendaraan);
//                                            adapter.notifyDataSetChanged();
//                                            Toast.makeText(getApplicationContext(), "add id "+id, Toast.LENGTH_LONG).show();
//
//                                        } else {
//                                            kendaraanModel.remove(kendaraan);
//                                            adapter.notifyDataSetChanged();
//                                            Toast.makeText(getApplicationContext(), "remove id "+id, Toast.LENGTH_LONG).show();
//                                        }
//
//                                    }
//                                }
//                            }
//                            else {
//                                kendaraanModel.add(kendaraan);
//                                adapter.notifyDataSetChanged();
//                                Toast.makeText(getApplicationContext(), "id "+id+" tidak ada di cek ketersediaan", Toast.LENGTH_LONG).show();
//                            }
//
//                            progressBar.setVisibility(View.GONE);
//
//                        }
//
//                        @Override
//                        public void onCancelled(FirebaseError firebaseError) {
//
//                        }
//                    });
//
////                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(DataSnapshot dataSnapshot) {
////                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
////                                    kendaraanModel.add(kendaraan);
////                                    adapter.notifyDataSetChanged();
////                                }
////                            }
////
////                            @Override
////                            public void onCancelled(DatabaseError databaseError) {
////
////                            }
////                        });
//                }
//            } else {
//                progressBar.setVisibility(View.GONE);
//                linearLayoutListKendaraan.setVisibility(View.GONE);
//                kendaraanTidakTersedia.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    });

    // -----------------------------------------------------------------------------------------------------------------//

//    public void getHasilPencarian() {
//        kendaraanModel.clear();
//        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
//        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
//        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
//        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
//        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
//        final ArrayList<Integer> listJumlah = new ArrayList<>();
//        final ArrayList<Integer> listJumlahModel = new ArrayList<>();
//
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            tglSewaPencarian = format.parse(tanggalSewaPencarian);
//            tglKembaliPencarian = format.parse(tanggalKembaliPencarian);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    progressBar.setVisibility(View.GONE);
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
//                        jmlKendaraan = kendaraan.getJumlahKendaraan();
//                        final String id = kendaraan.getIdKendaraan();
//                        final int jmlKendaraanModel = jmlKendaraan;
//
//                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/cekKetersediaanKendaraan");
//                        Query query = ref.orderByChild("idKendaraan").equalTo(id);
//                        query.addValueEventListener(new com.firebase.client.ValueEventListener() {
//                            @Override
//                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    long size = dataSnapshot.getChildrenCount();
//                                    for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                        PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
//                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                                        String dicek = id;
//                                        jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();
//                                        try {
//                                            tglSewaDipesan = format.parse(pemesanan.getTglSewa());
//                                            tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        if ((tglSewaPencarian.before(tglKembaliDipesan)
//                                                || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan)
//                                                || tglKembaliPencarian.equals(tglSewaDipesan))
//                                                || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
//
//                                            listJumlah.add(jmlKendaraanDipesan);
//                                            listJumlahModel.add(jmlKendaraanModel);
//                                            int jmlSeluruh = 0;
//                                            for (int i = 0; i < listJumlahModel.size(); i++) {
//                                                jmlSeluruh += listJumlahModel.get(i);
//                                                jmlKendaraanSeluruh = jmlSeluruh;
//                                            }
//                                            sum = 0;
//                                            for (int i = 0; i < listJumlah.size(); i++) {
//                                                sum += listJumlah.get(i);
//                                                jmlKendaraanDipesan = sum;
//                                            }
//                                            int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
//                                            int sisa = jmlKendaraanSeluruh - jmlKendaraanDipesan;
//                                            if (jmlKendaraanModel == a || jmlKendaraanModel > a) {
//                                                kendaraanModel.add(kendaraan);
//                                                adapter.notifyDataSetChanged();
//                                                Toast.makeText(getApplicationContext(), "add id "+id, Toast.LENGTH_LONG).show();
//                                                break;
//                                            } else {
//                                                kendaraanModel.remove(kendaraan);
//                                                adapter.notifyDataSetChanged();
//                                                Toast.makeText(getApplicationContext(), "remove id "+id, Toast.LENGTH_LONG).show();
//                                                break;
//                                            }
//
//                                        } else {
//                                            kendaraanModel.add(kendaraan);
//                                            adapter.notifyDataSetChanged();
//                                            Toast.makeText(getApplicationContext(), "id "+id+" tidak punya tanggal sama", Toast.LENGTH_LONG).show();
//                                            break;
//                                        }
//                                    }
//
//                                } else {
//                                    kendaraanModel.add(kendaraan);
//                                    adapter.notifyDataSetChanged();
//                                    Toast.makeText(getApplicationContext(), "id "+id+" tidak ada di cek ketersediaan", Toast.LENGTH_LONG).show();
//                                }
//
//                                progressBar.setVisibility(View.GONE);
//
//                            }
//
//                            @Override
//                            public void onCancelled(FirebaseError firebaseError) {
//
//                            }
//                        });
//
////                        mDatabase.child("cekKetersediaanKendaraan").addValueEventListener(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(DataSnapshot dataSnapshot) {
////                                if (!dataSnapshot.child(kendaraan.getIdKendaraan()).exists()) {
////                                    kendaraanModel.add(kendaraan);
////                                    adapter.notifyDataSetChanged();
////                                }
////                            }
////
////                            @Override
////                            public void onCancelled(DatabaseError databaseError) {
////
////                            }
////                        });
//                    }
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                    linearLayoutListKendaraan.setVisibility(View.GONE);
//                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
//        recyclerView.setAdapter(adapter);
//    }
}