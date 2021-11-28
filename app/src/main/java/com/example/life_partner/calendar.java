package com.example.life_partner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;


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
    Intent intent;
    ScrollView scrollView;
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
        scrollView = view.findViewById(R.id.calendar_tab_scroll_view);
        listView = view.findViewById(R.id.listView);
        dbHelper = new myDBHelper(getContext());
        db = dbHelper.getReadableDatabase();
        adapter = new ListViewAdapter(getContext());

        //item 클릭시 인텐트 안고 popup창으로 진입
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(getContext(), Schedule_popup.class);
                intent.putExtra("notiId", adapter.getItem(i).getId());
                intent.putExtra("selected_year", adapter.getItem(i).getYear());
                intent.putExtra("selected_month", adapter.getItem(i).getMonth());
                intent.putExtra("selected_day", adapter.getItem(i).getDay());
                intent.putExtra("selected_hour", adapter.getItem(i).getHour());
                intent.putExtra("selected_minute", adapter.getItem(i).getMinute());
                intent.putExtra("selected_hour", adapter.getItem(i).getHour());
                intent.putExtra("selected_title", adapter.getItem(i).getTitle());
                intent.putExtra("selected_description", adapter.getItem(i).getDescription());
                startActivity(intent);
                //listview갱신
                adapter.notifyDataSetChanged();
                listView.setAdapter((adapter));
            }
        });
        //스크롤뷰 - 리스트뷰 터치간섭 제거
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mcv.state().edit().setMinimumDate(CalendarDay.from(2021, 0, 1))
                .setMaximumDate(CalendarDay.from(2100, 11, 31));
        mcv.addDecorators(new SaturdayDecorator(), new SundayDecorator(), oneDayDecorator);
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selectedDay == null || selectedDay != date) {
                    adapter.list.clear();
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
                    //listview 갱신
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }
        });
        return view;
    }
}