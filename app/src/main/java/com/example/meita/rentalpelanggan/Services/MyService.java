package com.example.meita.rentalpelanggan.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class MyService extends Service {
    DatabaseReference mDatabase;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        Log.d("destroy", "onDestroy");
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //return super.onStartCommand(intent, flags, startId);
        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final Handler handler=new Handler();
        final Runnable updateTask=new Runnable() {
            @Override
            public void run() {
                updateCurrentTimeToFirebase();
                handler.postDelayed(this,10000);
                handler.removeCallbacks(this);
            }
        };

        handler.postDelayed(updateTask,10000);
    }
    public  void updateCurrentTimeToFirebase() {
        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        String id = mDatabase.push().getKey();
        mDatabase.child("autoUpdate").child(id).child("updateTime").setValue(currentDate);
    }
}
