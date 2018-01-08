package com.example.meita.rentalpelanggan.Autentifikasi;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.Constants;
import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.MenuPemesanan.BuatPesanan2_denganSupir;
import com.example.meita.rentalpelanggan.R;
import com.example.meita.rentalpelanggan.Utils.ShowAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class InputBiodataPelanggan extends AppCompatActivity {
    private EditText editTextNoIdentitas, editTextNamaLengkap, editTextAlamat, editTextNomorTelp;
    private Button buttonSimpanData, buttonCariGambar;
    private ProgressBar progressBarSimpan;
    private String userID, emailUser;
    private ImageView imageFotoProfile;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private Uri imgUri;
    private StorageReference mStorageRef;
    ProgressBar progressBar;
    public static final int PICK_IMAGE_REQUEST = 234;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_pengguna);
        setTitle("Biodata Pelanggan");

        editTextNoIdentitas = (EditText) findViewById(R.id.editTextNoIdentitas);
        editTextNamaLengkap = (EditText) findViewById(R.id.editTextNamaLengkap);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextNomorTelp = (EditText) findViewById(R.id.editTextNomorTelp);
        buttonSimpanData = (Button) findViewById(R.id.btn_simpanData);
        buttonCariGambar = (Button) findViewById(R.id.btn_cari);
        progressBar = (ProgressBar) findViewById(R.id.progress_circle);
        imageFotoProfile = (ImageView)findViewById(R.id.imageView);
        progressDialog = new ProgressDialog(InputBiodataPelanggan.this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userID = user.getUid();
        emailUser = user.getEmail();

        buttonCariGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        buttonSimpanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanDataPelanggan();
            }
        });
        progressBar.setVisibility(View.GONE);

    }

    private void simpanDataPelanggan() {
        if (cekKolomIsian() == true) {
            if (imgUri != null) {
                progressDialog.setMessage("Harap tunggu..."); // Setting Message
                progressDialog.setTitle("Menyimpan Biodata Anda"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                //displaying progress dialog while image is uploading
                //final ProgressDialog progressDialog = new ProgressDialog(this);

                //getting the storage reference
                StorageReference sRef = mStorageRef.child(Constants.STORAGE_PATH_FOTO_PROFILE_PELANGGAN+ System.currentTimeMillis() + "." + getFileExtension(imgUri));

                //adding the file to reference
                sRef.putFile(imgUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                String no_ktp_pelanggan = editTextNoIdentitas.getText().toString();
                                String nama_pelanggan = editTextNamaLengkap.getText().toString();
                                String alamat_pelanggan = editTextAlamat.getText().toString();
                                String no_telefon_pelanggan = editTextNomorTelp.getText().toString();


                                String currentDate = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    currentDate = DateFormat.getDateTimeInstance().format(new Date());
                                }
                                PelangganModel dataProfil = new PelangganModel(userID, taskSnapshot.getDownloadUrl().toString(), no_ktp_pelanggan, nama_pelanggan, alamat_pelanggan, emailUser, no_telefon_pelanggan, currentDate,
                                        getToken());
                                mDatabase.child("pelanggan").child(userID).setValue(dataProfil).addOnCompleteListener(InputBiodataPelanggan.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Biodata Anda Gagal Disimpan", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Biodata Anda Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(InputBiodataPelanggan.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                ShowAlertDialog.showAlert("Pilih Foto Profil Anda", this);
            }
        }


        }

    private boolean cekKolomIsian() {
        boolean sukses;
        if (TextUtils.isEmpty(editTextNoIdentitas.getText().toString()) || (TextUtils.isEmpty(editTextNamaLengkap.getText().toString())) ||
                (TextUtils.isEmpty(editTextAlamat.getText().toString())) || (TextUtils.isEmpty(editTextNomorTelp.getText().toString()))   ) {
            ShowAlertDialog.showAlert("Lengkapi Seluruh Kolom Isian", this);
            sukses = false;
        } else {
            sukses = true;
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageFotoProfile.setImageBitmap(bitmap);
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

    private String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }
}


