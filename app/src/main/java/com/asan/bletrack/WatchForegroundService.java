package com.asan.bletrack;

import static android.content.Context.POWER_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WatchForegroundService extends Service {
    private Context context = null;
    private PowerManager.WakeLock wakeLock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override //서비스 시작 시
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("WatchForegroundService","measure start");
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "bletrack::wakelock");
        wakeLock.acquire();

        foregroundNotification();



        return START_STICKY;
    }

    void foregroundNotification() { // foreground 실행 후 신호 전달 (안하면 앱 강제종료 됨)
        NotificationCompat.Builder builder;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "positioning_service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Positioning Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.drawable.baseline_pin_drop_24)
                .setContentTitle("ASAN BLE")
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
