package com.example.meita.rentalpelanggan.MenuPembayaran;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.Base.BaseActivity;
import com.example.meita.rentalpelanggan.Constants;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPemesanan.PemesananModel;
import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.R;
import com.example.meita.rentalpelanggan.Utils.ShowAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class UnggahBuktiPembayaran extends AppCompatActivity {
    TextView textViewNamaPemilikRekening, textViewNomorRekening, textViewTotalPembayaran, textViewTanggalPemesanan, textViewWaktuBatasTransfer;
    ImageView imageBuktiPembayaran;
    Button buttonCariGambar, buttonUnggahBuktiPembayaran;
    EditText editTextNamaBank, editTextNamaPemilikRekeningPelanggan, editTextNomorRekeningPelanggan, editTextJumlahTransfer;
    String idPelanggan;
    DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private Uri imgUri;
    private FirebaseAuth auth;
    ProgressBar progressBar;
    public static final int PICK_IMAGE_REQUEST = 234;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Unggah Bukti Pembayaran");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unggah_bukti_pembayaran);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        textViewNamaPemilikRekening = (TextView)findViewById(R.id.textViewNamaPemilik);
        textViewNomorRekening = (TextView)findViewById(R.id.textViewNomorRekening);
        textViewTotalPembayaran = (TextView)findViewById(R.id.txtViewTotalPembayaran);
        textViewWaktuBatasTransfer = (TextView)findViewById(R.id.textViewJamBatasTransfer);
        editTextNamaBank = (EditText) findViewById(R.id.editTextBankPelanggan);
        editTextNamaPemilikRekeningPelanggan = (EditText)findViewById(R.id.editTextNamaPemilikRekeningPelanggan);
        editTextNomorRekeningPelanggan = (EditText)findViewById(R.id.ediTextNomorRekeningPelanggan);
        editTextJumlahTransfer = (EditText)findViewById(R.id.editTextJumlahTransfer);
        imageBuktiPembayaran = (ImageView)findViewById(R.id.imageViewBuktiPembayaran);
        buttonCariGambar = (Button)findViewById(R.id.btn_cari);
        buttonUnggahBuktiPembayaran = (Button)findViewById(R.id.buttonUnggahBuktiPembayaran);

        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FEBD3D"), PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(UnggahBuktiPembayaran.this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        idPelanggan = user.getUid();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        infoPembayaran();

        buttonCariGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonUnggahBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekKolomIsian()) {
                    unggahBuktiPembayaranPelanggan();
                }
            }
        });
    }

    public void infoPembayaran() {
        progressBar.setVisibility(View.GONE);
        final String idRental = getIntent().getStringExtra("idRental");
        final String idRekening = getIntent().getStringExtra("idRekening");
        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
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

        try {
            mDatabase.child("pemesananKendaraan").child("belumBayar").child(idPemesanan).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PemesananModel dataPemesanan = dataSnapshot.getValue(PemesananModel.class);
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

    public void unggahBuktiPembayaranPelanggan() {
        progressDialog.setMessage("Harap tunggu..."); // Setting Message
        progressDialog.setTitle("Menyimpan Bukti Pembayaran"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        final String idRekening = getIntent().getStringExtra("idRekening");
        final String statusPemesanan2 = "Menunggu Konfirmasi Rental";
        final String namaBankPelanggan = editTextNamaBank.getText().toString();
        final String namaPemilikRekeningPelanggan = editTextNamaPemilikRekeningPelanggan.getText().toString();
        final String nomorRekeningPelanggan = editTextNomorRekeningPelanggan.getText().toString();
        final String jumlahTransfer = editTextJumlahTransfer.getText().toString();
        if (imgUri != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Menyimpan Bukti Pembayaran");
//            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = mStorageRef.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(imgUri));

            //adding the file to reference
            sRef.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
//                            progressDialog.dismiss();
                            String idPembayaran = mDatabase.push().getKey();
                            String waktuPembayaran = DateFormat.getDateTimeInstance().format(new Date());
                            final PembayaranModel dataPembayaran = new PembayaranModel(idPembayaran, idRekening, taskSnapshot.getDownloadUrl().toString(),
                                    namaBankPelanggan, namaPemilikRekeningPelanggan, nomorRekeningPelanggan, jumlahTransfer, waktuPembayaran);

                            mDatabase.child("pemesananKendaraan").child("belumBayar").child(idPemesanan).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mDatabase.child("pemesananKendaraan").child("menungguKonfirmasiRental").child(idPemesanan).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            mDatabase.child("pemesananKendaraan").child("menungguKonfirmasiRental").child(idPemesanan).child("pembayaran").setValue(dataPembayaran);
                                            mDatabase.child("pemesananKendaraan").child("menungguKonfirmasiRental").child(idPemesanan).child("statusPemesanan").setValue(statusPemesanan2);
                                            mDatabase.child("pemesananKendaraan").child("menungguKonfirmasiRental").child(idPemesanan).child("idRekeningRental").setValue(idRekening);
                                            mDatabase.child("pemesananKendaraan").child("belumBayar").child(idPemesanan).removeValue();
                                            Toast.makeText(getApplicationContext(), "Bukti Pembayaran Anda Berhasil Disimpan", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(UnggahBuktiPembayaran.this, MainActivity.class);
                                            intent.putExtra("halamanStatusMenungguKonfirmasi", 1);
                                            startActivity(intent);
                                            buatPemberitahuan();
                                        }
                                    });

                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } else {
            //display an error if no file is selected
            Toast.makeText(getApplicationContext(), "Silahkan Pilih Foto Bukti Pembayaran Anda", Toast.LENGTH_SHORT).show();
        }

    }

    private void buatPemberitahuan() {
        String idPemberitahuan = mDatabase.push().getKey();
        final String idRental = getIntent().getStringExtra("idRental");
        final String idKendaraan = getIntent().getStringExtra("idKendaraan");
        final String tglSewaPencarian = getIntent().getStringExtra("tglSewaPencarian");
        final String tglKembaliPencarian = getIntent().getStringExtra("tglKembaliPencarian");
        final String idPemesanan = getIntent().getStringExtra("idPemesanan");
        String valueHalaman = "menungguKonfirmasiRental";
        //String valueHalaman = "0";
        // String valueHalaman = "h";
        //int valueHalaman = 1;
        String statusPemesanan1 = "Menunggu Konfirmasi Rental";
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
        mDatabase.child("pemberitahuan").child("rental").child("menungguKonfirmasiRental").child(idRental).child(idPemberitahuan).setValue(dataNotif);
    }

    public boolean cekKolomIsian() {
        boolean sukses = true;
        if ( editTextNamaBank.getText().toString() == null || editTextNamaPemilikRekeningPelanggan.getText().toString() == null || editTextNomorRekeningPelanggan.getText().toString() == null ||
                editTextJumlahTransfer.getText().toString() == null){
            sukses = false;
            ShowAlertDialog.showAlert("Lengkapi Seluruh Kolom Isian", this);
        }
        return sukses;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)) {
            imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageBuktiPembayaran.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
        }
        return super.onOptionsItemSelected(item);
    }

}
