package com.example.life_partner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

        /////////////////////// 여기 //////////////////////////
        Intent svcIntent =  new Intent(getApplicationContext(), ScreenLockService.class);
        svcIntent.putExtra("command", "start");
        startForegroundService(svcIntent);///잠금화면 설정

        svcIntent.putExtra("command", "stop");
        startForegroundService(svcIntent);///잠금화면 설정 정지
        ///////////////////////설정에 스위치 넣어서 조절좀///////////
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}