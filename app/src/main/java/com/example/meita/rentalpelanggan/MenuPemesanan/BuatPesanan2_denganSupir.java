package com.example.meita.rentalpelanggan.MenuPemesanan;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPembayaran.DaftarRekeningPembayaran;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.MenuHasilPencarian;
import com.example.meita.rentalpelanggan.MenuPencarian.MenuHasilPencarianAdapter;
import com.example.meita.rentalpelanggan.R;
import com.example.meita.rentalpelanggan.Utils.ShowAlertDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class BuatPesanan2_denganSupir extends AppCompatActivity {
    private FirebaseAuth auth;
    TextView textViewIdentitasPelanggan, textViewNamaPelanggan, textViewAlamatPelanggan, textViewNomorTelponPelanggan,
    textViewEmailPelanggan;
    EditText editTextLokasiPenjemputan, editTextKeteranganKhusus;
    Spinner spinnerJamPenjemputan;
    Button buttonBuatPesanan;
    String idPelanggan, jamPenjemputan, idPemesanan;
    DatabaseReference mDatabase;
    double latitude_penjemputan, longitude_penjemputan;
    boolean statusUlasan;
    private boolean isSpinnerTouched = false;
    int jmlKendaraan, jmlKendaraanPencarian, jmlKendaraanReserved, sum, hargaAwal, hargaAkhir;
    String idKendaraanChecking;
    Date tanggalSewaPencarian, tanggalKembaliPencarian, tglSewaReserved, tglKembaliReserved;
    boolean kendaraanTersedia;


    private final static int PLACE_PICKER_REQUEST = 2;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Buat Pesanan");
        setContentView(R.layout.activity_buat_pesanan2_dengan_supir);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewIdentitasPelanggan = (TextView)findViewById(R.id.textViewIdentitasPelanggan);
        textViewNamaPelanggan = (TextView)findViewById(R.id.textViewNamaLengkap);
        textViewAlamatPelanggan = (TextView)findViewById(R.id.textViewAlamatLengkap);
        textViewNomorTelponPelanggan = (TextView)findViewById(R.id.textViewNomorTelpon);
        textViewEmailPelanggan = (TextView)findViewById(R.id.textViewEmail);
        editTextLokasiPenjemputan = (EditText)findViewById(R.id.editTextLokasiPenjemputan);
        editTextKeteranganKhusus = (EditText)findViewById(R.id.editTextKeteranganKhusus);
        buttonBuatPesanan = (Button)findViewById(R.id.btnBuatPesanan);
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
        spinnerJamPenjemputan = (Spinner) findViewById(R.id.spinnerWaktuPenjemputan);
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
        spinnerJamPenjemputan.setAdapter(adapterJam);
        spinnerJamPenjemputan.setSelection(adapterJam.getCount());
        spinnerJamPenjemputan.setPrompt("Jam Penjemputan");
        spinnerJamPenjemputan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });
        spinnerJamPenjemputan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isSpinnerTouched){
                    String jam = null;
                    jamPenjemputan = jam;
                }else {
                    String jam = adapterView.getItemAtPosition(i).toString();
                    jamPenjemputan = jam;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cekKetersediaan();

        buttonBuatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekKolomIsian()) {
                    if (cekKetersediaan()) {
                        buatPesanan();
                        finish();
                    }
                }
            }
        });

        editTextLokasiPenjemputan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lokasiPenjemputan();
            }
        });

        infoPelanggan();
    }

    public void infoPelanggan(){
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

    public  void buatPesanan() {
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
        String alamatPenjemputan = editTextLokasiPenjemputan.getText().toString();

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

        final PemesananModel dataPemesanan = new PemesananModel(id, idKendaraan, idPelanggan, idRental, statusPemesanan1, currentDate, tglSewaPencarian,
                tglKembaliPencarian, keteranganKhusus, jamPenjemputan, jmlKendaraanPencarian, latitude_penjemputan, longitude_penjemputan, alamatPenjemputan,
                jumlahHariPenyewaan, totalBiayaPembayaran, batasWaktuPembayaran, kategoriKendaraan, idRekeningRental, statusUlasan);

        mDatabase.child("pemesananKendaraan").child("belumBayar").child(id).setValue(dataPemesanan).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Pemesanan Gagal", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Pemesanan berhasil", Toast.LENGTH_SHORT).show();
                    mDatabase.child("cekKetersediaanKendaraan").child(id).setValue(dataPemesanan);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(BuatPesanan2_denganSupir.this, DaftarRekeningPembayaran.class);
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
                }
            }
        });
    }

    public boolean cekKolomIsian() {
            boolean sukses = true;
            if ( editTextKeteranganKhusus.getText().toString() == null || editTextLokasiPenjemputan.getText().toString() == null || jamPenjemputan == null){
                sukses = false;
                ShowAlertDialog.showAlert("Lengkapi Seluruh Kolom Isian", this);
            }
            return sukses;
        }

    public void lokasiPenjemputan() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            Intent intent = builder.build(BuatPesanan2_denganSupir.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public boolean cekKetersediaan() {
        kendaraanTersedia = true;
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");

        jmlKendaraanPencarian = Integer.parseInt(jumlahKendaraanPencarian);

        final ArrayList<Integer> listJumlah = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            tanggalSewaPencarian = format.parse(tglSewaPencarian);
            tanggalKembaliPencarian = format.parse(tglKembaliPencarian);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
                jmlKendaraan = dataKendaraan.getJumlahKendaraan();
                idKendaraanChecking = dataKendaraan.getIdKendaraan();
                final int jmlKendaraanModel = jmlKendaraan;

                mDatabase.child("cekKetersediaanKendaraan").orderByChild("idKendaraan").equalTo(idKendaraanChecking).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            PemesananModel pemesanan = postSnapshot.getValue(PemesananModel.class);
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            jmlKendaraanReserved = pemesanan.getJumlahKendaraan();

                            try {
                                tglSewaReserved = format.parse(pemesanan.getTglSewa());
                                tglKembaliReserved = format.parse(pemesanan.getTglKembali());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if ((tanggalSewaPencarian.before(tglKembaliReserved) || tanggalSewaPencarian.equals(tglKembaliReserved)) && (tanggalKembaliPencarian.after(tglSewaReserved) || tanggalKembaliPencarian.equals(tglSewaReserved))
                                    || tanggalSewaPencarian.equals(tglSewaReserved) && tanggalKembaliPencarian.equals(tglKembaliReserved)){
                                listJumlah.add(jmlKendaraanReserved);
                                sum = 0;
                                for (int i = 0; i < listJumlah.size(); i++) {
                                    sum += listJumlah.get(i);
                                    jmlKendaraanReserved = sum;
                                }
                                int a = jmlKendaraanPencarian + jmlKendaraanReserved;
                                if (jmlKendaraanModel < a) {
                                    Toast.makeText(getApplicationContext(), "Kendaraan yang anda pilih sudah tidak tersedia", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(BuatPesanan2_denganSupir.this, MainActivity.class);
                                    startActivity(intent);
                                    kendaraanTersedia = false;
                                }
                            }
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return kendaraanTersedia;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PLACE_PICKER_REQUEST && resultCode==RESULT_OK && data != null){
            Place place = PlacePicker.getPlace(BuatPesanan2_denganSupir.this, data);
            latitude_penjemputan=place.getLatLng().latitude;
            longitude_penjemputan=place.getLatLng().longitude;

            this.editTextLokasiPenjemputan.setText(""+place.getAddress());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
