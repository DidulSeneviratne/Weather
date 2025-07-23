package com.izone.globalweather.model;

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

        // Add wind
        @SerializedName("wind")
        public Wind wind;

        // Add precipitation probability
        @SerializedName("pop")
        public double pop;

        // ✅ Add rain volume (may be null)
        @SerializedName("rain")
        public Rain rain;

        public static class Main {
            public double temp;

            // ✅ Add pressure
            @SerializedName("pressure")
            public double pressure;
        }

        public static class Weather {
            public String main;
            public String description;
            public String icon; // Optional: for loading weather icon
        }

        public static class Wind {
            public double speed;
        }

        public static class Rain {
            // 3-hour volume of rain
            @SerializedName("3h")
            public double volume;
        }
    }
}
