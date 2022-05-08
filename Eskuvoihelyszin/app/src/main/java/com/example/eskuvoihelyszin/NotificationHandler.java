package com.example.eskuvoihelyszin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "eskuvo_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager;
    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Eskuvo értesítés", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.BLUE);
        channel.setDescription("Esküvői Helyszín App.");
        this.notificationManager.createNotificationChannel(channel);
    }

    public void send(String msg){
        Intent intent = new Intent(context, FoglalasByUserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle("Esküvői Helyszín").setContentText(msg).setSmallIcon(R.mipmap.ic_launcher_round).setContentIntent(pendingIntent);

        this.notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.notificationManager.cancel(NOTIFICATION_ID);
    }
}
