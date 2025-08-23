package com.izone.globalweather.air_quality;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

        @GET("data/2.5/air_pollution?")
        Call<PollutionModel> getdata(
                @Query("lon") double lon,
                @Query("lat") double lat,
                @Query("appid") String appid
        );




}
