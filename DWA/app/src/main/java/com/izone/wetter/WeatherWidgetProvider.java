package com.izone.wetter;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Scanner;

public class WeatherWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            fetchWeatherAndUpdate(context, widgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (ids != null) {
                for (int id : ids) {
                    fetchWeatherAndUpdate(context, id);
                }
            }
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
                        fetchWeatherByCoords(context, widgetId, lat, lon);
                    } else {
                        // fallback if location is null
                        fetchWeatherByCity(context, widgetId, "Colombo");
                    }
                });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            // fallback if no permission
            fetchWeatherByCity(context, widgetId, "Colombo");
        }
    }

    private void fetchWeatherByCoords(Context context, int widgetId, double lat, double lon) {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                    String lang = prefs.getString("lang", "en");
                    String units = prefs.getString("units", "metric");
                    String apiKey = "4e0f642bc59577eadf094fa2366f5c1a";

                    String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon
                            + "&appid=" + apiKey + "&units=" + units + "&lang=" + lang;

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

    private void fetchWeatherByCity(Context context, int widgetId, String city) {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
                    String lang = prefs.getString("lang", "en");
                    String units = prefs.getString("units", "metric");
                    String apiKey = "4e0f642bc59577eadf094fa2366f5c1a";

                    String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city
                            + "&appid=" + apiKey + "&units=" + units + "&lang=" + lang;

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

                SharedPreferences.Editor editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
                editor.putString("last_temp", temp);
                editor.putString("last_cond", condition);
                editor.putString("last_city", city);
                editor.apply();

                Log.d("WeatherWidget", "Weather: " + city + ", " + temp + ", " + condition);

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

        views.setTextViewText(R.id.widget_city, city);
        views.setTextViewText(R.id.widget_temp, temp);
        views.setTextViewText(R.id.widget_condition, cond);

        Intent intent = new Intent(context, WeatherWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_refresh, pendingIntent);

        appWidgetManager.updateAppWidget(widgetId, views);
    }
}