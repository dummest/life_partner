package com.example.life_partner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

        dp.setMinDate(System.currentTimeMillis());
        dp.updateDate(
                getIntent().getIntExtra("selected_year", 2021),
                getIntent().getIntExtra("selected_month", 0),
                getIntent().getIntExtra("selected_day", 1)
        );
        Date currentDate = new Date();

        tp.setHour(currentDate.getHours());
        tp.setMinute(currentDate.getMinutes());


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

                if(cb_checked)
                    setAlarm(cb, hour, minute);
                else
                    setAlarm(year, month, day, hour, minute);
                    dbHelper.insert(year, month, day, hour, minute, title.getText().toString(), des.getText().toString());
                Toast.makeText(getApplicationContext(), "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void setAlarm(int year, int month, int dayOfMonth, int hour, int minute) {
        alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(year, month, dayOfMonth, hour, minute,0);

        Context context = getApplicationContext();
        Intent alarmIntent = new Intent(context,MyReceiver.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //pendingIntent = 어떤 일이 생길때 까지 기다리는 인텐트
        PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);

    }

    public void setAlarm(CheckBox dayOfWeek[], int hour, int minute){
        alarmCalendar = Calendar.getInstance();
    }
}
