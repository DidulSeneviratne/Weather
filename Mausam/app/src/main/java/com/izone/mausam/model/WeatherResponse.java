package com.izone.mausam.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("name")
    public String name;

    public class Main {
        public double temp;
        @SerializedName("feels_like")
        public double feels_like;
    }

    public class Weather {
        public String main;
        public String description;
        public String icon;
    }

    public class Wind {
        public double speed;
    }
}
