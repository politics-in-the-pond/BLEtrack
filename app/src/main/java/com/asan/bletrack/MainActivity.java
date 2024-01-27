package com.asan.bletrack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.asan.bletrack.filter.Kalman;


import com.asan.bletrack.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private TextView deviceidText;
    private ActivityMainBinding binding;
    private Intent foregroundService;
    private ImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIBind();

        foregroundService = new Intent(this, WatchForegroundService.class);
        foregroundService.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        foregroundService.setData(Uri.parse("package:" + getPackageName()));
        startForegroundService(foregroundService);

        String deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        StaticResources.deviceID = deviceid;
        deviceidText.setText(deviceid);
    }

    protected void UIBind(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deviceidText = binding.deviceid;
        setting = binding.setting;

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}