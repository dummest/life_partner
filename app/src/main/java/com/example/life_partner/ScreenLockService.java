package com.example.life_partner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ScreenLockService extends Service {
    ScreenLockReceiver receiver;

    public ScreenLockService() {
        Log.d("스크락서비스", "constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ScreenLockReceiver();
        Log.d("스크린락서비스 onCreate", "first");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("스크린락서비스", "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        try {
            if (intent.getStringExtra("command").equals("start")) {
                String alarmId = "life_partner_screen_lock";
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel(alarmId, "잠금화면", NotificationManager.IMPORTANCE_DEFAULT);
                nm.createNotificationChannel(channel);

                Intent toMain = new Intent(this, settingFragment.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, toMain, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, alarmId)
                        .setSmallIcon(R.drawable.ic_launcher_handshake_foreground)
                        .setContentTitle("잠금화면 서비스")
                        .setContentText("잠금화면 서비스 동작중")
                        .setContentIntent(pendingIntent);

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

                registerReceiver(receiver, intentFilter);
                startForeground(-2, builder.build());
                return START_STICKY;
            }
            else {
                try {
                    stopForeground(true);
                    unregisterReceiver(receiver);
                } catch (IllegalArgumentException iae) { }
                return START_NOT_STICKY;
            }
        }
        catch (NullPointerException ne){
            Log.d("널참조", "onScreenLockService ");
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("ScreenLockService", "onDestroy");
        super.onDestroy();
    }
}