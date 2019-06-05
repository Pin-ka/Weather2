package com.pinka.weather2.rest.entites;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("cod") public int cod;
    @SerializedName("list") public TemperItemRestModel[] list;
}
