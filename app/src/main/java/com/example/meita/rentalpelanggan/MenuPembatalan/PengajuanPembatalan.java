package com.example.meita.rentalpelanggan.MenuPembatalan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPembayaran.PembayaranModel;
import com.example.meita.rentalpelanggan.MenuPemesanan.PenyewaanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PengajuanPembatalan extends AppCompatActivity {
    TextView textViewTipeKendaraan, textViewNamaRental, textViewDenganSupir, textViewTanpaSupir,
            textViewDenganBBM, textViewTanpaBBM,textViewNamaPemesan, textViewAlamatPemesan, textViewTelponPemesan, textViewEmailPemesan,textViewTotalPembayaran,
            textViewWaktuPenjemputanValue, textViewWaktuPengambilanValue, textViewLokasiPenjemputan, textViewLokasiPenjemputanValue;
    ImageView checkListDenganSupir, checkListTanpaSupir, checkListDenganBBM, checkListTanpaBBM, icLokasiPenjemputan;
    TextView textViewTglSewa, textViewTglKembali, textViewJumlahSewaKendaraan, textViewMobil, textViewMotor, textViewJmlHariPenyewaan, textViewAreaPemakaian,
            textViewWaktuPenjemputan, textViewWaktuPengambilan;
    TextView textViewNamaRekeningPelanggan, textViewNomorRekeningPelanggan, textViewNamaBankPelanggan,
            textViewJumlahTransfer, textViewWaktuTransfer;
    TextView textViewNamaRekeningRental, textViewNomorRekeningRental, textViewNamaBankRental;
    Button buttonAjukanPembatalan;
    EditText editTextAlasanPembatalan;
    DatabaseReference mDatabase;
    String idPelanggan;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pengajuan Pembatalan");
        setContentView(R.layout.activity_pengajuan_pembatalan);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewTipeKendaraan = (TextView)findViewById(R.id.textViewTipeKendaraan);
        textViewNamaRental = (TextView)findViewById(R.id.textViewNamaRentalKendaraan);
        textViewTotalPembayaran = (TextView)findViewById(R.id.textViewTotalPembayaran);
        textViewDenganSupir = (TextView)findViewById(R.id.textViewDenganSupir);
        textViewTanpaSupir = (TextView)findViewById(R.id.textViewTanpaSupir);
        textViewDenganBBM = (TextView)findViewById(R.id.textViewDenganBBM);
        textViewTanpaBBM = (TextView)findViewById(R.id.textViewTanpaBBM);

        textViewTglSewa = (TextView)findViewById(R.id.textViewTglSewa);
        textViewTglKembali = (TextView)findViewById(R.id.textViewTglKembali);
        textViewJumlahSewaKendaraan = (TextView)findViewById(R.id.textViewJumlahSewaKendaraan);
        textViewMobil = (TextView)findViewById(R.id.textViewMobil);
        textViewMotor = (TextView)findViewById(R.id.textViewMotor);
        textViewJmlHariPenyewaan = (TextView)findViewById(R.id.textViewJmlHariPenyewaan);

        textViewAreaPemakaian = (TextView)findViewById(R.id.textViewAreaPemakaian);
        textViewWaktuPenjemputan = (TextView)findViewById(R.id.textViewWaktuPenjemputan);
        textViewWaktuPenjemputanValue = (TextView)findViewById(R.id.textViewWaktuPenjemputanValue);
        textViewWaktuPengambilan = (TextView)findViewById(R.id.textViewWaktuPengambilan);
        textViewWaktuPengambilanValue = (TextView)findViewById(R.id.textViewWaktuPengambilanValue);
        textViewLokasiPenjemputan = (TextView)findViewById(R.id.textViewLokasiPenjemputan);
        textViewLokasiPenjemputanValue = (TextView)findViewById(R.id.textViewLokasiPenjemputanValue);

        editTextAlasanPembatalan = (EditText)findViewById(R.id.editTextAlasanPembatalan);

        textViewNamaRekeningPelanggan = (TextView)findViewById(R.id.textViewNamaRekeningPelanggan);
        textViewNomorRekeningPelanggan = (TextView)findViewById(R.id.textViewNomorRekeningPelanggan);
        textViewNamaBankPelanggan = (TextView)findViewById(R.id.textViewNamaBankPelanggan);
        textViewJumlahTransfer = (TextView)findViewById(R.id.textViewJumlahTransfer);
        textViewWaktuTransfer = (TextView)findViewById(R.id.textViewWaktuTransfer);
        textViewNamaRekeningRental = (TextView)findViewById(R.id.textViewNamaRekeningRental);
        textViewNomorRekeningRental = (TextView)findViewById(R.id.textViewNomorRekeningRental);
        textViewNamaBankRental = (TextView)findViewById(R.id.textViewNamaBankRental);

        buttonAjukanPembatalan = (Button)findViewById(R.id.buttonAjukanPembatalan);

        checkListDenganSupir = (ImageView)findViewById(R.id.icCheckListDenganSupir);
        checkListTanpaSupir = (ImageView)findViewById(R.id.icCheckListTanpaSupir);
        checkListDenganBBM = (ImageView)findViewById(R.id.icCheckListDenganBBM);
        checkListTanpaBBM = (ImageView)findViewById(R.id.icCheckListTanpaBBM);
        icLokasiPenjemputan = (ImageView)findViewById(R.id.icLokasiPenjemputan);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        buttonAjukanPembatalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengajuanPembatalan();
            }
        });

        infoKendaraan();
//        infoPelanggan();
        infoPemesanan();
        infoPembayaran();
    }

    public void infoKendaraan() {
        try {
            final String idKendaraan = getIntent().getStringExtra("idKendaraan");
            final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
            mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    KendaraanModel dataKendaraan = dataSnapshot.getValue(KendaraanModel.class);
                    textViewTipeKendaraan.setText(dataKendaraan.getTipeKendaraan());
                    textViewAreaPemakaian.setText(dataKendaraan.getAreaPemakaian());
                    if (dataKendaraan.isSupir() == true ) {
                        textViewDenganSupir.setVisibility(View.VISIBLE);
                        checkListDenganSupir.setVisibility(View.VISIBLE);
                        textViewTanpaSupir.setVisibility(View.GONE);
                        checkListTanpaSupir.setVisibility(View.GONE);
                    } else {
                        textViewDenganSupir.setVisibility(View.GONE);
                        checkListDenganSupir.setVisibility(View.GONE);
                        textViewTanpaSupir.setVisibility(View.VISIBLE);
                        checkListTanpaSupir.setVisibility(View.VISIBLE);
                    }

                    if (dataKendaraan.isBahanBakar() == true ) {
                        textViewDenganBBM.setVisibility(View.VISIBLE);
                        checkListDenganBBM.setVisibility(View.VISIBLE);
                        textViewTanpaBBM.setVisibility(View.GONE);
                        checkListTanpaBBM.setVisibility(View.GONE);
                    } else {
                        textViewDenganBBM.setVisibility(View.GONE);
                        checkListDenganBBM.setVisibility(View.GONE);
                        textViewTanpaBBM.setVisibility(View.VISIBLE);
                        checkListTanpaBBM.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }

    }

//    public void infoPelanggan() {
//        try {
//            final String idPelanggan = getIntent().getStringExtra("idPelanggan");
//            mDatabase.child("pelanggan").child(idPelanggan).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    PelangganModel dataPelanggan = dataSnapshot.getValue(PelangganModel.class);
//                    textViewNamaPemesan.setText(dataPelanggan.getNamaLengkap());
//                    textViewAlamatPemesan.setText(dataPelanggan.getAlamat());
//                    textViewTelponPemesan.setText(dataPelanggan.getNoTelp());
//                    textViewEmailPemesan.setText(dataPelanggan.getEmail());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception e) {
//
//        }
//    }

    public void infoPemesanan() {
        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            mDatabase.child("penyewaanKendaraan").child("berhasil").child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPemesanan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewTotalPembayaran.setText("Rp."+ BaseActivity.rupiah().format(dataPemesanan.getTotalBiayaPembayaran()));
                        textViewTglSewa.setText(dataPemesanan.getTglSewa());
                        textViewTglKembali.setText(dataPemesanan.getTglKembali());
                        textViewJumlahSewaKendaraan.setText(String.valueOf(dataPemesanan.getJumlahKendaraan()));
                        textViewJmlHariPenyewaan.setText(String.valueOf(dataPemesanan.getJumlahHariPenyewaan()));
                        if (dataPemesanan.getKategoriKendaraan().equals("Mobil")) {
                            textViewMobil.setVisibility(View.VISIBLE);
                            textViewMotor.setVisibility(View.GONE);
                        } else {
                            textViewMotor.setVisibility(View.VISIBLE);
                            textViewMobil.setVisibility(View.GONE);
                        }

                        if (dataPemesanan.getJamPenjemputan() == null) {
                            textViewWaktuPenjemputan.setVisibility(View.GONE);
                            textViewWaktuPenjemputanValue.setVisibility(View.GONE);
                            textViewLokasiPenjemputan.setVisibility(View.GONE);
                            textViewLokasiPenjemputanValue.setVisibility(View.GONE);
                            icLokasiPenjemputan.setVisibility(View.GONE);
                            textViewWaktuPengambilanValue.setText(dataPemesanan.getJamPengambilan());
                        } else {
                            textViewWaktuPengambilan.setVisibility(View.GONE);
                            textViewWaktuPengambilanValue.setVisibility(View.GONE);
                            textViewWaktuPenjemputanValue.setText(dataPemesanan.getJamPenjemputan());
                            textViewLokasiPenjemputanValue.setText(dataPemesanan.getAlamatPenjemputan());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }
    }

    public void infoPembayaran() {
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String idRental = getIntent().getStringExtra("idRental");
        try {
            mDatabase.child("penyewaanKendaraan").child("berhasil").child(idPenyewaan).child("pembayaran").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PembayaranModel dataPembayaran = dataSnapshot.getValue(PembayaranModel.class);
                        final String idRekening = dataPembayaran.getIdRekeningRental();
                        textViewNamaRekeningPelanggan.setText(dataPembayaran.getNamaPemilikRekeningPelanggan());
                        textViewNomorRekeningPelanggan.setText(dataPembayaran.getNomorRekeningPelanggan());
                        textViewNamaBankPelanggan.setText(dataPembayaran.getBankPelanggan());
                        textViewJumlahTransfer.setText(dataPembayaran.getJumlahTransfer());
                        textViewWaktuTransfer.setText(dataPembayaran.getWaktuPembayaran());

                        try {
                            mDatabase.child("rentalKendaraan").child(idRental).child("rekeningPembayaran").child(idRekening).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                                    textViewNamaRekeningRental.setText(dataRental.getNamaPemilikBank());
                                    textViewNomorRekeningRental.setText(dataRental.getNomorRekeningBank());
                                    textViewNamaBankRental.setText(dataRental.getNamaBank());
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e) {

                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void infoRentalKendaraan() {
        try {
            final String idRental = getIntent().getStringExtra("idRental");
            mDatabase.child("rentalKendaraan").child(idRental).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                    textViewNamaRental.setText(dataRental.getNama_rental());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void pengajuanPembatalan() {
        final String alasanPembatalan = editTextAlasanPembatalan.getText().toString();
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPemesanan5 = "Pengajuan Pembatalan";
        mDatabase.child("penyewaanKendaraan").child("berhasil").child(idPenyewaan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("penyewaanKendaraan").child("pengajuanPembatalan").child(idPenyewaan).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mDatabase.child("penyewaanKendaraan").child("pengajuanPembatalan").child(idPenyewaan).child("statusPenyewaan").setValue(statusPemesanan5);
                        mDatabase.child("penyewaanKendaraan").child("pengajuanPembatalan").child(idPenyewaan).child("alasanPembatalan").setValue(alasanPembatalan);
                        mDatabase.child("penyewaanKendaraan").child("berhasil").child(idPenyewaan).removeValue();
                        Toast.makeText(getApplicationContext(), "Pengajuan Pembatalan anda berhasil disimpan", Toast.LENGTH_LONG).show();
                        buatPemberitahuan();
                        Intent intent = new Intent(PengajuanPembatalan.this, MainActivity.class);
                        intent.putExtra("halamanStatusPengajuanPembatalan", 3);
                        startActivity(intent);
                    }
                });

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
        final String tglSewa = getIntent().getStringExtra("tglSewa");
        final String tglKembali = getIntent().getStringExtra("tglKembali");
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        String valueHalaman = "pengajuanPembatalan";
        String statusPemesanan1 = "pengajuanPembatalan";
        HashMap<String, Object> dataNotif = new HashMap<>();
        dataNotif.put("idPemberitahuan", idPemberitahuan);
        dataNotif.put("idRental", idRental);
        dataNotif.put("idKendaraan", idKendaraan);
        dataNotif.put("tglSewa", tglSewa);
        dataNotif.put("tglKembalian", tglKembali);
        dataNotif.put("nilaiHalaman", valueHalaman);
        dataNotif.put("statusPenyewaan", statusPemesanan1);
        dataNotif.put("idPelanggan", idPelanggan);
        dataNotif.put("idPenyewaan", idPenyewaan);
        mDatabase.child("pemberitahuan").child("rental").child("pengajuanPembatalan").child(idRental).child(idPemberitahuan).setValue(dataNotif);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
