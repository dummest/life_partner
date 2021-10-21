package com.example.life_partner;

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

    public timer() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        TimePicker tp = view.findViewById(R.id.timer_selector);
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
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                String t = "" + year + "년 " + (month + 1) + "월 " + day + "일 " + hour + "시 " + minute + "분";
                tv.setText(t + "에 알람이 울립니다");


            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), tp.getHour() + "시간 " + tp.getMinute() + "분 뒤에 알람이 울립니다", Toast.LENGTH_LONG);
            }
        });

        return view;

    }
}