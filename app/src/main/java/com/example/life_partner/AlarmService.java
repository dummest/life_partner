package com.example.life_partner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {
    String TAG = "TAG+Service";
    String CHANNEL_ID = "Life_partner";
    NotificationManager notificationManager;
    int importance;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(CHANNEL_ID, "default channel", NotificationManager.IMPORTANCE_HIGH);//채널 생성

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int notiId = intent.getIntExtra("notiId", 0);
        myDBHelper dbHelper = new myDBHelper(getApplicationContext());
        String sql = "SELECT title, description from notiTBL where notiId =" + notiId + ";";
        String title = "제목";
        String description = "내용";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            title = cursor.getString(0);
            description = cursor.getString(1);
        }
        cursor.close();

        createNotification(CHANNEL_ID, notiId, title, description);

        return super.onStartCommand(intent, flags, startId);
    }

    void createNotificationChannel(String channelId, String channelName, int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String description){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);//터치시 메인액티비티로 이동할 인텐트
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)//헤드업 알람
                .setContentTitle(title)//제목 텍스트
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher)//작은 아이콘
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//알림시 효과음, 진동여부
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
