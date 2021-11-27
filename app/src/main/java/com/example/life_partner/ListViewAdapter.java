package com.example.life_partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<myData> list  = new ArrayList<myData>();

    public ListViewAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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

        TextView timeText = view.findViewById(R.id.time_text);
        TextView titleText = view.findViewById(R.id.title_text);
        timeText.setText(Integer.toString(listdata.getHour()) + ":" + Integer.toString(listdata.getMinute()));
        titleText.setText(listdata.getTitle());

        return view;
    }

    public void addItem(int id, int year, int month, int day, int hour, int minute, String title, String description) {
        myData data = new myData(id, year, month, day, hour, minute, title, description);
        list.add(data);
    }


}

