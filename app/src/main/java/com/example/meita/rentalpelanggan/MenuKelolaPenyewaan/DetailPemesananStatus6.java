package com.example.meita.rentalpelanggan.MenuKelolaPenyewaan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.Autentifikasi.PelangganModel;
import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.MenuPembayaran.PembayaranModel;
import com.example.meita.rentalpelanggan.MenuPemesanan.PenyewaanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.KendaraanModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.PetaLokasiPenjemputan;
import com.example.meita.rentalpelanggan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPemesananStatus6 extends AppCompatActivity {

    TextView textViewTipeKendaraan, textViewNamaRental, textViewDenganSupir, textViewTanpaSupir,
            textViewDenganBBM, textViewTanpaBBM, textViewStatusPemesanan, textViewAreaPemakaian, textViewTotalPembayaran, textViewWaktuPenjemputan, textViewWaktuPengambilan,
            textViewWaktuPenjemputanValue, textViewWaktuPengambilanValue, textViewLokasiPenjemputan, textViewLokasiPenjemputanValue,
            textViewNamaPemesan, textViewAlamatPemesan, textViewTelponPemesan, textViewEmailPemesan;
    TextView textViewNamaRekeningPelanggan, textViewNomorRekeningPelanggan, textViewNamaBankPelanggan,
            textViewJumlahTransfer, textViewWaktuTransfer;
    TextView textViewNamaRekeningRental, textViewNomorRekeningRental, textViewNamaBankRental;
    public ImageView checkListDenganSupir, checkListTanpaSupir, checkListDenganBBM, checkListTanpaBBM, icLokasiPenjemputan;
    Button buttonLihatBuktiPembayaran, btnLihatLokasiPenjemputan, btnLihatBuktiPengembalianDana, btnLihatBuktiSisaPembayaran;
    DatabaseReference mDatabase;
    TextView textViewTglSewa, textViewTglKembali, textViewJumlahSewaKendaraan, textViewMobil, textViewMotor, textViewJmlHariPenyewaan;
    TextView textViewNamaRekeningRentalPengembalian, textViewNomorRekeningRentalPengembalian, textViewNamaBankRentalPengembalian, textViewJumlahTransferPengembalian, textViewWaktuTransferPengembalian,
            textViewNamaRekeningPelangganPengembalian, textViewNomorRekeningPelangganPengembalian, textViewNamaBankPelangganPengembalian;
    TextView textViewNamaRekeningPelangganSisaPembayaran,textViewNomorRekeningPelangganSisaPembayaran,
            textViewNamaBankPelangganSisaPembayaran, textViewJumlahTransferSisaPembayaran,textViewWaktuTransferSisaPembayaran,
            textViewNamaRekeningRentalSisaPembayaran, textViewNomorRekeningRentalSisaPembayaran,
            textViewNamaBankRentalSisaPembayaran;
    LinearLayout linearLayoutInfoSisapembayaran;
    TextView textViewAlasanPembatalan;
    View pembatas5, pembatas9, pembatas4;
    LinearLayout linearLayoutInformasiPembayaran, linearLayoutInformasiPengembalianDana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Detail Penyewaan Batal");
        setContentView(R.layout.activity_detail_pemesanan_status6);

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
        textViewAlasanPembatalan = (TextView)findViewById(R.id.textViewAlasanPembatalan);

        checkListDenganSupir = (ImageView)findViewById(R.id.icCheckListDenganSupir);
        checkListTanpaSupir = (ImageView)findViewById(R.id.icCheckListTanpaSupir);
        checkListDenganBBM = (ImageView)findViewById(R.id.icCheckListDenganBBM);
        checkListTanpaBBM = (ImageView)findViewById(R.id.icCheckListTanpaBBM);
        icLokasiPenjemputan = (ImageView)findViewById(R.id.icLokasiPenjemputan);

        textViewTglSewa = (TextView)findViewById(R.id.textViewTglSewa);
        textViewTglKembali = (TextView)findViewById(R.id.textViewTglKembali);
        textViewJumlahSewaKendaraan = (TextView)findViewById(R.id.textViewJumlahSewaKendaraan);
        textViewMobil = (TextView)findViewById(R.id.textViewMobil);
        textViewMotor = (TextView)findViewById(R.id.textViewMotor);
        textViewJmlHariPenyewaan = (TextView)findViewById(R.id.textViewJmlHariPenyewaan);

        textViewNamaRekeningRentalPengembalian = (TextView) findViewById(R.id.textViewNamaRekeningRentalPengembalian);
        textViewNomorRekeningRentalPengembalian = (TextView) findViewById(R.id.textViewNomorRekeningRentalPengembalian);
        textViewNamaBankRentalPengembalian = (TextView) findViewById(R.id.textViewNamaBankRentalPengembalian);
        textViewJumlahTransferPengembalian = (TextView) findViewById(R.id.textViewJumlahTransferPengembalian);
        textViewWaktuTransferPengembalian = (TextView) findViewById(R.id.textViewWaktuTransferPengembalian);
        textViewNamaRekeningPelangganPengembalian = (TextView) findViewById(R.id.textViewNamaRekeningPelangganPengembalian);
        textViewNomorRekeningPelangganPengembalian = (TextView) findViewById(R.id.textViewNomorRekeningPelangganPengembalian);
        textViewNamaBankPelangganPengembalian = (TextView) findViewById(R.id.textViewNamaBankPelangganPengembalian);

        //text view dari pembayaran sisa
        textViewNamaRekeningPelangganSisaPembayaran = (TextView)findViewById(R.id.textViewNamaRekeningPelangganSisaPembayaran);
        textViewNomorRekeningPelangganSisaPembayaran = (TextView)findViewById(R.id.textViewNomorRekeningPelangganSisaPembayaran);
        textViewNamaBankPelangganSisaPembayaran = (TextView)findViewById(R.id.textViewNamaBankPelangganSisaPembayaran);
        textViewJumlahTransferSisaPembayaran = (TextView)findViewById(R.id.textViewJumlahTransferSisaPembayaran);
        textViewWaktuTransferSisaPembayaran = (TextView)findViewById(R.id.textViewWaktuTransferSisaPembayaran);
        textViewNamaRekeningRentalSisaPembayaran = (TextView)findViewById(R.id.textViewNamaRekeningRentalSisaPembayaran);
        textViewNomorRekeningRentalSisaPembayaran = (TextView)findViewById(R.id.textViewNomorRekeningRentalSisaPembayaran);
        textViewNamaBankRentalSisaPembayaran = (TextView)findViewById(R.id.textViewNamaBankRentalSisaPembayaran);
        btnLihatBuktiSisaPembayaran = (Button)findViewById(R.id.btnLihatBuktiSisaPembayaran);
        linearLayoutInfoSisapembayaran = (LinearLayout)findViewById(R.id.linearLayoutInfoSisapembayaran);
        pembatas5 = (View)findViewById(R.id.pembatas5);

        pembatas4 = (View)findViewById(R.id.pembatas4);
        pembatas9 = (View)findViewById(R.id.pembatas9);

        linearLayoutInformasiPembayaran = (LinearLayout)findViewById(R.id.linearLayoutInformasiPembayaran);
        linearLayoutInformasiPengembalianDana = (LinearLayout)findViewById(R.id.linearLayoutInformasiPengembalianDana);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        buttonLihatBuktiPembayaran = (Button)findViewById(R.id.btnLihatBuktiPembayaran);
        btnLihatLokasiPenjemputan = (Button)findViewById(R.id.btnLihatLokasiPenjemputan);
        btnLihatBuktiPengembalianDana = (Button)findViewById(R.id.btnLihatBuktiPengembalianDana);

        buttonLihatBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihatBuktiPembayaran();
            }
        });

        btnLihatBuktiPengembalianDana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatBuktiPengembalianDana();
            }
        });

        btnLihatLokasiPenjemputan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
                Intent intent = new Intent(DetailPemesananStatus6.this, PetaLokasiPenjemputan.class);
                intent.putExtra("idPenyewaan", idPenyewaan);
                intent.putExtra("statusPenyewaan", "batal");
                startActivity(intent);
            }
        });

        btnLihatBuktiSisaPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lihatBuktiSisaPembayaran();
            }
        });

        infoPenyewaan();
        infoPembayaran();
        infoKendaraan();
        infoRentalKendaraan();
        infoPelanggan();
        infoPengembalianDana();
        infoSisaPembayaran();
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

    public void infoPenyewaan() {
        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPemesanan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewStatusPemesanan.setText(dataPemesanan.getstatusPenyewaan());
                        textViewTotalPembayaran.setText("Rp." + BaseActivity.rupiah().format(dataPemesanan.getTotalBiayaPembayaran()));
                        textViewTglSewa.setText(dataPemesanan.getTglSewa());
                        textViewTglKembali.setText(dataPemesanan.getTglKembali());
                        textViewJumlahSewaKendaraan.setText(String.valueOf(dataPemesanan.getJumlahKendaraan()));
                        textViewJmlHariPenyewaan.setText(String.valueOf(dataPemesanan.getJumlahHariPenyewaan()));
                        textViewAlasanPembatalan.setText(dataPemesanan.getAlasanPembatalan());
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
                            btnLihatLokasiPenjemputan.setVisibility(View.GONE);
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

            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).child("pengembalianDana").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPemesanan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewAlasanPembatalan.setText(dataPemesanan.getAlasanPembatalan());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPemesanan = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewAlasanPembatalan.setText(dataPemesanan.getAlasanPembatalan());
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
            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).child("pembayaran").addValueEventListener(new ValueEventListener() {
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
                    } else {
                        linearLayoutInformasiPembayaran.setVisibility(View.GONE);
                        linearLayoutInformasiPengembalianDana.setVisibility(View.GONE);
                        linearLayoutInfoSisapembayaran.setVisibility(View.GONE);
                        pembatas4.setVisibility(View.GONE);
                        pembatas9.setVisibility(View.GONE);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void infoPengembalianDana() {
        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            final String idRental = getIntent().getStringExtra("idRental");
            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).child("pembayaran").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PembayaranModel dataPembayaran = dataSnapshot.getValue(PembayaranModel.class);
                        textViewNamaRekeningPelangganPengembalian.setText(dataPembayaran.getNamaPemilikRekeningPelanggan());
                        textViewNomorRekeningPelangganPengembalian.setText(dataPembayaran.getNomorRekeningPelanggan());
                        textViewNamaBankPelangganPengembalian.setText(dataPembayaran.getBankPelanggan());

                        textViewNamaRekeningPelanggan.setText(dataPembayaran.getNamaPemilikRekeningPelanggan());
                        textViewNomorRekeningPelanggan.setText(dataPembayaran.getNomorRekeningPelanggan());
                        textViewNamaBankPelanggan.setText(dataPembayaran.getBankPelanggan());
                        textViewJumlahTransfer.setText(dataPembayaran.getJumlahTransfer());
                        textViewWaktuTransfer.setText(dataPembayaran.getWaktuPembayaran());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            final String idRental = getIntent().getStringExtra("idRental");
            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).child("pengembalianDana").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PenyewaanModel dataPengembalian = dataSnapshot.getValue(PenyewaanModel.class);
                        textViewNamaRekeningRentalPengembalian.setText(dataPengembalian.getNamaBankRental());
                        textViewNomorRekeningRentalPengembalian.setText(dataPengembalian.getNomorRekeningRental());
                        textViewNamaBankRentalPengembalian.setText(dataPengembalian.getNamaBankRental());
                        textViewJumlahTransferPengembalian.setText(dataPengembalian.getJumlahTransferPengembalian());
                        textViewWaktuTransferPengembalian.setText(dataPengembalian.getWaktuTransferPengembalian());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void lihatBuktiPembayaran() {
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPenyewaan = getIntent().getStringExtra("statusPenyewaan");
        Bundle bundle = new Bundle();
        Intent intent = new Intent(DetailPemesananStatus6.this, GambarBuktiPembayaran.class);
        bundle.putString("idPenyewaan", idPenyewaan);
        bundle.putString("statusPenyewaan", statusPenyewaan);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void lihatBuktiPengembalianDana() {
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        final String statusPenyewaan = getIntent().getStringExtra("statusPenyewaan");
        Bundle bundle = new Bundle();
        Intent intent = new Intent(DetailPemesananStatus6.this, GambarBuktiPembayaranPengembalianDana.class);
        bundle.putString("idPenyewaan", idPenyewaan);
        bundle.putString("statusPenyewaan", "batal");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void infoSisaPembayaran() {
        try {
            final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
            final String idRental = getIntent().getStringExtra("idRental");
            mDatabase.child("penyewaanKendaraan").child("batal").child(idPenyewaan).child("pembayaran").child("sisaPembayaran").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PembayaranModel dataPembayaran = dataSnapshot.getValue(PembayaranModel.class);
                        final String idRekening = dataPembayaran.getIdRekeningRental();
                        textViewNamaRekeningPelangganSisaPembayaran.setText(dataPembayaran.getNamaPemilikRekeningPelanggan());
                        textViewNomorRekeningPelangganSisaPembayaran.setText(dataPembayaran.getNomorRekeningPelanggan());
                        textViewNamaBankPelangganSisaPembayaran.setText(dataPembayaran.getBankPelanggan());
                        textViewJumlahTransferSisaPembayaran.setText(dataPembayaran.getJumlahTransfer());
                        textViewWaktuTransferSisaPembayaran.setText(dataPembayaran.getWaktuPembayaran());
                        btnLihatBuktiSisaPembayaran.setVisibility(View.VISIBLE);
                        mDatabase.child("rentalKendaraan").child(idRental).child("rekeningPembayaran").child(idRekening).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                RentalModel dataRental = dataSnapshot.getValue(RentalModel.class);
                                textViewNamaRekeningRentalSisaPembayaran.setText(dataRental.getNamaPemilikBank());
                                textViewNomorRekeningRentalSisaPembayaran.setText(dataRental.getNomorRekeningBank());
                                textViewNamaBankRentalSisaPembayaran.setText(dataRental.getNamaBank());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        linearLayoutInfoSisapembayaran.setVisibility(View.GONE);
                        pembatas5.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            linearLayoutInfoSisapembayaran.setVisibility(View.GONE);
            pembatas5.setVisibility(View.GONE);
            btnLihatBuktiSisaPembayaran.setVisibility(View.GONE);
        }
    }

    public void lihatBuktiSisaPembayaran() {
        final String idPenyewaan = getIntent().getStringExtra("idPenyewaan");
        Bundle bundle = new Bundle();
        Intent intent = new Intent(DetailPemesananStatus6.this, GambarBuktiSisaPembayaran.class);
        bundle.putString("idPenyewaan", idPenyewaan);
        bundle.putString("statusPenyewaan", "batal");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}