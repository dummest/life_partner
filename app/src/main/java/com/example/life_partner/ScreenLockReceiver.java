package com.example.life_partner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenLockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("스크린락서비스 intent.getAction", intent.getAction());
        //if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
        Intent in = new Intent(context, LockScreenActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(in);
        Log.d("스크린락서비스", "리시버 액티비티 시작");
        // }
    }
}