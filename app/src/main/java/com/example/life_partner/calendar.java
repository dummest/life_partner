package com.example.life_partner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;


import com.example.life_partner.SaturdayDecorator;
import com.example.life_partner.SundayDecorator;
import com.google.android.material.datepicker.MaterialCalendar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
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
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });
        return view;
    }



}