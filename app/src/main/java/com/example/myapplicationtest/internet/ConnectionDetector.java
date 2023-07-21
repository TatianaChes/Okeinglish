package com.example.myapplicationtest.internet;

import static com.example.myapplicationtest.ui.home.HomeFragment.dialog;
import static com.example.myapplicationtest.ui.profile.ProfileActivity.dialogProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector extends BroadcastReceiver {
    // проверка наличия интернета
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)) {
                dialog(true); // для главного экрана
                dialogProfile(true); // для профиля
            } else {
                dialog(false);
                dialogProfile(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //следует установить значение null, потому что в режиме полета оно будет равно null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
