package com.example.meita.rentalpelanggan.MenuPembayaran;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPemesanan.PenyewaanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPembayaran extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView textViewNamaPemilikRekening, textViewNomorRekening, textViewTipeKendaraan, textViewNamaRental, textViewDenganSupir, textViewTanpaSupir,
            textViewDenganBBM, textViewTanpaBBM, textViewAreaPemakaian, textViewTotalPembayaran, textViewWaktuBatasTransfer;
    ImageView imageChecklistSupirTrue, imageCheckListSupirFalse, imageCheckListBBMTrue, imageCheckListBBMFalse;
    boolean valueSupir;
    Button buttonUnggahSekarang, buttonUnggahNanti;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detail Pembayaran");
        setContentView(R.layout.activity_detail_pembayaran);

        textViewNamaPemilikRekening = (TextView)findViewById(R.id.textViewNamaPemilik);
        textViewNomorRekening = (TextView)findViewById(R.id.textViewNomorRekening);
        textViewTipeKendaraan = (TextView)findViewById(R.id.textViewTipeKendaraan);
        textViewNamaRental = (TextView)findViewById(R.id.textViewRentalKendaraan);
        textViewDenganSupir = (TextView)findViewById(R.id.txtViewSupir);
        textViewTanpaSupir = (TextView)findViewById(R.id.txtViewSupirFalse);
        textViewDenganBBM = (TextView)findViewById(R.id.txtViewBBMTrue);
        textViewTanpaBBM = (TextView)findViewById(R.id.txtViewBBMFalse);
        textViewAreaPemakaian = (TextView)findViewById(R.id.txtViewAreaPemakaian);
        textViewTotalPembayaran = (TextView)findViewById(R.id.txtViewTotalPembayaran);
        textViewWaktuBatasTransfer = (TextView)findViewById(R.id.textViewJamBatasTransfer);

        imageChecklistSupirTrue = (ImageView)findViewById(R.id.icCheckListDenganSupir);
        imageCheckListSupirFalse = (ImageView)findViewById(R.id.icCheckListTanpaSupir);
        imageCheckListBBMTrue = (ImageView)findViewById(R.id.icCheckListDenganBBM);
        imageCheckListBBMFalse = (ImageView)findViewById(R.id.icCheckListTanpaBBM);

        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final String idRental = getIntent().getStringExtra("idRental");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String idRekening = getIntent().getStringExtra("idRekening");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        final String tglSewa = getIntent().getStringExtra("tglSewa");
        final String tglKembali = getIntent().getStringExtra("tglKembali");

        buttonUnggahSekarang = (Button)findViewById(R.id.buttonUnggahBuktiSekarang);
        buttonUnggahNanti = (Button)findViewById(R.id.buttonUnggahBuktiNanti);

        buttonUnggahSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(DetailPembayaran.this, UnggahBuktiPembayaran.class);
                bundle.putString("idRekening", idRekening);
                bundle.putString("idRental", idRental);
                bundle.putString("idKendaraan", idKendaraan);
                bundle.putString("idPenyewaan", idPenyewaan);
                bundle.putString("kategoriKendaraan", kategoriKendaraan);
                bundle.putString("tglSewa", tglSewa);
                bundle.putString("tglKembali", tglKembali);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        buttonUnggahNanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPembayaran.this, MainActivity.class);
                intent.putExtra("halamanStatusBelumBayar", 0);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        infoRental();
        infoRekeningPembayaran();
        infoKendaraan();
        infoPemesanan();

    }

    public void infoRental() {
        progressBar.setVisibility(View.GONE);
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

    public void infoRekeningPembayaran() {
        final String idRental = getIntent().getStringExtra("idRental");
        final String idRekening = getIntent().getStringExtra("idRekening");
        mDatabase.child("rentalKendaraan").child(idRental).child("rekeningPembayaran").child(idRekening).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                textViewNamaPemilikRekening.setText(dataRental.getNamaPemilikBank());
                textViewNomorRekening.setText(dataRental.getNomorRekeningBank());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void infoKendaraan() {
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String kategoriKendaraan = getIntent().getStringExtra("kategoriKendaraan");
        try {
            mDatabase.child("kendaraan").child(kategoriKendaraan).child(idKendaraan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        KendaraanModel kendaraan = dataSnapshot.getValue(KendaraanModel.class);
                        if (kendaraan != null) {
                            textViewTipeKendaraan.setText(kendaraan.getTipeKendaraan());
                            textViewAreaPemakaian.setText(kendaraan.getAreaPemakaian());

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
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

    }

    public void infoPemesanan() {
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        try {
            mDatabase.child("penyewaanKendaraan").child("belumBayar").child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPemesanan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewWaktuBatasTransfer.setText(dataPemesanan.getBatasWaktuPembayaran());
                        textViewTotalPembayaran.setText("Rp."+ BaseActivity.rupiah().format(dataPemesanan.getTotalBiayaPembayaran()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);
    }
}
