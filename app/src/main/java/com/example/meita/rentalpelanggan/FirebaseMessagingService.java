package com.example.meita.rentalpelanggan;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by AkshayeJH on 13/07/17.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String valueHalaman1, valueHalaman2;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_message = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();

        String idRental = remoteMessage.getData().get("id_pengirim");
        String valueHalaman3 = remoteMessage.getData().get("valueHalaman3");
        String valueHalaman4 = remoteMessage.getData().get("valueHalaman4");
        String valueHalaman7 = remoteMessage.getData().get("valueHalaman7");
        String idPemesanan = remoteMessage.getData().get("idPemesanan");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);


        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("idRental", idRental);
        resultIntent.putExtra("notifBerhasil", valueHalaman3);
        resultIntent.putExtra("notifSelesai", valueHalaman4);
        resultIntent.putExtra("notifMenungguSisaPembayaran", valueHalaman7);
        resultIntent.putExtra("idPemesanan", idPemesanan);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);




        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }
}
