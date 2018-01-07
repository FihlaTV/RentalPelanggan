package com.example.meita.rentalpelanggan.MenuPemesanan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPembayaran.DaftarRekeningPembayaran;
import com.example.meita.rentalpelanggan.MenuPembayaran.UnggahBuktiPembayaran;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.R;
import com.example.meita.rentalpelanggan.SisaKendaraanModel;
import com.example.meita.rentalpelanggan.Utils.ShowAlertDialog;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BuatPesanan2_tanpaSupir extends AppCompatActivity {
    private FirebaseAuth auth;
    TextView textViewIdentitasPelanggan, textViewNamaPelanggan, textViewAlamatPelanggan, textViewNomorTelponPelanggan,
            textViewEmailPelanggan;
    EditText editTextKeteranganKhusus;
    Spinner spinnerJamPengambilan;
    String idPelanggan, jamPengambilan, idPemesanan;
    Button buttonBuatPesanan;
    boolean statusUlasan;
    private boolean isSpinnerTouched = false;
    int jmlKendaraan, jmlKendaraanPencarian, jmlKendaraanDipesan, sum, hargaAwal, hargaAkhir;
    String idKendaraanDiEksekusi;
    Date tanggalSewaPencarian, tanggalKembaliPencarian, tglSewaDipesan, tglKembaliDipesan;
    boolean kendaraanTersedia = true;
    Date tglSewaPencarian, tglKembaliPencarian;
    int sisaKendaraan;
    boolean sisa;
    ProgressDialog progressDialog;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Buat Pesanan");
        setContentView(R.layout.activity_buat_pesanan2_tanpa_supir);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewIdentitasPelanggan = (TextView)findViewById(R.id.textViewIdentitasPelanggan);
        textViewNamaPelanggan = (TextView)findViewById(R.id.textViewNamaLengkap);
        textViewAlamatPelanggan = (TextView)findViewById(R.id.textViewAlamatLengkap);
        textViewNomorTelponPelanggan = (TextView)findViewById(R.id.textViewNomorTelpon);
        textViewEmailPelanggan = (TextView)findViewById(R.id.textViewEmail);
        editTextKeteranganKhusus = (EditText)findViewById(R.id.editTextKeteranganKhusus);
        buttonBuatPesanan = (Button)findViewById(R.id.btnBuatPesanan);
        progressDialog = new ProgressDialog(BuatPesanan2_tanpaSupir.this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //list dialog spinner jam penjemputan
        ArrayList<String> listJam = new ArrayList<>();
        listJam.add(new String("05.00"));
        listJam.add(new String("06.00"));
        listJam.add(new String("07.00"));
        listJam.add(new String("08.00"));
        listJam.add(new String("09.00"));
        listJam.add(new String("10.00"));
        listJam.add(new String("11.00"));
        listJam.add(new String("12.00"));
        spinnerJamPengambilan = (Spinner) findViewById(R.id.spinnerWaktuPengambilan);
        SpinnerAdapterJam adapterJam = new SpinnerAdapterJam(getApplicationContext(), R.layout.spinner_jam_layout, R.id.txtspinnerJam, listJam) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View itemView = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) itemView.findViewById(R.id.txtspinnerJam)).setText("");
                }
                return itemView;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        spinnerJamPengambilan.setAdapter(adapterJam);
        spinnerJamPengambilan.setSelection(adapterJam.getCount());
        spinnerJamPengambilan.setPrompt("Jam Pengambilan");
        spinnerJamPengambilan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });
        spinnerJamPengambilan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isSpinnerTouched){
                    String jam = null;
                    jamPengambilan = jam;
                }else {
                    String jam = adapterView.getItemAtPosition(i).toString();
                    jamPengambilan = jam;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonBuatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekKolomIsian()) {
                    if (cekSisa()) {
                        buatPesanan();
                        kelolaSisa();
                    }
                }
            }
        });
        infoPelanggan();
    }

    public void infoPelanggan() {
        mDatabase.child("pelanggan").child(idPelanggan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PelangganModel pelanggan = dataSnapshot.getValue(PelangganModel.class);
                textViewIdentitasPelanggan.setText(pelanggan.getNoIdentitas());
                textViewNamaPelanggan.setText(pelanggan.getNamaLengkap());
                textViewAlamatPelanggan.setText(pelanggan.getAlamat());
                textViewNomorTelponPelanggan.setText(pelanggan.getNoTelp());
                textViewEmailPelanggan.setText(pelanggan.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public  boolean cekSisa() {
        sisa = true;
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tanggalSewaPencarian = format.parse(tglSewaPencarian);
            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDatabase.child("cekSisaKendaraan").orderByChild("idKendaraan").equalTo(idKendaraan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String idCek = idKendaraan;
                        SisaKendaraanModel sisaModel = postSnapshot.getValue(SisaKendaraanModel.class);
                        int sisaKendaraan = sisaModel.getSisaKendaraan();

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            tglSewaDipesan = format.parse(sisaModel.getTglSewa());
                            tglKembaliDipesan = format.parse(sisaModel.getTglKembali());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if ((tanggalSewaPencarian.before(tglKembaliDipesan) || tanggalSewaPencarian.equals(tglKembaliDipesan)) && (tanggalKembaliPencarian.after(tglSewaDipesan) || tanggalKembaliPencarian.equals(tglSewaDipesan))
                                || tanggalSewaPencarian.equals(tglSewaDipesan) && tanggalKembaliPencarian.equals(tglKembaliDipesan)) {
                            String id = sisaModel.getIdCekSisa();
                            if (sisaKendaraan > jmlKendaraanPencarian || sisaKendaraan == jmlKendaraanPencarian) {
                                boolean cek = true;
                                sisa = cek;
                                //Toast.makeText(getApplicationContext(), "di update oiii", Toast.LENGTH_LONG).show();
                            }
                            else {
                                boolean cek = false;
                                sisa = cek;
                                //Toast.makeText(getApplicationContext(), "kendaraan udah abis alias ga cukup", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            boolean cek = true;
                            sisa = cek;
                            //Toast.makeText(getApplicationContext(), "Buat sisa karena tanggalnya ga sama", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                   boolean cek = true;
                   sisa = cek;
                   //Toast.makeText(getApplicationContext(), "Buat sisa karena ga ada sama sekali di tabel cek sisa", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return sisa;
    }

    // di oprek oprek
    public  void buatPesanan() {
        progressDialog.setMessage("Harap tunggu..."); // Setting Message
        progressDialog.setTitle("Memproses Penyewaan"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        final String idRental = getIntent().getStringExtra("idRental");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        final int jumlahHariPenyewaan = getIntent().getIntExtra("jumlahHariPenyewaan", 0);
        final double totalBiayaPembayaran = getIntent().getDoubleExtra("totalBiayaPembayaran", 0);
        final int jmlKendaraanPencarian = Integer.valueOf(jumlahKendaraanPencarian);
        final String idRekeningRental = "";
        statusUlasan = false;

        String keteranganKhusus = editTextKeteranganKhusus.getText().toString();

        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);
        Date a = calendar.getTime();
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        String batasWaktuPembayaran = df.format(a);

        final String id = mDatabase.push().getKey();
        idPemesanan = id;
        String statusPemesanan1 = "Belum Bayar";
        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        final PemesananModel dataPemesanan = new PemesananModel(idPemesanan, idKendaraan, idPelanggan, idRental, statusPemesanan1, currentDate, tglSewaPencarian,
                tglKembaliPencarian, keteranganKhusus, jamPengambilan, jmlKendaraanPencarian, jumlahHariPenyewaan, totalBiayaPembayaran, batasWaktuPembayaran,
                kategoriKendaraan, idRekeningRental, statusUlasan);
        mDatabase.child("pemesananKendaraan").child("belumBayar").child(id).setValue(dataPemesanan).
                addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Pemesanan Gagal", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Pemesanan berhasil", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(BuatPesanan2_tanpaSupir.this, DaftarRekeningPembayaran.class);
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idRental", idRental);
                    bundle.putString("idPemesanan", idPemesanan);
                    bundle.putString("kategoriKendaraan", kategoriKendaraan);
                    bundle.putString("tglSewaPencarian", tglSewaPencarian);
                    bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
                    bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
                    bundle.putInt("jumlahHariPenyewaan", jumlahHariPenyewaan);
                    bundle.putDouble("totalBiayaPembayaran", totalBiayaPembayaran);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    buatPemberitahuan();
                }
            }
        });
    }

    public void kelolaSisa() {
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tanggalSewaPencarian = format.parse(tglSewaPencarian);
            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("cekSisaKendaraan").orderByChild("idKendaraan").equalTo(idKendaraan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String idCek = idKendaraan;
                        SisaKendaraanModel sisaModel = postSnapshot.getValue(SisaKendaraanModel.class);
                        final int sisaKendaraan = sisaModel.getSisaKendaraan();
                        final String a = sisaModel.getIdCekSisa();

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            tglSewaDipesan = format.parse(sisaModel.getTglSewa());
                            tglKembaliDipesan = format.parse(sisaModel.getTglKembali());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if ((tanggalSewaPencarian.before(tglKembaliDipesan) || tanggalSewaPencarian.equals(tglKembaliDipesan)) && (tanggalKembaliPencarian.after(tglSewaDipesan) || tanggalKembaliPencarian.equals(tglSewaDipesan))
                                || tanggalSewaPencarian.equals(tglSewaDipesan) && tanggalKembaliPencarian.equals(tglKembaliDipesan)) {
                            if (sisaKendaraan > jmlKendaraanPencarian || sisaKendaraan == jmlKendaraanPencarian) {
                                perbaruiSisa(jmlKendaraanPencarian, a, sisaKendaraan);
                                //Toast.makeText(getApplicationContext(), "di update oiii", Toast.LENGTH_LONG).show();
                            }
                            else {
                                //Toast.makeText(getApplicationContext(), "kendaraan udah abis alias ga cukup", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            buatSisaKendaraan(jmlKendaraanPencarian);
                            //Toast.makeText(getApplicationContext(), "Buat sisa karena tanggalnya ga sama", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    buatSisaKendaraan(jmlKendaraanPencarian);
                    //Toast.makeText(getApplicationContext(), "Buat sisa karena ga ada sama sekali di tabel cek sisa", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void perbaruiSisa(final int jmlKendaraanPesanan, String idCek, int sisaModelKendaraan) {
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tanggalSewaPencarian = format.parse(tglSewaPencarian);
            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int sisaKendaraan = sisaModelKendaraan - jmlKendaraanPesanan;
        mDatabase.child("cekSisaKendaraan").child(idCek).child("sisaKendaraan").setValue(sisaKendaraan);
    }

    public void buatSisaKendaraan(final int jmlKendaraanPesanan) {
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        final String idCek = mDatabase.push().getKey();

        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
                int jmlKendaraanModel = dataKendaraan.getJumlahKendaraan();
                int sisa = jmlKendaraanModel - jmlKendaraanPesanan;
                sisaKendaraan = sisa;
                SisaKendaraanModel dataSisaKendaraan = new SisaKendaraanModel(idCek, tglSewaPencarian, tglKembaliPencarian, idKendaraan, sisaKendaraan);
                mDatabase.child("cekSisaKendaraan").child(idCek).setValue(dataSisaKendaraan);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void buatPemberitahuan() {
        String idPemberitahuan = mDatabase.push().getKey();
        final String idRental = getIntent().getStringExtra("idRental");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        String valueHalaman = "belumBayar";
        //int valueHalaman = 0;
        String statusPemesanan1 = "Belum Bayar";
        HashMap<String, Object> dataNotif = new HashMap<>();
        dataNotif.put("idPemberitahuan", idPemberitahuan);
        dataNotif.put("idRental", idRental);
        dataNotif.put("idKendaraan", idKendaraan);
        dataNotif.put("tglSewa", tglSewaPencarian);
        dataNotif.put("tglKembalian", tglKembaliPencarian);
        dataNotif.put("nilaiHalaman", valueHalaman);
        dataNotif.put("statusPemesanan", statusPemesanan1);
        dataNotif.put("idPelanggan", idPelanggan);
        dataNotif.put("idPemesanan", idPemesanan);
        mDatabase.child("pemberitahuan").child("rental").child("belumBayar").child(idRental).child(idPemberitahuan).setValue(dataNotif);
        //mDatabase.child("pemberitahuan").child("rental").child("belumBayar").child(idRental).child(idPemberitahuan).child("nilaiHalaman").setValue(valueHalaman);
    }

    public boolean cekKolomIsian() {
        boolean sukses = true;
        if ( editTextKeteranganKhusus.getText().toString() == null || jamPengambilan == null){
            sukses = false;
            ShowAlertDialog.showAlert("Lengkapi Seluruh Kolom Isian", this);
        }
        return sukses;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



//    public boolean cekKetersediaan() {
//        kendaraanTersedia = true;
//        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
//        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
//        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
//        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
//        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
//        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);
//        final ArrayList<Integer> listJumlah = new ArrayList<>();
//
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            tanggalSewaPencarian = format.parse(tglSewaPencarian);
//            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
//                jmlKendaraan = dataKendaraan.getJumlahKendaraan();
//                final int jmlKendaraanModel = jmlKendaraan;
//
//                mDatabase.child("cekKetersediaanKendaraan").orderByChild("idKendaraan").equalTo(dataKendaraan.getIdKendaraan()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                            PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
//                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                            jmlKendaraanDipesan = pemesanan.getJumlahKendaraan();
//
//                            try {
//                                tglSewaDipesan = format.parse(pemesanan.getTglSewa());
//                                tglKembaliDipesan = format.parse(pemesanan.getTglKembali());
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            if ((tanggalSewaPencarian.before(tglKembaliDipesan) || tanggalSewaPencarian.equals(tglKembaliDipesan)) && (tanggalKembaliPencarian.after(tglSewaDipesan) || tanggalKembaliPencarian.equals(tglSewaDipesan))
//                                    || tanggalSewaPencarian.equals(tglSewaDipesan) && tanggalKembaliPencarian.equals(tglKembaliDipesan)){
//                                listJumlah.add(jmlKendaraanDipesan);
//                                sum = 0;
//                                for (int i = 0; i < listJumlah.size(); i++) {
//                                    sum += listJumlah.get(i);
//                                    jmlKendaraanDipesan = sum;
//                                }
//                                int a = jmlKendaraanPencarian + jmlKendaraanDipesan;
//                                if (jmlKendaraanModel < a) {
//                                    Toast.makeText(getApplicationContext(), "Kendaraan yang anda pilih sudah tidak tersedia", Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(BuatPesanan2_tanpaSupir.this, MainActivity.class);
//                                    startActivity(intent);
//                                    kendaraanTersedia = false;
//                                }
//                            }
//                            break;
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        return kendaraanTersedia;
//
//    }

    //belom di oprek oprek
//    public  void buatPesanan() {
//        final String idRental = getIntent().getStringExtra("idRental");
//        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
//        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
//        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
//        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
//        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
//        final int jumlahHariPenyewaan = getIntent().getIntExtra("jumlahHariPenyewaan", 0);
//        final double totalBiayaPembayaran = getIntent().getDoubleExtra("totalBiayaPembayaran", 0);
//        final int jmlKendaraanPencarian = Integer.valueOf(jumlahKendaraanPencarian);
//        final String idRekeningRental = "";
//        statusUlasan = false;
//
//        String keteranganKhusus = editTextKeteranganKhusus.getText().toString();
//
//        Date date = new Date();
//        final Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.HOUR, 1);
//        Date a = calendar.getTime();
//        DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
//        String batasWaktuPembayaran = df.format(a);
//
//        final String id = mDatabase.push().getKey();
//        idPemesanan = id;
//        String statusPemesanan1 = "Belum Bayar";
//        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
//        final PemesananModel dataPemesanan = new PemesananModel(idPemesanan, idKendaraan, idPelanggan, idRental, statusPemesanan1, currentDate, tglSewaPencarian,
//                tglKembaliPencarian, keteranganKhusus, jamPengambilan, jmlKendaraanPencarian, jumlahHariPenyewaan, totalBiayaPembayaran, batasWaktuPembayaran,
//                kategoriKendaraan, idRekeningRental, statusUlasan);
//        mDatabase.child("pemesananKendaraan").child("belumBayar").child(id).setValue(dataPemesanan).
//                addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Pemesanan Gagal", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Pemesanan berhasil", Toast.LENGTH_SHORT).show();
//                            mDatabase.child("cekKetersediaanKendaraan").child(id).setValue(dataPemesanan);
//                            Bundle bundle = new Bundle();
//                            Intent intent = new Intent(BuatPesanan2_tanpaSupir.this, DaftarRekeningPembayaran.class);
//                            bundle.putString("idKendaraan", idKendaraan);
//                            bundle.putString("idRental", idRental);
//                            bundle.putString("idPemesanan", idPemesanan);
//                            bundle.putString("kategoriKendaraan", kategoriKendaraan);
//                            bundle.putString("tglSewaPencarian", tglSewaPencarian);
//                            bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
//                            bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
//                            bundle.putInt("jumlahHariPenyewaan", jumlahHariPenyewaan);
//                            bundle.putDouble("totalBiayaPembayaran", totalBiayaPembayaran);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    }
//                });
//    }
}
