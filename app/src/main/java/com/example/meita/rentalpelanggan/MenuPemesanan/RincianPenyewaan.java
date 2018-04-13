package com.example.meita.rentalpelanggan.MenuPemesanan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RincianPenyewaan extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    TextView textViewTipeKendaraan, textViewNamaRental, textViewDenganSupir, textViewTanpaSupir,
    textViewDenganBBM, textViewTanpaBBM, textViewAreaPemakaian, textViewTotalPembayaran, textViewLamaSewaKendaraan;
    Button buttonLanjutkan;
    ImageView imageChecklistSupirTrue, imageCheckListSupirFalse, imageCheckListBBMTrue, imageCheckListBBMFalse;
    boolean valueSupir;
    double totalBiayaPembayaran;
    ProgressBar progressBar;
    TextView textViewTglSewa, textViewTglKembali, textViewJumlahSewaKendaraan, textViewJmlHariPenyewaan;
    TextView textViewHargaSewaKendaraan, textViewLamaPenyewaan, textViewJumlahKendaraan, textViewLamaSewaPemesanan;
    public int jumlahHariPenyewaan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rincian Penyewaan");
        setContentView(R.layout.activity_rincian_pesanan);

        textViewTipeKendaraan = (TextView)findViewById(R.id.textViewTipeKendaraan);
        textViewNamaRental = (TextView)findViewById(R.id.textViewNamaRentalKendaraan);
        textViewDenganSupir = (TextView)findViewById(R.id.textViewDenganSupir);
        textViewTanpaSupir = (TextView)findViewById(R.id.textViewTanpaSupir);
        textViewDenganBBM = (TextView)findViewById(R.id.textViewDenganBBM);
        textViewTanpaBBM = (TextView)findViewById(R.id.textViewTanpaBBM);
        textViewAreaPemakaian = (TextView)findViewById(R.id.textViewAreaPemakaian);
        textViewTotalPembayaran = (TextView)findViewById(R.id.txtViewTotalPembayaran);

        imageChecklistSupirTrue = (ImageView)findViewById(R.id.icCheckListDenganSupir);
        imageCheckListSupirFalse = (ImageView)findViewById(R.id.icCheckListTanpaSupir);
        imageCheckListBBMTrue = (ImageView)findViewById(R.id.icCheckListDenganBBM);
        imageCheckListBBMFalse = (ImageView)findViewById(R.id.icCheckListTanpaBBM);

        textViewTglSewa = (TextView)findViewById(R.id.textViewTglSewa);
        textViewTglKembali = (TextView)findViewById(R.id.textViewTglKembali);

        textViewHargaSewaKendaraan = (TextView)findViewById(R.id.textViewHargaSewaKendaraan);
        textViewLamaPenyewaan = (TextView)findViewById(R.id.textViewLamaPenyewaan);
        textViewJumlahKendaraan = (TextView)findViewById(R.id.textViewJumlahKendaraan);
        textViewLamaSewaPemesanan = (TextView)findViewById(R.id.textViewLamaSewaPemesanan);


//        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
//        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
//        progressBar.setVisibility(View.VISIBLE);

        buttonLanjutkan = (Button)findViewById(R.id.btnLanjutkan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        final String idRental = getIntent().getStringExtra("idRental");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        textViewJumlahKendaraan.setText(jumlahKendaraanPencarian);
        textViewTglSewa.setText(tglSewaPencarian);
        textViewTglKembali.setText(tglKembaliPencarian);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueSupir == true) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(RincianPenyewaan.this, BuatPenyewaan_denganSupir.class);
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idRental", idRental);
                    bundle.putString("kategoriKendaraan", kategoriKendaraan);
                    bundle.putString("tglSewaPencarian", tglSewaPencarian);
                    bundle.putString("tglKembaliPencarian", tglKembaliPencarian);
                    bundle.putString("jumlahKendaraanPencarian", jumlahKendaraanPencarian);
                    bundle.putInt("jumlahHariPenyewaan", jumlahHariPenyewaan);
                    bundle.putDouble("totalBiayaPembayaran", totalBiayaPembayaran);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(RincianPenyewaan.this, BuatPenyewaan_tanpaSupir.class);
                    bundle.putString("idKendaraan", idKendaraan);
                    bundle.putString("idRental", idRental);
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

        try {
            totalBiayaSewa();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        infoKendaraan();
        infoRentalKendaraan();

//        try {
//            getJumlahHariPenyewaan();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }



    public void infoKendaraan() {
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
                textViewTipeKendaraan.setText(kendaraan.getTipeKendaraan());
                textViewAreaPemakaian.setText(kendaraan.getAreaPemakaian());
                textViewHargaSewaKendaraan.setText("Rp." + BaseActivity.rupiah().format(kendaraan.getHargaSewa()));
                textViewLamaPenyewaan.setText(kendaraan.getLamaPenyewaan());

                if (kendaraan.isSupir() == true ) {
                    textViewDenganSupir.setVisibility(View.VISIBLE);
                    imageChecklistSupirTrue.setVisibility(View.VISIBLE);
                    textViewTanpaSupir.setVisibility(View.GONE);
                    imageCheckListSupirFalse.setVisibility(View.GONE);
                } else {
                    textViewTanpaSupir.setVisibility(View.VISIBLE);
                    imageCheckListSupirFalse.setVisibility(View.VISIBLE);
                    textViewDenganSupir.setVisibility(View.GONE);
                    imageChecklistSupirTrue.setVisibility(View.GONE);
                }

                if (kendaraan.isBahanBakar() == true ) {
                    textViewDenganBBM.setVisibility(View.VISIBLE);
                    imageCheckListBBMTrue.setVisibility(View.VISIBLE);
                    textViewTanpaBBM.setVisibility(View.GONE);
                    imageCheckListBBMFalse.setVisibility(View.GONE);
                } else {
                    textViewTanpaBBM.setVisibility(View.VISIBLE);
                    imageCheckListBBMFalse.setVisibility(View.VISIBLE);
                    textViewDenganBBM.setVisibility(View.GONE);
                    imageCheckListBBMTrue.setVisibility(View.GONE);
                }

                boolean supir = kendaraan.isSupir();
                valueSupir = supir;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        progressBar.setVisibility(View.GONE);
    }

    public void infoRentalKendaraan() {
        final String idRental = getIntent().getStringExtra("idRental");
        mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                textViewNamaRental.setText(dataRental.getNama_rental());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void totalBiayaSewa() throws ParseException  { //1
        final String idKendaraan = getIntent().getStringExtra("idKendaraan"); //2
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
        final int jmlKendaraan = Integer.parseInt(jumlahKendaraanPencarian);
        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() { //3
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class); //4
                String lamaPenyewaan = kendaraan.getLamaPenyewaan();
                boolean fasilitasSupir = kendaraan.isSupir();
                double hargaSewa = kendaraan.getHargaSewa();
                String durasiPenyewaanKendaraan = "12 Jam";
                double total = 0;
                try { //5
                    if ((lamaPenyewaan.equals(durasiPenyewaanKendaraan) && fasilitasSupir == true)) { //6
                        total = (getJumlahHariPenyewaan() * hargaSewa) * jmlKendaraan; //7
                        totalBiayaPembayaran = total;
                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
                    }
                    else if ((lamaPenyewaan.equals(durasiPenyewaanKendaraan) && fasilitasSupir == false && getJumlahHariPenyewaan()==1)) { //8
                        total = (getJumlahHariPenyewaan() * hargaSewa) * jmlKendaraan; //9
                        //int valueJumlahHariPenyewaan = getJumlahHariPenyewaan();
                        totalBiayaPembayaran = total;
                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
                    }
                    else if ((lamaPenyewaan.equals(durasiPenyewaanKendaraan) && fasilitasSupir == false && getJumlahHariPenyewaan()>1)) { //10
                        total = (getJumlahHariPenyewaan() * (hargaSewa*2)) * jmlKendaraan; //11
                        totalBiayaPembayaran = total;
                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
                    } else { //12
                        total = (getJumlahHariPenyewaan() * (hargaSewa)) * jmlKendaraan; //13
                        totalBiayaPembayaran = total;
                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
                    }
                } catch (ParseException e) { //14
                    Toast.makeText(getApplicationContext(), "Terdapat Kesalahan pada Sistem", Toast.LENGTH_LONG).show(); //15
                    Intent intent = new Intent(RincianPenyewaan.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }); //16

//                try {
//                    // stub
//                    final int stubJumlahHariPenyewaan = 1;
//
//                    if ((lamaPenyewaan.equals(durasiPenyewaanKendaraan) && fasilitasSupir == true)) {
//                        total = (stubJumlahHariPenyewaan * hargaSewa) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                    }
//                    else if ((lamaPenyewaan.equals(durasiPenyewaanKendaraan) && fasilitasSupir == false && stubJumlahHariPenyewaan==1)) {
//                        total = (stubJumlahHariPenyewaan * hargaSewa) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                    }
//                    else if ((lamaPenyewaan.equals(durasiPenyewaanKendaraan) && fasilitasSupir == false && stubJumlahHariPenyewaan>1)) {
//                        total = (stubJumlahHariPenyewaan * (hargaSewa*2)) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                    } else {
//                        total = (stubJumlahHariPenyewaan * (hargaSewa)) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "Terdapat Kesalahan pada Sistem", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(RincianPenyewaan.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });





//        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
//                String lamaPenyewaan = kendaraan.getLamaPenyewaan();
//                double hargaSewa = kendaraan.getHargaSewa();
//                String status = "24 Jam";
//
//                if (lamaPenyewaan.equals(status)) {
//                    double total = 0;
//                    try {
//                        total = (getJumlahHariPenyewaan() * hargaSewa) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Terdapat Kesalahan pada Sistem", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(RincianPenyewaan.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//                } else {
//                    double total = 0;
//                    try {
//                        total = (getJumlahHariPenyewaan() * (hargaSewa*2)) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Terdapat Kesalahan pada Sistem", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(RincianPenyewaan.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        //stub
//        final int stubJumlahHariPenyewaan = 1;
//
//        mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
//                String lamaPenyewaan = kendaraan.getLamaPenyewaan();
//                double hargaSewa = kendaraan.getHargaSewa();
//                String status = "24 Jam";
//                if (lamaPenyewaan.equals(status)) {
//                    double total = 0;
//                    total = (stubJumlahHariPenyewaan * hargaSewa) * jmlKendaraan;
//                    totalBiayaPembayaran = total;
//                    textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                } else {
//                    double total = 0;
//                    total = (stubJumlahHariPenyewaan * (hargaSewa*2)) * jmlKendaraan;
//                    totalBiayaPembayaran = total;
//                    textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public int getJumlahHariPenyewaan() throws ParseException {
        jumlahHariPenyewaan = 0;
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");

        if (tglSewaPencarian.equals(tglKembaliPencarian)) {
            jumlahHariPenyewaan = 1;
            textViewLamaSewaPemesanan.setText(String.valueOf(jumlahHariPenyewaan));
        } else {
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = myFormat.parse(tglSewaPencarian);
            Date date2 = myFormat.parse(tglKembaliPencarian);
            long diff = date2.getTime() - date1.getTime();
            int dayCount = (int) diff / (24 * 60 * 60 * 1000);
            jumlahHariPenyewaan = dayCount + 1;
            textViewLamaSewaPemesanan.setText(String.valueOf(jumlahHariPenyewaan));
        }
        return jumlahHariPenyewaan;
    }

    //asli
//    public void totalBiayaSewa() throws ParseException {
//        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
//        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
//        final String jumlahKendaraanPencarian = getIntent().getStringExtra("jumlahKendaraanPencarian");
//        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
//        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
//        final int jmlKendaraan = Integer.parseInt(jumlahKendaraanPencarian);
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        if (tglSewaPencarian.equals(tglKembaliPencarian)) {
//            jumlahHariPenyewaan = 1;
//            mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
//                    String lamaPenyewaan = kendaraan.getLamaPenyewaan();
//                    double hargaSewa = kendaraan.getHargaSewa();
//                    String status = "24 Jam";
//                    if (lamaPenyewaan.equals(status)) {
//                        double total = (jumlahHariPenyewaan * hargaSewa) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                        textViewLamaSewaPemesanan.setText(String.valueOf(jumlahHariPenyewaan));
//                    } else {
//                        double total = (jumlahHariPenyewaan * (hargaSewa*2)) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(total));
//                        textViewLamaSewaPemesanan.setText(String.valueOf(jumlahHariPenyewaan));
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            Date date1 = myFormat.parse(tglSewaPencarian);
//            Date date2 = myFormat.parse(tglKembaliPencarian);
//            long diff = date2.getTime() - date1.getTime();
//            int dayCount = (int) diff / (24 * 60 * 60 * 1000);
//            jumlahHariPenyewaan = dayCount;
//            String lamaSewaPelanggan = String.valueOf(dayCount);
//            mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
//                    String lamaPenyewaan = kendaraan.getLamaPenyewaan();
//                    double hargaSewa = kendaraan.getHargaSewa();
//                    String status = "24 Jam";
//                    if (lamaPenyewaan.equals(status)) {
//                        double total = (jumlahHariPenyewaan * hargaSewa) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        String a = String.valueOf(total);
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(totalBiayaPembayaran));
//                        textViewLamaSewaPemesanan.setText(String.valueOf(jumlahHariPenyewaan));
//                    } else {
//                        double total = (jumlahHariPenyewaan * (hargaSewa*2)) * jmlKendaraan;
//                        totalBiayaPembayaran = total;
//                        String a = String.valueOf(total);
//                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(totalBiayaPembayaran));
//                        textViewLamaSewaPemesanan.setText(String.valueOf(jumlahHariPenyewaan));
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
