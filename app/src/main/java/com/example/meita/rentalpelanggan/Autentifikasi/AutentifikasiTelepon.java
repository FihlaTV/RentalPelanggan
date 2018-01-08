package com.example.meita.rentalpelanggan.Autentifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.MainActivity;
import com.example.meita.rentalpelanggan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class AutentifikasiTelepon extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layout_input_kode, layout_input_nomor;
    EditText input_nomor_telepon, input_kode_verifikasi;
    ImageButton btn_edit_nomor;
    Button btn_selanjutnya, btn_submit, btn_kirim_ulang_kode;
    TextView txt_phone_number, txt_countdown;

    private FirebaseAuth mAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;
    private String mVerificationId, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentifikasi_telepon);

        layout_input_kode = (LinearLayout)findViewById(R.id.layout_input_kode);
        layout_input_nomor = (LinearLayout)findViewById(R.id.layout_input_nomor);
        input_nomor_telepon = (EditText)findViewById(R.id.input_nomor_telepon);
        input_kode_verifikasi = (EditText)findViewById(R.id.input_kode_verifikasi);
        btn_edit_nomor = (ImageButton)findViewById(R.id.btn_edit_nomor);
        btn_selanjutnya = (Button)findViewById(R.id.btn_selanjutnya);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_kirim_ulang_kode = (Button)findViewById(R.id.btn_kirim_ulang_kode);
        txt_phone_number = (TextView)findViewById(R.id.txt_phone_number);
        txt_countdown = (TextView)findViewById(R.id.txt_countdown);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        btn_edit_nomor.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_kirim_ulang_kode.setOnClickListener(this);
        btn_selanjutnya.setOnClickListener(this);
        //requestCode();
        layout_input_kode.setVisibility(View.GONE);
    }

    public void requestCode() {
        // phoneNumber = etxtPhone.getText().toString();
        txt_phone_number.setText(phoneNumber);
        if (TextUtils.isEmpty(phoneNumber))
            return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, AutentifikasiTelepon.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(AutentifikasiTelepon.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                txt_countdown.setText("kirim kode dalam :  " + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                txt_countdown.setVisibility(View.GONE);
                                btn_kirim_ulang_kode.setVisibility(View.VISIBLE);
                            }
                        }.start();

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        //Toast.makeText(ActivityPhoneAuth.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AutentifikasiTelepon.this, "berhasil login", Toast.LENGTH_SHORT).show();
                            mDatabase.child("pelanggan").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        PelangganModel dataPelanggan = dataSnapshot.getValue(PelangganModel.class);
                                        if (dataPelanggan != null) {
                                            startActivity(new Intent(AutentifikasiTelepon.this, MainActivity.class));
                                            finish();
                                        } else {
                                            //Toast.makeText(AutentifikasiTeleponActivity.this, "data kosong", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AutentifikasiTelepon.this, InputBiodataPelanggan.class);
                                            intent.putExtra("notelfon_rental", phoneNumber);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(AutentifikasiTelepon.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {
        String code = input_kode_verifikasi.getText().toString();
        if (TextUtils.isEmpty(code))
            return;

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }

    @Override
    public void onClick(View v) {
        if (v == btn_submit) {
            signIn(v);
        } else if (v == btn_kirim_ulang_kode) {
            requestCode();
        } else if (v == btn_edit_nomor) {
            //finish();
            layout_input_kode.setVisibility(View.GONE);
            layout_input_nomor.setVisibility(View.VISIBLE);
        }else if (v == btn_selanjutnya){
            layout_input_kode.setVisibility(View.VISIBLE);
            layout_input_nomor.setVisibility(View.GONE);
            phoneNumber = input_nomor_telepon.getText().toString();
            requestCode();
        }
    }
}
