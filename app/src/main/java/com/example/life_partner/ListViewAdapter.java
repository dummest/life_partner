package com.example.life_partner;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.sql.SQLDataException;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<myData> list  = new ArrayList<myData>();
    public ListViewAdapter(Context c) {
        context = c;
    }

    public void listUpdate(){
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public myData getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup,false);
        }
        myData listdata = list.get(i);
        Log.d(TAG, "getView: i = " + i);

        TextView timeText = view.findViewById(R.id.time_text);
        TextView titleText = view.findViewById(R.id.title_text);
        ImageButton imageButton = view.findViewById(R.id.deleteBtn);

        if(listdata.getMinute() >= 10)
            timeText.setText(Integer.toString(listdata.getHour()) + ":" + Integer.toString(listdata.getMinute()));
        else
            timeText.setText(Integer.toString(listdata.getHour()) + ":0" + Integer.toString(listdata.getMinute()));
        titleText.setText(listdata.getTitle());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.outline_clear_24)
                        .setTitle("일정을 제거합니다")
                        .setMessage(listdata.getTitle() + " 일정을 삭제 하시겠습니까?\n이 작업은 되돌릴 수 없습니다.")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    SQLiteDatabase db;
                                    myDBHelper dbHelper = new myDBHelper(context.getApplicationContext());
                                    int id = listdata.getId();
                                    list.remove(listdata);
                                    dbHelper.delete(id);

                                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent intent = new Intent(context.getApplicationContext(), MyReceiver.class);
                                    PendingIntent sender = PendingIntent.getBroadcast(context.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    if (sender != null) {
                                        am.cancel(sender);
                                        sender.cancel();
                                    }
                                    listUpdate();
                                    Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e) {
                                    Toast.makeText(context, "이미 삭제된 일정입니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null);

                        builder.create().show();
            };
        });
        return view;
    }

    public void addItem(int id, int year, int month, int day, int hour, int minute, String title, String description, int alarmtype) {
        myData data = new myData(id, year, month, day, hour, minute, title, description, alarmtype);
        list.add(data);
    }
}

