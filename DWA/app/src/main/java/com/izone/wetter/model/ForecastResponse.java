package com.izone.wetter.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
    @SerializedName("list")
    public List<ForecastItem> list;

    public static class ForecastItem {
        @SerializedName("dt_txt")
        public String dt_txt;

        @SerializedName("main")
        public Main main;

        @SerializedName("weather")
        public List<Weather> weather;

        public static class Main {
            public double temp;
        }

        public static class Weather {
            public String main;
            public String description;
        }
    }
}

