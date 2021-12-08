package com.example.life_partner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vp);

        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_calendar_today_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.baseline_alarm_on_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.baseline_settings_24);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}