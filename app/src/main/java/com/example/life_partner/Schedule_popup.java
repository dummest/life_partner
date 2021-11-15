package com.example.life_partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;


public class Schedule_popup extends Activity {
    TimePicker tp;
    DatePicker dp;
    Switch swch;
    EditText et;
    CheckBox[] cb = new CheckBox[7];
    TableRow tr;
    Button save;
    EditText schedule_descripsion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.schedule_add);

        tp = findViewById(R.id.time_selector);
        dp = findViewById(R.id.date_selector);
        swch = findViewById(R.id.set_style_switch);
        et = findViewById(R.id.schedule_description);
        tr = findViewById(R.id.cb_table);
        cb[0] = findViewById(R.id.sundayBox);
        cb[1] = findViewById(R.id.mondayBox);
        cb[2] = findViewById(R.id.tuesdayBox);
        cb[3] = findViewById(R.id.wednesdayBox);
        cb[4] = findViewById(R.id.thursdayBox);
        cb[5] = findViewById(R.id.fridayBox);
        cb[6] = findViewById(R.id.saturdayBox);
        schedule_descripsion = findViewById(R.id.schedule_description);
        save = findViewById(R.id.schedule_btn_save);


        dp.setMinDate(System.currentTimeMillis());
        dp.updateDate(
                getIntent().getIntExtra("selected_year", 2021),
                getIntent().getIntExtra("selected_month", 0),
                getIntent().getIntExtra("selected_day", 1)
        );
        Date currentDate = new Date();

        tp.setHour(currentDate.getHours());
        tp.setMinute(currentDate.getMinutes());


        swch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (swch.isChecked()) {
                    tr.setVisibility(View.VISIBLE);
                    dp.setVisibility(View.GONE);
                }
                else{
                    tr.setVisibility(View.GONE);
                    dp.setVisibility(View.VISIBLE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            boolean cb_checked = false;
            @Override
            public void onClick(View view) {
                if (swch.isChecked()){
                    for(CheckBox c : cb){
                        if (c.isChecked())
                            cb_checked = true;
                    }
                    if(!cb_checked){
                        Toast.makeText(getApplicationContext(), "set your day of the week", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Date selectedDate = new Date(dp.getYear(),dp.getMonth(),dp.getDayOfMonth(),tp.getHour(),tp.getMinute());

                if(selectedDate.before(currentDate)){
                    Toast.makeText(getApplicationContext(), "you can't plan before than now", Toast.LENGTH_LONG).show();

                }

                Intent intent = new Intent();
                intent.putExtra("saved_year", dp.getYear());
                intent.putExtra("saved_month", dp.getMonth());
                intent.putExtra("saved_day", dp.getDayOfMonth());
                intent.putExtra("week_of_day_checked", swch.isChecked());
                intent.putExtra("sunday_is_checked", cb[0].isChecked());
                intent.putExtra("monday_is_checked", cb[1].isChecked());
                intent.putExtra("tuesday_is_checked", cb[2].isChecked());
                intent.putExtra("wednesday_is_checked", cb[3].isChecked());
                intent.putExtra("thursday_is_checked", cb[4].isChecked());
                intent.putExtra("friday_is_checked", cb[5].isChecked());
                intent.putExtra("saturday_is_checked", cb[6].isChecked());

                intent.putExtra("saved_hour", tp.getHour());
                intent.putExtra("saved_minute", tp.getMinute());
                intent.putExtra("alarm_description", schedule_descripsion.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
