package com.asan.bletrack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import com.asan.bletrack.databinding.ActivitySettingBinding;

public class SettingActivity extends Activity {
    private EditText URL;
    private EditText pw;
    private ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIBind();
        URL.setText(StaticResources.ServerURL);
        pw.setText(Integer.toString(StaticResources.password));
    }

    protected void UIBind(){
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        URL = binding.URL;
        pw = binding.password;

    }
}
