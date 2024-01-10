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

import com.asan.bletrack.filter.Kalman;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.RangedBeacon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class WatchForegroundService extends Service {
    String TAG = "WatchForegroundService";
    private Context context = null;
    private PowerManager.WakeLock wakeLock;
    private BeaconManager beaconManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override //서비스 시작 시
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("WatchForegroundService","measure start");
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "bletrack::wakelock");
        wakeLock.acquire();
        initBeaconManager();
        foregroundNotification();

        return START_STICKY;
    }

    void initBeaconManager(){
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        NotificationCompat.Builder builder;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "BLE_service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BLE Scanning",
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
        beaconManager.addRangeNotifier(new RangeNotifier() {
            Kalman kalman = new Kalman();
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {
                    Beacon beacon = beacons.iterator().next();
                    int rssi = beacon.getRssi();
                    double frssi = kalman.do_calc((double) rssi);
                    String address = beacon.getBluetoothAddress();
                    String name = beacon.getParserIdentifier();
                    Log.i(TAG, "The first beacon, raw rssi :"+ Integer.toString(rssi) + " filtered: " + Double.toString(frssi) + " address : " + address + " name : "+ name);
                }
            }
        });
        beaconManager.enableForegroundServiceScanning(builder.build(), 456);
        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.startRangingBeacons(new Region("myRangingUniqueId", null, null, null));
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
