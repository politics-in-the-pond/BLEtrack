package com.asan.bletrack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.asan.bletrack.StaticResources;
import com.asan.bletrack.databinding.ActivityPasswordBinding;

public class PasswordActivity extends Activity {
    private EditText pw;
    private com.asan.bletrack.databinding.ActivityPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIBind();
    }

    protected void UIBind(){
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pw = binding.passwordInput;
        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Integer.parseInt(charSequence.toString())== StaticResources.password){
                    Intent stintent = new Intent(PasswordActivity.this, SettingActivity.class);
                    startActivity(stintent);
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
