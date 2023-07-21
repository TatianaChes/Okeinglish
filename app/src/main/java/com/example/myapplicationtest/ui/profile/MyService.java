package com.example.myapplicationtest.ui.profile;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Hawk.init(this).build();
        FirebaseDatabaseHelper.getSoundButton(); // пользовательское разрешение на отправку уведомления
        // проверка текущего времени с установленным
        if (Hawk.get("sendingTime").toString().equals(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()))
                &&  (Hawk.get("sendingMessage").equals(true)) ) {

            @SuppressLint("RemoteViewLayout")
            RemoteViews contentView = new RemoteViews(getPackageName() , R.layout.custom_notification_layout ) ;

                String CHANNEL_ID = "124";
                CharSequence name = this.getResources().getString(R.string.app_name);
                NotificationCompat.Builder mBuilder;
                Intent notificationIntent = new Intent(this, SettingActivity.class);
                Bundle bundle = new Bundle();
                notificationIntent.putExtras(bundle);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(mChannel);
                    mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setLights(Color.RED, 300, 300)
                            .setChannelId(CHANNEL_ID);
                           // .setContentTitle("Уведомление");
                } else {
                    mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setPriority(Notification.PRIORITY_HIGH);
                           // .setContentTitle("Уведомление");
                }

                mBuilder.setContentIntent(contentIntent);
             //   mBuilder.setContentText("Привет! Не забудь позаниматься)");
                mBuilder.setContent(contentView) ;
                mBuilder.setAutoCancel(true);
                mNotificationManager.notify(1, mBuilder.build());

        }
    }

}
