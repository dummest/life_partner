package com.example.life_partner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;



public class calendar extends Fragment {
    MaterialCalendarView mcv;//main calendar
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();//today text color
    CalendarDay selectedDay = null;// selected day
    myDBHelper dbHelper;
    ListView listView;
    SQLiteDatabase db;
    ListViewAdapter adapter;
    String sql;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mcv = view.findViewById(R.id.calendar_view);
        listView = view.findViewById(R.id.listView);

        dbHelper = new myDBHelper(getContext());
        db = dbHelper.getReadableDatabase();
        adapter = new ListViewAdapter(getContext());



        mcv.state().edit().setMinimumDate(CalendarDay.from(2021, 0, 1))
                .setMaximumDate(CalendarDay.from(2100, 11, 31));

        mcv.addDecorators(new SaturdayDecorator(), new SundayDecorator(), oneDayDecorator);
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent;
                if (selectedDay == null || selectedDay != date) {
                    selectedDay = date;
                    sql = "select * from notiTBL where year =" + date.getYear() + " and month = " + date.getMonth() + " and day = " + date.getDay() + ";";

                    Cursor cursor = db.rawQuery(sql, null);
                    while(cursor.moveToNext()){
                        adapter.addItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4),cursor.getInt(5), cursor.getString(6), cursor.getString(7));
                        Log.d("리스트뷰  아이템 추가 id=", Integer.toString(cursor.getInt(0)));
                    }
                    cursor.close();

                    listView.setAdapter((adapter));
                }
                //선택된 날을 한번 더 클릭시
                else {
                    intent = new Intent(widget.getContext(), Schedule_popup.class);
                    intent.putExtra("selected_year", mcv.getSelectedDate().getYear());
                    intent.putExtra("selected_month", mcv.getSelectedDate().getMonth());
                    intent.putExtra("selected_day", mcv.getSelectedDate().getDay());
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}