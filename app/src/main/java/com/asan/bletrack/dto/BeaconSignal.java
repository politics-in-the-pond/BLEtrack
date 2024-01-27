package com.asan.bletrack.dto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeaconSignal {
    @Expose
    @SerializedName("BLEaddress") public String BLEaddress;
    @SerializedName("rssi") public double rssi;
    @SerializedName("major") public int major;
    @SerializedName("minor") public int minor;
    @SerializedName("rx") public int rx;

}
