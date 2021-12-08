package com.example.life_partner;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Date date = Calendar.getInstance().getTime();

            myDBHelper dbHelper = new myDBHelper(context.getApplicationContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT notiId, year, month, day, hour, minute from notiTBL " +
                    "where (year >" + date.getYear() +
                    ") or where (year >=" + date.getYear() + " and month>" + date.getMonth() +
                    ") or where (year >=" + date.getYear() + " and month>=" + date.getMonth() + " and day >" + date.getDay() +
                    ") or where (year >=" + date.getYear() + " and month>=" + date.getMonth() + " and day >=" + date.getDay() + " and hour >" + date.getHours() +
                    ") or where (year >=" + date.getYear() + " and month>=" + date.getMonth() + " and day >=" + date.getDay() + " and hour >=" + date.getHours() + " and minute >" + date.getMinutes();
            Cursor cursor = db.rawQuery(sql, null);

            while(cursor.moveToNext()){
                Calendar alarmCalendar = Calendar.getInstance();
                alarmCalendar.set(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
                Intent alarmIntent = new Intent(context,MyReceiver.class);
                alarmIntent.putExtra("notiId", cursor.getInt(0));

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                //pendingIntent = 특정 시점 까지 기다리는 인텐트
                PendingIntent sender = PendingIntent.getBroadcast(context, cursor.getInt(0), alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), sender);
            }
        }
    }
}