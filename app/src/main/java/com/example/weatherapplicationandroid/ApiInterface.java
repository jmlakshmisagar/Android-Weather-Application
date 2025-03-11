package com.example.weatherapplicationandroid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("q") String cityName, @Query("units") String units, @Query("appid") String apiKey);
}
