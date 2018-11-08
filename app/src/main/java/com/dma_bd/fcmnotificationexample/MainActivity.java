package com.dma_bd.fcmnotificationexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID="Fcm_Example";
    private static final String CHANNEL_NAME="FCM_NOTIFICATION";
    private static final String CHANNEL_DESC="This is the description of FCM Notification";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        textView=(TextView)findViewById(R.id.buttonNotify);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token=task.getResult().getToken();
                            textView.setText(token);
                        }else {
                            textView.setText(task.getException().getMessage());
                        }
                    }
                });

    }

    private void displayNotification(){

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Works..")
                .setContentText("FCM works perfectly!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat nMCompat=NotificationManagerCompat.from(this);
        nMCompat.notify(1,mBuilder.build());

    }
}
