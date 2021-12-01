package com.example.life_partner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

import androidx.annotation.Nullable;


public class myDBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "notiDB.db";

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notiTBL (notiId INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER, title TEXT DEFAULT '일정', description TEXT DEFAULT '', alarmtype INTEGER);");
        Log.d("dev", "myDBHelper onCreate 호출");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS notiTBL;");
        onCreate(db);
    }

    public myDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void insert(int year, int month, int day, int hour, int minute, String title, String description, int alarmtype) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO notiTBL (year, month, day, hour, minute, title, description, alarmtype) VALUES(" + year + "," + month + "," + day + "," + hour + "," + minute + ",'" + title + "','" + description + "'," + alarmtype + ");");
        db.close();
    }

    public void update(int id, int year, int month, int day, int hour, int minute, String title, String description, int alarmtype) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update notiTBL set year =" + year + ", month =" + month + ", day =" + day + ", hour =" + hour + ", minute =" + minute + ", title = '" + title + "', description = '" + description + "', alarmtype = " + alarmtype + " where notiId =" + id);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM notiTBL WHERE notiId = " + id +";");
        db.close();
    }

    public void getDs(int year, int month, int day){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notiTBL WHERE YEAR = " + year + ", AND MONTH = " + month + ", AND DAY = "+ day + ";  ", null);
        while (cursor.moveToNext()){

        }
        db.close();
    }

}
