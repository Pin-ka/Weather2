package com.pinka.weather2.rest.entites;

import com.google.gson.annotations.SerializedName;

public class MainElementRestModel {
    @SerializedName("temp_min") public float temp_min;
    @SerializedName("temp_max") public float temp_max;
    @SerializedName("pressure") public float pressure;
}
