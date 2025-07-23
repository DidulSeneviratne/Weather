package com.izone.wetter;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class WeatherWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            fetchWeatherAndUpdate(context, widgetId);
        }
    }

    private void fetchWeatherAndUpdate(Context context, int widgetId) {
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PermissionChecker.PERMISSION_GRANTED) {
            FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(context);
            try {
                locationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        fetchWeather(context, widgetId, lat, lon);
                    } else {
                        fetchWeatherByCity(context, widgetId, "Colombo");
                    }
                });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            fetchWeatherByCity(context, widgetId, "Colombo");
        }
    }

    private void fetchWeather(Context context, int widgetId, double lat, double lon) {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                    String lang = prefs.getString("lang", "en");
                    String units = prefs.getString("units", "metric");
                    String apiKey = "4e0f642bc59577eadf094fa2366f5c1a";

                    String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon +
                            "&appid=" + apiKey + "&units=" + units + "&lang=" + lang;

                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";

                    JSONObject json = new JSONObject(response);
                    json.put("lat", lat);
                    json.put("lon", lon);
                    return json;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                updateWidgetData(context, widgetId, response);
            }
        }.execute();
    }

    private void fetchWeatherByCity(Context context, int widgetId, String city) {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                    String lang = prefs.getString("lang", "en");
                    String units = prefs.getString("units", "metric");
                    String apiKey = "4e0f642bc59577eadf094fa2366f5c1a";

                    String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                            "&appid=" + apiKey + "&units=" + units + "&lang=" + lang;

                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";

                    return new JSONObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                updateWidgetData(context, widgetId, response);
            }
        }.execute();
    }

    private void updateWidgetData(Context context, int widgetId, JSONObject response) {
        if (response != null) {
            try {
                String temp = Math.round(response.getJSONObject("main").getDouble("temp")) + "°";
                String condition = response.getJSONArray("weather").getJSONObject(0).getString("main");
                String city = response.getString("name");
                String iconCode = response.getJSONArray("weather").getJSONObject(0).getString("icon");
                double lat = response.optDouble("lat", 0);
                double lon = response.optDouble("lon", 0);

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String locationDetail = "";
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address addr = addresses.get(0);
                        locationDetail = addr.getSubAdminArea() + ", " + addr.getCountryName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM 'at' h:mm a", Locale.getDefault());
                String currentTime = sdf.format(new Date());

                SharedPreferences.Editor editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
                editor.putString("last_temp", temp);
                editor.putString("last_cond", condition);
                editor.putString("last_city", city);
                editor.putString("last_code", iconCode);
                editor.putString("last_detail", locationDetail);
                editor.putString("last_time", currentTime);
                editor.apply();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateWidget(context, appWidgetManager, widgetId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_weather);

        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String temp = prefs.getString("last_temp", "25°");
        String cond = prefs.getString("last_cond", "Clear");
        String city = prefs.getString("last_city", "Your City");
        String iconCode = prefs.getString("last_code", "01d");
        String locationDetail = prefs.getString("last_detail", "Panadura, Sri Lanka");
        String time = prefs.getString("last_time", "Sat 12 July at 6:00 pm");

        views.setTextViewText(R.id.widget_city, city);
        views.setTextViewText(R.id.widget_temp, temp);
        views.setTextViewText(R.id.widget_condition, cond);
        views.setTextViewText(R.id.widget_location_detail, locationDetail);
        views.setTextViewText(R.id.widget_time, time);

        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    return BitmapFactory.decodeStream(new URL(strings[0]).openConnection().getInputStream());
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    views.setImageViewBitmap(R.id.widget_icon, bitmap);
                }

                Intent intent = new Intent(context, WeatherWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        widgetId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
                views.setOnClickPendingIntent(R.id.widget_refresh, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, views);
            }
        }.execute(iconUrl);
    }
}
