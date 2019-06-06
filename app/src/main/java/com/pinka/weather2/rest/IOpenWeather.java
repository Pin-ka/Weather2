package com.pinka.weather2.rest;

import com.pinka.weather2.rest.entites.WeatherRequestRestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String city,
                                              @Query("units") String units,
                                              @Query("cnt") int quantityFor3hours,
                                              @Query("appid") String keyApi);
}
