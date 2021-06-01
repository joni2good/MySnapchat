package com.example.mysnapchat.models;

import com.google.android.gms.maps.model.LatLng;

public class Pin {
    String id;
    String name;
    LatLng latLng;

    public Pin(String id, String name, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
    }

    public Pin(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    public Pin() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
