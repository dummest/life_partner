package com.example.life_partner;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class timer extends Fragment {
    int year, month, day, hour, minute;
    public timer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        TimePicker tp = view.findViewById(R.id.timer_selector);
        tp.setHour(0);
        tp.setMinute(0);
        Button btn = view.findViewById(R.id.timer_btn_save);
        EditText et = view.findViewById(R.id.timer_schedule_description);
        TextView tv = view.findViewById(R.id.timer_show_plan);


        tp.setIs24HourView(true);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Date date = new Date();
                Calendar cal = Calendar.getInstance();

                cal.setTime(date);
                cal.add(Calendar.HOUR, timePicker.getHour());
                cal.add(Calendar.MINUTE, timePicker.getMinute());
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
                String t = "" + year + "년 " + (month + 1) + "월 " + day + "일 " + hour + "시 " + minute + "분";
                tv.setText(t + "에 알람이 울립니다");


            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext().getApplicationContext();
                int notiId = -1;
                myDBHelper dbHelper = new myDBHelper(context);
                dbHelper.insert(year, month, day, hour, minute, et.getText().toString(),"" ,2);

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String sql = "select seq from sqlite_sequence";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    notiId = cursor.getInt(0);
                }

                setAlarm(notiId, year, month, day, hour, minute);

                Log.d(TAG, "notiid = " + notiId);
                Toast.makeText(getContext(), tp.getHour() + "시간 " + tp.getMinute() + "분 뒤에 알람이 울립니다", Toast.LENGTH_LONG).show();
                et.setText("");
                tp.setHour(0);
                tp.setMinute(0);
                tv.setText("");
            }
        });

        return view;

    }
    public void setAlarm(int id, int year, int month, int day, int hour, int minute) {
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(year, month, day, hour, minute,0);

        Context context = getContext().getApplicationContext();
        Intent alarmIntent = new Intent(context,MyReceiver.class);
        alarmIntent.putExtra("notiId", id);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //pendingIntent = 특정 시점 까지 기다리는 인텐트
        PendingIntent sender = PendingIntent.getBroadcast(context, id, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), sender);
    }
}