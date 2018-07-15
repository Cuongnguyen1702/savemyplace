package com.congcuong.savemyplace.map.service.pojo;

public class Location {

    private double Lat;
    private double Lng;

    public Location(double lat, double lng) {
        Lat = lat;
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public Location setLat(double lat) {
        Lat = lat;
        return this;
    }

    public double getLng() {
        return Lng;
    }

    public Location setLng(double lng) {
        Lng = lng;
        return this;
    }
}
