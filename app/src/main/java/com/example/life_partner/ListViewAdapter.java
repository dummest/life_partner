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
    ImageButton imageButton;
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
        imageButton = view.findViewById(R.id.deleteBtn);


        String hourString = "";
        String minuteString = "";

        if(listdata.getHour() >= 10)
            hourString += Integer.toString(listdata.getHour()) + ":";
        else
            hourString += "0" + Integer.toString(listdata.getHour()) + ":";
        if(listdata.getMinute() >= 10)
            minuteString += Integer.toString(listdata.getMinute());
        else
            minuteString += "0" + Integer.toString(listdata.getMinute());

        timeText.setText(hourString + minuteString);

        titleText.setText(listdata.getTitle());




        if(context == context.getApplicationContext()){
            imageButton.setEnabled(false);
            imageButton.setVisibility(View.GONE);
        }
        else{
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = new MainActivity();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setIcon(R.drawable.outline_clear_24)
                            .setTitle("????????? ???????????????")
                            .setMessage(listdata.getTitle() + " ????????? ?????? ???????????????????\n??? ????????? ????????? ??? ????????????.")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(context, "?????? ????????? ???????????????", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("??????", null);

                    builder.create().show();
                }
            });
        }

        return view;
    }

    public void addItem(int id, int year, int month, int day, int hour, int minute, String title, String description, int alarmtype) {
        myData data = new myData(id, year, month, day, hour, minute, title, description, alarmtype);
        list.add(data);
    }
}
