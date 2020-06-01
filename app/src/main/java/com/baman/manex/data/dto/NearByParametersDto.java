package com.baman.manex.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByParametersDto {

    @SerializedName("latitude")
    @Expose
    private String lat ;

    @SerializedName("longitude")
    @Expose
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLatLng() {
        return lat + "," + lng;
    }
}
