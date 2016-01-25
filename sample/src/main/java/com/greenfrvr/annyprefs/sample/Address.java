package com.greenfrvr.annyprefs.sample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by greenfrvr
 */
public class Address {

    @SerializedName("contry")
    int countryCode;

    @SerializedName("city")
    String city;

    @SerializedName("streetName")
    String streetName;

    @SerializedName("streetNum")
    int streetNum;

    @Override
    public String toString() {
        return "Address{" +
                "countryCode=" + countryCode +
                ", city='" + city + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetNum=" + streetNum +
                '}';
    }
}
