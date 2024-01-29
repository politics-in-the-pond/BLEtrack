package com.asan.bletrack;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsLoader {
    SharedPreferences pref = StaticResources.maincontext.getSharedPreferences("settings", Context.MODE_PRIVATE);
    public void getsettings(){
        StaticResources.ServerURL = this.pref.getString("url", "http://127.0.0.1:8765/");
        StaticResources.password = this.pref.getInt("pw", 501020);
    }

    public void putsettings(String url, int pw){
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putString("url", url);
        editor.putInt("pw", pw);
        editor.commit();
    }
}
