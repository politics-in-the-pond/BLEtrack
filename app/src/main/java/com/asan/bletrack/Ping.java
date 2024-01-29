package com.asan.bletrack;

import java.net.HttpURLConnection;
import java.net.URL;

public class Ping extends Thread{
    private boolean success;
    private String url;

    public Ping(String url){
        this.url = url;
    }

    @Override
    public void run() {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestProperty("User-Agent","Android");
            conn.setConnectTimeout(1000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            System.out.println(Integer.toString(responseCode));
            if(responseCode == 204 || responseCode == 200) success = true;
            else success = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        if(conn != null){
            conn.disconnect();
        }
    }

    public boolean isSuccess(){
        return success;
    }

}