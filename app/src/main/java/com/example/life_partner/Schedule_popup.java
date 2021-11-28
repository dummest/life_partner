package com.example.life_partner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;


public class Schedule_popup extends Activity {
    EditText title, des;
    TimePicker tp;
    DatePicker dp;
    Switch swch;
    CheckBox[] cb = new CheckBox[7];
    TableRow tr;
    Button save;
    Calendar alarmCalendar;
    myDBHelper dbHelper;
    int notiId = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.schedule_add);
        title = findViewById(R.id.edt_title);
        tp = findViewById(R.id.time_selector);
        dp = findViewById(R.id.date_selector);
        swch = findViewById(R.id.set_style_switch);
        des = findViewById(R.id.schedule_description);
        tr = findViewById(R.id.cb_table);
        cb[0] = findViewById(R.id.sundayBox);
        cb[1] = findViewById(R.id.mondayBox);
        cb[2] = findViewById(R.id.tuesdayBox);
        cb[3] = findViewById(R.id.wednesdayBox);
        cb[4] = findViewById(R.id.thursdayBox);
        cb[5] = findViewById(R.id.fridayBox);
        cb[6] = findViewById(R.id.saturdayBox);
        save = findViewById(R.id.schedule_btn_save);
        dbHelper = new myDBHelper(getApplicationContext());

        notiId = getIntent().getIntExtra("notiId", 0);
        dp.setMinDate(System.currentTimeMillis());
        dp.updateDate(
                getIntent().getIntExtra("selected_year", 2021),
                getIntent().getIntExtra("selected_month", 0),
                getIntent().getIntExtra("selected_day", 1)
        );
        Date currentDate = new Date();

        tp.setHour(getIntent().getIntExtra("selected_hour", currentDate.getHours()));
        tp.setMinute(getIntent().getIntExtra("selected_minute", currentDate.getMinutes()));
        String t = getIntent().getStringExtra("selected_title");
        if (t != null)
            title.setText(t);
        String d = getIntent().getStringExtra("selected_description");
        if (d != null)
            des.setText(d);


        swch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (swch.isChecked()) {
                    tr.setVisibility(View.VISIBLE);
                    dp.setVisibility(View.GONE);
                }
                else{
                    tr.setVisibility(View.GONE);
                    dp.setVisibility(View.VISIBLE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            boolean cb_checked = false;
            @Override
            public void onClick(View view) {
                if (swch.isChecked()){
                    for(CheckBox c : cb){
                        if (c.isChecked())
                            cb_checked = true;
                    }
                    if(!cb_checked){
                        Toast.makeText(getApplicationContext(), "set your day of the week", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Date selectedDate = new Date(dp.getYear(),dp.getMonth(),dp.getDayOfMonth(),tp.getHour(),tp.getMinute());

                if(selectedDate.before(currentDate)){
                    Toast.makeText(getApplicationContext(), "you can't plan before than now", Toast.LENGTH_LONG).show();

                }

                int year, month, day, hour, minute;
                year = dp.getYear();
                month = dp.getMonth();
                day  = dp.getDayOfMonth();
                hour = tp.getHour();
                minute = tp.getMinute();

                //노티id가 초기값(0)일 때 즉 새로 만들 때
                if(notiId == 0) {
                    if (cb_checked)
                        setAlarm(cb, hour, minute);
                    else {
                        dbHelper.insert(year, month, day, hour, minute, title.getText().toString(), des.getText().toString());

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String sql = "select seq from sqlite_sequence";
                        Cursor cursor = db.rawQuery(sql, null);
                        while (cursor.moveToNext()){
                            notiId = cursor.getInt(0);
                        }
                        setAlarm(notiId, year, month, day, hour, minute);
                    }
                }
                //노티id가 있을 떄 즉 편집할 때
                else{
                    dbHelper.update(notiId, year, month, day, hour, minute, title.getText().toString(), des.getText().toString());
                    setAlarm(notiId, year, month, day, hour, minute);
                }

                Toast.makeText(getApplicationContext(), "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void setAlarm(int id, int year, int month, int day, int hour, int minute) {
        alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(year, month, day, hour, minute,0);

        Context context = getApplicationContext();
        Intent alarmIntent = new Intent(context,MyReceiver.class);
        alarmIntent.putExtra("notiId", id);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //pendingIntent = 어떤 일이 생길때 까지 기다리는 인텐트
        PendingIntent sender = PendingIntent.getBroadcast(context, id, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), sender);

    }

    public void setAlarm(CheckBox dayOfWeek[], int hour, int minute){
        alarmCalendar = Calendar.getInstance();
    }

    public void stopAlarm(int id){
        Context context = getApplicationContext();
        Intent alarmIntent = new Intent(context,MyReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        PendingIntent sender = PendingIntent.getBroadcast(context, id, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(sender);
    }
}
