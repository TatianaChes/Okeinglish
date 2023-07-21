package com.example.myapplicationtest.ui.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.orhanobut.hawk.Hawk;

import java.util.Calendar;

// широковещательное уведомление
public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // запуск сервиса
        Hawk.init(context).build();
        Intent intent1 = new Intent(context, MyService.class);
        context.startService(intent1);

        Intent _intent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //  alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String[] time = Hawk.get("sendingTime").toString().split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            // Wakes up the device in Doze Mode
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), // time in millis
                    pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // Wakes up the device in Idle Mode
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            // Old APIs
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}
