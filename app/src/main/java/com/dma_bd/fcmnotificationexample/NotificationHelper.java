package com.dma_bd.fcmnotificationexample;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body){

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,MainActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat nMCompat=NotificationManagerCompat.from(context);
        nMCompat.notify(1,mBuilder.build());

    }
}
