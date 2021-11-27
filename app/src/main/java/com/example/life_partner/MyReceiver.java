package com.example.life_partner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private static PowerManager.WakeLock sCpuWakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent in = new Intent(context, AlarmService.class );
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(in);
        Log.d("receiver", "리시버 호출");
    }
}