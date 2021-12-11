package com.example.life_partner;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LockScreenActivity extends Activity {
    Date date = new Date();
    TextView dateText;
    TextView timeText;
    LinearLayout linearLayout;
    ListView listView;
    ListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        Log.d("스크린락서비스 액티비티", "onCreate: ");
        this.setShowWhenLocked(true);
        this.setShowWhenLocked(true);

        dateText = findViewById(R.id.lock_screen_date_textview);
        timeText = findViewById(R.id.lock_screen_time_textview);
        linearLayout = findViewById(R.id.lock_screen_layout);
        listView = findViewById(R.id.lock_screen_listview);
        adapter = new ListViewAdapter(getApplicationContext());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        dateText.setText(sdf.format(date));
        sdf = new SimpleDateFormat("hh시 mm분");
        timeText.setText(sdf.format(date));


        String sql = "select * from notiTBL where year =" + (date.getYear() + 1900) + " and month = " + date.getMonth() + " and day = " + date.getDate()+ " order by hour, minute;";

        Log.d("쿼리", sql);
        SQLiteDatabase db;
        myDBHelper dbHelper = new myDBHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            adapter.addItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4),cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            Log.d("리스트뷰  아이템 추가 id=", Integer.toString(cursor.getInt(0)));
        }
        cursor.close();
        listView.setAdapter(adapter);
        Log.d(TAG, "listLoad: 완료");

    }
}
