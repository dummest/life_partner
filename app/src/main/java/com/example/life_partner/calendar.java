package com.example.life_partner;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class calendar extends Fragment {

    MaterialCalendarView mcv;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        mcv = view.findViewById(R.id.calendar_view);

        mcv.state().edit().setMinimumDate(CalendarDay.from(2021, 0, 1))
                .setMaximumDate(CalendarDay.from(2100, 11, 31));

        mcv.addDecorators(new SaturdayDecorator(), new SundayDecorator(), oneDayDecorator);

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(widget.getContext(), Schedule_popup.class);
                intent.putExtra("selected_year", mcv.getSelectedDate().getYear());
                intent.putExtra("selected_month", mcv.getSelectedDate().getMonth());
                intent.putExtra("selected_day", mcv.getSelectedDate().getDay());
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1){
                //************* temporary output by using Toast ******************
                //************* should be use Database ***************************
                int year, month, day, hour, minute;
                String result = "";
                if(data.getBooleanExtra("week_of_day_checked", false)){
                    if (data.getBooleanExtra("sunday_is_checked", false))result += "일요일 ";
                    if (data.getBooleanExtra("monday_is_checked", false))result += "월요일 ";
                    if (data.getBooleanExtra("tuesday_is_checked", false))result += "화요일 ";
                    if (data.getBooleanExtra("wednesday_is_checked", false))result += "수요일 ";
                    if (data.getBooleanExtra("thursday_is_checked", false))result += "목요일 ";
                    if (data.getBooleanExtra("friday_is_checked", false))result += "금요일 ";
                    if (data.getBooleanExtra("saturday_is_checked", false))result += "토요일 ";
                    result += "마다";
                }
                else{
                    year = data.getIntExtra("saved_year", 2000);
                    month = data.getIntExtra("saved_month", 0);
                    day = data.getIntExtra("saved_day", 1);
                    result += Integer.toString(year) + "년 ";
                    result += Integer.toString(month) + "월 ";
                    result += Integer.toString(day) + "일 ";
                }
                hour = data.getIntExtra("saved_hour", 1);
                minute = data.getIntExtra("saved_minute", 1);
                result += Integer.toString(hour) + "시 ";
                result += Integer.toString(minute) + "분에 알람이 울립니다.";

                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            }
    }
}