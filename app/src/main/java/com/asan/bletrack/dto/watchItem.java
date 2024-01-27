package com.asan.bletrack.dto;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class watchItem {
    @Expose
    @SerializedName("item") public BeaconSignal[] item = new BeaconSignal[]{};
    @SerializedName("deviceID") public String deviceID;
}
