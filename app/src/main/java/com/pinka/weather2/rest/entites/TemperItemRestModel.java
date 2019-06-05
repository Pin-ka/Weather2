package com.pinka.weather2.rest.entites;

import com.google.gson.annotations.SerializedName;

public class TemperItemRestModel {
    @SerializedName("main") public MainElementRestModel main;
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("clouds") public CloudsElementRestModel clouds;
}
