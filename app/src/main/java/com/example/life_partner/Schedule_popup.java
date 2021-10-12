package com.example.life_partner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;


public class Schedule_popup extends Activity {
    TimePicker tp;
    DatePicker dp;
    Switch swch;
    EditText et;
    CheckBox[] cb = new CheckBox[7];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.schedule_add);

        tp = findViewById(R.id.time_selector);
        dp = findViewById(R.id.date_selector);
        swch = findViewById(R.id.set_style_switch);
        et = findViewById(R.id.schedule_description);
        cb[0] = findViewById(R.id.sundayBox);
        cb[1] = findViewById(R.id.mondayBox);
        cb[2] = findViewById(R.id.tuesdayBox);
        cb[3] = findViewById(R.id.wednesdayBox);
        cb[4] = findViewById(R.id.thursdayBox);
        cb[5] = findViewById(R.id.fridayBox);
        cb[6] = findViewById(R.id.saturdayBox);

        dp.setMinDate(System.currentTimeMillis());
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
    }

}
