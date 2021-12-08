package com.example.life_partner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.net.URI;
import java.util.Calendar;

import in.shadowfax.proswipebutton.ProSwipeButton;

public class AlarmActivity extends AppCompatActivity {
    Calendar calendar;
    ProSwipeButton proSwipeButton;
    TextView timeText, titleText, descriptionText;
    MediaPlayer mediaPlayer;
    Vibrator vibrator;

    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        calendar = Calendar.getInstance();
        proSwipeButton =  findViewById(R.id.swipe);
        timeText = (TextView) findViewById(R.id.time_view);
        descriptionText = findViewById(R.id.description_view);
        titleText = findViewById(R.id.title_view);

        titleText.setText(getIntent().getStringExtra("title"));
        descriptionText.setText(getIntent().getStringExtra("description"));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100,1000};
        v.vibrate(pattern,-1);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
        ringtone.setAudioAttributes(audioAttributes);
        ringtone.play();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag == true) {
                    try {
                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.HOUR_OF_DAY) > 0 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                            timeText.setText("AM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 ");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
                            timeText.setText("PM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 ");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) > 12 && calendar.get(Calendar.HOUR_OF_DAY) < 24) {
                            timeText.setText("PM " + (calendar.get(Calendar.HOUR_OF_DAY) - 12) + "시 " + calendar.get(Calendar.MINUTE) + "분 ");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                            timeText.setText("AM 0시 " + calendar.get(Calendar.MINUTE) + "분 ");
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }).start(); // 실시간으로 시계 출력

        proSwipeButton.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                vibrator.cancel();
                ringtone.stop();
                flag = false;
                finish();
            }
        });
    }
}