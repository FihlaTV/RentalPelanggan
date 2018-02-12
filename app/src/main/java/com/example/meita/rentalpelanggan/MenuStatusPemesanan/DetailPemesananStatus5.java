package com.example.meita.rentalpelanggan.MenuStatusPemesanan;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MenuPembayaran.PembayaranModel;
import com.example.meita.rentalpelanggan.MenuPemesanan.PenyewaanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPemesananStatus5 extends AppCompatActivity {

    TextView textViewTipeKendaraan, textViewNamaRental, textViewDenganSupir, textViewTanpaSupir,
            textViewDenganBBM, textViewTanpaBBM, textViewStatusPemesanan, textViewAreaPemakaian, textViewTotalPembayaran, textViewWaktuPenjemputan, textViewWaktuPengambilan,
            textViewWaktuPenjemputanValue, textViewWaktuPengambilanValue, textViewLokasiPenjemputan, textViewLokasiPenjemputanValue,
            textViewNamaPemesan, textViewAlamatPemesan, textViewTelponPemesan, textViewEmailPemesan;
    TextView textViewNamaRekeningPelanggan, textViewNomorRekeningPelanggan, textViewNamaBankPelanggan,
            textViewJumlahTransfer, textViewWaktuTransfer;
    TextView textViewNamaRekeningRental, textViewNomorRekeningRental, textViewNamaBankRental;
    public ImageView checkListDenganSupir, checkListTanpaSupir, checkListDenganBBM, checkListTanpaBBM, icLokasiPenjemputan;
    Button buttonLihatBuktiPembayaran;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detail Penyewaan Pengajuan Pembatalan");
        setContentView(R.layout.activity_detail_pemesanan_status5);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewStatusPemesanan = (TextView)findViewById(R.id.textViewStatusPemesanan);
        textViewTipeKendaraan = (TextView)findViewById(R.id.textViewTipeKendaraan);
        textViewNamaRental = (TextView)findViewById(R.id.textViewNamaRentalKendaraan);
        textViewAreaPemakaian = (TextView)findViewById(R.id.textViewAreaPemakaian);
        textViewTotalPembayaran = (TextView)findViewById(R.id.textViewTotalPembayaran);
        textViewDenganSupir = (TextView)findViewById(R.id.textViewDenganSupir);
        textViewTanpaSupir = (TextView)findViewById(R.id.textViewTanpaSupir);
        textViewDenganBBM = (TextView)findViewById(R.id.textViewDenganBBM);
        textViewTanpaBBM = (TextView)findViewById(R.id.textViewTanpaBBM);
        textViewWaktuPenjemputan = (TextView)findViewById(R.id.textViewWaktuPenjemputan);
        textViewWaktuPenjemputanValue = (TextView)findViewById(R.id.textViewWaktuPenjemputanValue);
        textViewWaktuPengambilan = (TextView)findViewById(R.id.textViewWaktuPengambilan);
        textViewWaktuPengambilanValue = (TextView)findViewById(R.id.textViewWaktuPengambilanValue);
        textViewLokasiPenjemputan = (TextView)findViewById(R.id.textViewLokasiPenjemputan);
        textViewLokasiPenjemputanValue = (TextView)findViewById(R.id.textViewLokasiPenjemputanValue);
        textViewNamaRekeningPelanggan = (TextView)findViewById(R.id.textViewNamaRekeningPelanggan);
        textViewNomorRekeningPelanggan = (TextView)findViewById(R.id.textViewNomorRekeningPelanggan);
        textViewNamaBankPelanggan = (TextView)findViewById(R.id.textViewNamaBankPelanggan);
        textViewJumlahTransfer = (TextView)findViewById(R.id.textViewJumlahTransfer);
        textViewWaktuTransfer = (TextView)findViewById(R.id.textViewWaktuTransfer);
        textViewNamaRekeningRental = (TextView)findViewById(R.id.textViewNamaRekeningRental);
        textViewNomorRekeningRental = (TextView)findViewById(R.id.textViewNomorRekeningRental);
        textViewNamaBankRental = (TextView)findViewById(R.id.textViewNamaBankRental);
        textViewNamaPemesan = (TextView)findViewById(R.id.textViewNamaPemesan);
        textViewAlamatPemesan = (TextView)findViewById(R.id.textViewAlamatPemesan);
        textViewTelponPemesan = (TextView)findViewById(R.id.textViewTelponPemesan);
        textViewEmailPemesan = (TextView)findViewById(R.id.textViewEmailPemesan);

        checkListDenganSupir = (ImageView)findViewById(R.id.icCheckListDenganSupir);
        checkListTanpaSupir = (ImageView)findViewById(R.id.icCheckListTanpaSupir);
        checkListDenganBBM = (ImageView)findViewById(R.id.icCheckListDenganBBM);
        checkListTanpaBBM = (ImageView)findViewById(R.id.icCheckListTanpaBBM);
        icLokasiPenjemputan = (ImageView)findViewById(R.id.icLokasiPenjemputan);

        buttonLihatBuktiPembayaran = (Button)findViewById(R.id.btnLihatBuktiPembayaran);
        buttonLihatBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(DetailPemesananStatus5.this, GambarBuktiPembayaran.class);
                bundle.putString("idPenyewaan", idPenyewaan);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        infoPemesanan();
        infoPembayaran();
        infoKendaraan();
        infoRentalKendaraan();
        infoPelanggan();
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

    public void infoPelanggan() {
        try {
            final String idPelanggan = getIntent().getStringExtra("idPelanggan");
            mDatabase.child("pelanggan").child(idPelanggan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    PelangganModel dataPelanggan = dataSnapshot.getValue(PelangganModel.class);
                    textViewNamaPemesan.setText(dataPelanggan.getNamaLengkap());
                    textViewAlamatPemesan.setText(dataPelanggan.getAlamat());
                    textViewTelponPemesan.setText(dataPelanggan.getNoTelp());
                    textViewEmailPemesan.setText(dataPelanggan.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void infoPemesanan() {
        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            mDatabase.child("penyewaanKendaraan").child("selesai").child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPemesanan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewStatusPemesanan.setText(dataPemesanan.getstatusPenyewaan());
                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(dataPemesanan.getTotalBiayaPembayaran()));
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
        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            final String idRental = getIntent().getStringExtra("idRental");
            mDatabase.child("penyewaanKendaraan").child("selesai").child(idPenyewaan).child("pembayaran").addValueEventListener(new ValueEventListener() {
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
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }
}

