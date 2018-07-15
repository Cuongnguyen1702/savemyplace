package com.congcuong.savemyplace.map.service;


import com.congcuong.savemyplace.map.service.pojo.GeocodingRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceMapAPI {
    @GET("geocode/json?&key=AIzaSyAMeQrdYOblO4lAM74xpcCNoxvP9sLRElo")
    Call<GeocodingRoot> getLocation(@Query("address") String address);
}
