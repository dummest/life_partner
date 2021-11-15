package com.example.life_partner;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;


public class calendar extends Fragment {
    Calendar alarmCalendar;
    MaterialCalendarView mcv;//main calendar
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();//today text color
    CalendarDay selectedDay = null;// selected day
    String des;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        mcv = view.findViewById(R.id.calendar_view);

        mcv.state().edit().setMinimumDate(CalendarDay.from(2021, 0, 1))
                .setMaximumDate(CalendarDay.from(2100, 11, 31));

        mcv.addDecorators(new SaturdayDecorator(), new SundayDecorator(), oneDayDecorator);



        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent;
                if(selectedDay == null || selectedDay != date) {
                    selectedDay = date;
                }
                //선택된 날을 한번 더 클릭시
                else {
                    intent = new Intent(widget.getContext(), Schedule_popup.class);
                    intent.putExtra("selected_year", mcv.getSelectedDate().getYear());
                    intent.putExtra("selected_month", mcv.getSelectedDate().getMonth());
                    intent.putExtra("selected_day", mcv.getSelectedDate().getDay());
                    startActivityForResult(intent, 1);
                }
            }

        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //************* temporary output by using Toast ******************
            //************* should be use Database **************************
            String result = "";
            int year, month, day, hour, minute;
            boolean[] week = { data.getBooleanExtra("week_of_day_checked", false),
                    data.getBooleanExtra("sunday_is_checked", false),
                    data.getBooleanExtra("monday_is_checked", false),
                    data.getBooleanExtra("tuesday_is_checked", false),
                    data.getBooleanExtra("wednesday_is_checked", false),
                    data.getBooleanExtra("thursday_is_checked", false),
                    data.getBooleanExtra("friday_is_checked", false),
                    data.getBooleanExtra("saturday_is_checked", false)};
                    //week[0]은 요일별 알람인지 여부

            year = data.getIntExtra("saved_year", 2000);
            month = data.getIntExtra("saved_month", 0);
            day = data.getIntExtra("saved_day", 1);
            hour = data.getIntExtra("saved_hour", 1);
            minute = data.getIntExtra("saved_minute", 1);


            if(!week[0]) {//요일별 알람이 아니라면
                result += Integer.toString(year) + "년 ";
                result += Integer.toString(month+1) + "월 ";
                result += Integer.toString(day) + "일 ";
                setAlarm(year, month, day, hour, minute);
                //년월일시분 으로 알람 세팅
            }
            else {
                if(data.getBooleanExtra("sunday_is_checked", false)) result += "일요일 ";
                if(data.getBooleanExtra("monday_is_checked", false)) result += "월요일 ";
                if(data.getBooleanExtra("tuesday_is_checked", false)) result += "화요일 ";
                if(data.getBooleanExtra("wednesday_is_checked", false)) result += "수요일 ";
                if(data.getBooleanExtra("thursday_is_checked", false)) result += "목요일 ";
                if(data.getBooleanExtra("friday_is_checked", false)) result += "금요일 ";
                if(data.getBooleanExtra("saturday_is_checked", false)) result += "토요일 ";
                setAlarm(week, hour, minute);
                //요일시분 으로 알람 세팅
            }
            result += Integer.toString(hour) + "시 ";
            result += Integer.toString(minute) + "분에 알람이 울립니다.";
            des = data.getStringExtra("alarm_description");

            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

        }
    }

    public void setAlarm(int year, int month, int dayOfMonth, int hour, int minute) {
        alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(year, month, dayOfMonth, hour, minute,0);

        Context context = this.getContext();
        Intent alarmIntent = new Intent(context,MyReceiver.class);
        alarmIntent.putExtra("description", des);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //pendingIntent = 어떤 일이 생길때 까지 기다리는 인텐트
        PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //버전별로 움직임, 좋아보여서 넣음
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //API 19 이상 API 23미만
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
            } else {
                //API 19미만
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
            }
        } else {
            //API 23 이상
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
        }
    }

    public void setAlarm(boolean dayOfWeek[], int hour, int minute){
        alarmCalendar = Calendar.getInstance();

    }
}