package com.example.meita.rentalpelanggan.MenuPencarian;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.meita.rentalpelanggan.MenuPemesanan.PemesananModel;
import com.example.meita.rentalpelanggan.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuHasilPencarian extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MenuHasilPencarianAdapter adapter;
    private List<KendaraanModel> kendaraanModel;
    private DatabaseReference mDatabase;
    Date tglSewaPencarian, tglKembaliPencarian, tglSewaDipesan, tglKembaliDipesan;
    int jmlKendaraan, jmlKendaraanPencarian, jmlKendaraanDipesan, sum, hargaAwal, hargaAkhir;
    String idKendaraanChecking;
    TextView kategoriToolbar, tglToolbar;
    ProgressBar progressBar;
    Button buttonFilter, buttonTerdekat;
    ImageView kendaraanTidakTersedia;
    LinearLayout linearLayoutListKendaraan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hasil Pencarian");
        setContentView(R.layout.activity_menu_hasil_pencarian);
        Firebase.setAndroidContext(this);

        recyclerView = (RecyclerView) findViewById(R.id.listViewKendaraan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kendaraanModel = new ArrayList<>();
        kategoriToolbar = (TextView)findViewById(R.id.kategoriKendaraan);
        tglToolbar = (TextView)findViewById(R.id.tglSewa);
        buttonFilter = (Button)findViewById(R.id.btn_filter);
        buttonTerdekat = (Button)findViewById(R.id.btn_terdekat);
        kendaraanTidakTersedia = (ImageView)findViewById(R.id.ic_kendaran_noavailable);
        linearLayoutListKendaraan = (LinearLayout)findViewById(R.id.linearLayoutListKendaraan);

        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);
        kendaraanTidakTersedia.setVisibility(View.GONE);


        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String tanggalSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tanggalKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");

        String tgl = tanggalSewaPencarian + " - " + tanggalKembaliPencarian;
        kategoriToolbar.setText(kategoriKendaraanPencarian);
        tglToolbar.setText(tgl);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFilterPencarian();
            }
        });

        getHasilPencarian();
    }

    public void showDialogFilterPencarian(){
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
        Button btnYa = (Button) dialog.findViewById(R.id.btn_filter_ya);
        Button btnTidak = (Button) dialog.findViewById(R.id.btn_filter_tidak);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                txthargaAwal.setText(String.valueOf(minValue));
                txthargaAkhir.setText(String.valueOf(maxValue));
                hargaAwal = minValue.intValue();
                hargaAkhir = maxValue.intValue();
            }
        });

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dialog.getContext(), String.valueOf(hargaAwal)+" - "+String.valueOf(hargaAkhir), Toast.LENGTH_SHORT).show();
                getFilterPencarian();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void getFilterPencarian() {
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
                progressBar.setVisibility(View.VISIBLE); //progress bar mulai
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                    idKendaraanChecking = kendaraan.getIdKendaraan();
                    jmlKendaraan = kendaraan.getJumlahKendaraan();
                    final int jmlKendaraanModel = jmlKendaraan;

                    mDatabase.child("cekKetersediaanKendaraan").orderByChild("idKendaraan").equalTo(idKendaraanChecking).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();

                                try {
                                    tglSewaDipesan= format.parse(pemesanan.getTglSewa());
                                    tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan) || tglKembaliPencarian.equals(tglSewaDipesan))
                                        || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)){
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
                        public void onCancelled(DatabaseError databaseError) {

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

                } // breakpoint for dalam mDatabase dataChange child kendaraan

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // breakpoint addValueEventListener query child kendaraan

        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        //progress bar berhenti ketika cardview muncul

    }

    public void getHasilPencarian() {
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

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                        jmlKendaraan = kendaraan.getJumlahKendaraan();
                        final int jmlKendaraanModel = jmlKendaraan;

                        mDatabase.child("cekKetersediaanKendaraan").orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();
                                    try {
                                        tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                        tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan)
                                            || tglKembaliPencarian.equals(tglSewaDipesan)) || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)) {
                                        listJumlah.add(jmlKendaraanDipesan);
                                        sum = 0;
                                        for (int i = 0; i < listJumlah.size(); i++) {
                                            sum += listJumlah.get(i);
                                            jmlKendaraanDipesan = sum;
                                        }
                                        int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                        if (jmlKendaraanModel < a) {
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
                            public void onCancelled(DatabaseError databaseError) {

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
                    progressBar.setVisibility(View.GONE);
                    linearLayoutListKendaraan.setVisibility(View.GONE);
                    kendaraanTidakTersedia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        recyclerView.setAdapter(adapter);
    }

    public void getHasilPencarianCumaTglSewa() {
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

        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final KendaraanModel kendaraan = postSnapshot.getValue(KendaraanModel.class);
                    jmlKendaraan = kendaraan.getJumlahKendaraan();
                    final int jmlKendaraanModel = jmlKendaraan;

                    mDatabase.child("cekKetersediaanKendaraan").orderByChild("idKendaraan").equalTo(kendaraan.getIdKendaraan()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();
                                try {
                                    tglSewaDipesan = format.parse(pemesanan.getTglSewa());
                                    tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if ((tglSewaPencarian.before(tglKembaliDipesan) || tglSewaPencarian.equals(tglKembaliDipesan)) && (tglKembaliPencarian.after(tglSewaDipesan)
                                        || tglKembaliPencarian.equals(tglSewaDipesan)) || tglSewaPencarian.equals(tglSewaDipesan) && tglKembaliPencarian.equals(tglKembaliDipesan)){
                                    listJumlah.add(jmlKendaraanDipesan);
                                    sum = 0;
                                    for (int i = 0; i < listJumlah.size(); i++) {
                                        sum += listJumlah.get(i);
                                        jmlKendaraanDipesan = sum;
                                    }
                                    int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
                                    if (jmlKendaraanModel < a) {
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
                        public void onCancelled(DatabaseError databaseError) {

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tanggalSewaPencarian, tanggalKembaliPencarian, jumlahKendaraanPencarian);
        recyclerView.setAdapter(adapter);
    }
}


        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final KendaraanModel dataKendaraan = postSnapshot.getValue(KendaraanModel.class);
                    final int jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
                    int jmlKendaraanModel = dataKendaraan.getJumlahKendaraan();

                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/pemesanan"
                        );
                        Query query = ref.orderByChild("idKendaraan").equalTo(dataKendaraan.getIdKendaraan());
                        query.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (postSnapshot != null) {
                                        PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                        if (pemesanan != null) {
                                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                            try {
                                                tanggalSewaReserved = format.parse(pemesanan.getTglSewa());
                                                tanggalKembaliReserved = format.parse(pemesanan.getTglKembali());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if ((tanggalSewaPencarian.after(tanggalSewaReserved) && tanggalSewaPencarian.before(tanggalKembaliReserved)) || (tanggalKembaliPencarian.after(tanggalSewaReserved) && tanggalKembaliReserved.before(tanggalKembaliReserved))) {
                                                if (jmlKendaraanPencarian + (pemesanan.getJumlahKendaraan()) <= dataKendaraan.getJumlahKendaraan()) {
                                                    kendaraanModel.add(dataKendaraan);
                                                } else {
                                                    kendaraanModel.remove(dataKendaraan);
                                                }

                                            } else {
                                                kendaraanModel.add(dataKendaraan);
                                                adapter.notifyDataSetChanged();

                                            }

                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        mDatabase.child("pemesanan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(dataKendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(dataKendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getApplicationContext(), "TABEL PENYEWAAN TIDAAAAKKK ADA", Toast.LENGTH_SHORT).show();
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewaPencarian, tglKembaliPencarian, jumlahKendaraanPencarian);
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter); /*
    }
}

        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final KendaraanModel dataKendaraan = postSnapshot.getValue(KendaraanModel.class);
                    final int jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
                    int jmlKendaraanModel = dataKendaraan.getJumlahKendaraan();

                    if (jmlKendaraanPencarian <= jmlKendaraanModel) {
                        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/pemesanan"
                        );
                        Query query = ref.orderByChild("idKendaraan").equalTo(dataKendaraan.getIdKendaraan());
                        query.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                for (com.firebase.client.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (postSnapshot != null) {
                                        PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                                        if (pemesanan != null) {
                                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                            try {
                                                tanggalSewaReserved = format.parse(pemesanan.getTglSewa());
                                                tanggalKembaliReserved = format.parse(pemesanan.getTglKembali());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            if ((tanggalSewaPencarian.after(tanggalSewaReserved) && tanggalSewaPencarian.before(tanggalKembaliReserved)) || (tanggalKembaliPencarian.after(tanggalSewaReserved) && tanggalKembaliReserved.before(tanggalKembaliReserved))) {
                                                kendaraanModel.remove(dataKendaraan);
                                            } else {
                                                kendaraanModel.add(dataKendaraan);
                                                adapter.notifyDataSetChanged();



                                            }

                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        mDatabase.child("pemesanan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(dataKendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(dataKendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(getApplicationContext(), "TABEL PENYEWAAN TIDAAAAKKK ADA", Toast.LENGTH_SHORT).show();
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewaPencarian, tglKembaliPencarian, jumlahKendaraanPencarian);
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter); */

        /*mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final KendaraanModel dataKendaraan = postSnapshot.getValue(KendaraanModel.class);
                    final int jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
                    int jmlKendaraanModel = dataKendaraan.getJumlahKendaraan();

                    if (jmlKendaraanPencarian <= jmlKendaraanModel) {

                        mDatabase.child("pemesanan").child(dataKendaraan.getIdKendaraan()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    PemesananModel pemesanan = dataSnapshot.getValue(PemesananModel.class); // ERROR HERE
                                    if (pemesanan != null) {
                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                        try {
                                            tanggalSewaReserved = format.parse(pemesanan.getTglSewa());
                                            tanggalKembaliReserved = format.parse(pemesanan.getTglKembali());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if ((tanggalSewaPencarian.after(tanggalSewaReserved) && tanggalSewaPencarian.before(tanggalKembaliReserved)) || (tanggalKembaliPencarian.after(tanggalSewaReserved) && tanggalKembaliReserved.before(tanggalKembaliReserved))) {
                                            kendaraanModel.remove(dataKendaraan);
                                        } else {
                                            kendaraanModel.add(dataKendaraan);
                                            adapter.notifyDataSetChanged();



                                        }


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mDatabase.child("pemesanan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(dataKendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(dataKendaraan);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(getApplicationContext(), "TABEL PENYEWAAN TIDAAAAKKK ADA", Toast.LENGTH_SHORT).show();
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewaPencarian, tglKembaliPencarian, jumlahKendaraanPencarian);
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter); */


/*mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final KendaraanModel dataKendaraan = postSnapshot.getValue(KendaraanModel.class);
                    final int jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
                    int jmlKendaraanModel = dataKendaraan.getJumlahKendaraan();

                    if (jmlKendaraanPencarian <= jmlKendaraanModel) {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            tanggalSewaPencarian = format.parse(tglSewaPencarian);
                            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mDatabase.child("pemesanan").child(dataKendaraan.getIdKendaraan()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                    //ERROR HERE
                                    PemesananModel pemesanan = dataSnapshot.getValue(PemesananModel.class); // ERROR HERE
                                    long date1 = pemesanan.getTglSewa();
                                    long date2 = pemesanan.getTglKembali();
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                    try {
                                        tglSewaReserved = format.parse(date1);
                                        tglKembaliReserved = format.parse(date2);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if ((tanggalSewaPencarian.after(tglSewaReserved) && tanggalSewaPencarian.before(tglKembaliReserved)) || (tanggalKembaliPencarian.after(tglSewaReserved) && tglKembaliReserved.before(tglKembaliReserved))) {
                                        kendaraanModel.remove(dataKendaraan);
                                    } else {
                                        kendaraanModel.add(dataKendaraan);
                                    }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mDatabase.child("pemesanan").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(dataKendaraan.getIdKendaraan()).exists()) {
                                    kendaraanModel.add(dataKendaraan);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewaPencarian, tglKembaliPencarian, jumlahKendaraanPencarian);
        //adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(), "TABEL PENYEWAAN TIDAAAAKKK ADA", Toast.LENGTH_SHORT).show(); */




        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("pemesanan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Toast.makeText(getApplicationContext(), "TABEL PEMESANAN ADA", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                KendaraanModel upload = postSnapshot.getValue(KendaraanModel.class);
                                kendaraanModel.add(upload);
                            }
                            adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewaPencarian, tglKembaliPencarian, jumlahKendaraanPencarian);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            //Toast.makeText(getApplicationContext(), "TABEL PENYEWAAN TIDAAAAKKK ADA", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

    /*public void pencarian() {
        final String kategoriKendaraanPencarian = getIntent().getStringExtra("kategoriKendaraanPencarian");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        mDatabase.child("kendaraan").child(kategoriKendaraanPencarian).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {

                    final KendaraanModel dataKendaraan = child.getValue(KendaraanModel.class);
                    final int jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
                    int jmlKendaraanModel = dataKendaraan.getJumlahKendaraan();

                    if (jmlKendaraanPencarian <= jmlKendaraanModel) {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            tanggalSewaPencarian = format.parse(tglSewaPencarian);
                            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mDatabase.child("pemesanan").child(dataKendaraan.getIdKendaraan()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    PemesananModel pemesanan = child.getValue(PemesananModel.class);
                                    String date1 = pemesanan.getTglSewa();
                                    String date2 = pemesanan.getTglKembali();
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                    try {
                                        tglSewaReserved = format.parse(date1);
                                        tglKembaliReserved = format.parse(date2);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if ((tanggalSewaPencarian.after(tglSewaReserved) && tanggalSewaPencarian.before(tglKembaliReserved)) || (tanggalKembaliPencarian.after(tglSewaReserved) && tglKembaliReserved.before(tglKembaliReserved))) {
                                        kendaraanModel.remove(dataKendaraan);
                                        break;
                                    } else {
                                        kendaraanModel.add(dataKendaraan);
                                        adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewaPencarian, tglKembaliPencarian, jumlahKendaraanPencarian);
                                        //adding adapter to recyclerview
                                        recyclerView.setAdapter(adapter);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } */

       /* Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com");
        Query query = ref.child("pemesanan");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // kodingan berdasarkan datenya
                    //Toast.makeText(getApplicationContext(), "TABEL PEMESANAN ADA", Toast.LENGTH_SHORT).show();
                } else {
                    Firebase ref1 = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/kendaraan");
                    Query query2 = ref1.child(kategoriKendaraan);
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                KendaraanModel upload = postSnapshot.getValue(KendaraanModel.class);
                                kendaraanModel.add(upload);
                            }
                            adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewa, tglKembali, jumlahKendaraan);
                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            //Toast.makeText(getApplicationContext(), "TABEL PENYEWAAN TIDAAAAKKK ADA", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }); */



        /*if (query == null) {
            Firebase ref1 = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/KendaraanNEW");
            Query query2 = ref1.child(kategoriKendaraan).equalTo(kategoriKendaraan);
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        KendaraanModel upload = postSnapshot.getValue(KendaraanModel.class);
                        kendaraanModel.add(upload);
                        //Log.d(TAG, "valuenya apa");
                    }
                    adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewa, tglKembali);
                    //adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("KendaraanNEW").child(kategoriKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    KendaraanModel dataKendaraan = postSnapshot.getValue(KendaraanModel.class);
                    if (postSnapshot.hasChild("Penyewaan")) {



                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */



// ----------- KODINGAN LAMA YANG NAMPILIN BERDASARKAN KATEGORI AJA ----------- //
// dalam method onCreate
        /*List<Date> dates = getDates(tglSewa, tglKembali);
        ArrayList<String> rentangTglSewa = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        for (Date date : dates) {
            rentangTglSewa.add(fmt.format(date));
        }

        Firebase ref = new Firebase("https://bismillahskripsi-44a73.firebaseio.com/Kendaraan");
        Query query = ref.orderByChild("kategoriKendaraan").equalTo(kategoriKendaraan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    KendaraanModel upload = postSnapshot.getValue(KendaraanModel.class);
                    kendaraanModel.add(upload);
                    //Log.d(TAG, "valuenya apa");
                }
                adapter = new MenuHasilPencarianAdapter(MenuHasilPencarian.this, kendaraanModel, tglSewa, tglKembali);
                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }); */

// --------- METHOD UNTUK DAPAT RENTANG TANGGAL ------------- //
    /*private static List<Date> getDates(String tglSewa, String tglKembali) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date tanggalSewa = null;
        Date tanggalKembali = null;

        try {
            tanggalSewa = formatter.parse(tglSewa);
            tanggalKembali = formatter.parse(tglKembali);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(tanggalSewa);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(tanggalKembali);

        while(!cal1.after(cal2))
        {

            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);

        }
        return dates;
    } */
