package com.izone.mausam;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.izone.mausam.Ads.MyApp;
import com.izone.mausam.adapter.DailyForecastAdapter;
import com.izone.mausam.adapter.HourlyForecastAdapter;
import com.izone.mausam.api.ApiClient;
import com.izone.mausam.api.WeatherService;
import com.izone.mausam.model.ForecastResponse;
import com.izone.mausam.model.WeatherResponse;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textTemperature, textCondition, textFeelsLike, textWind;
    TextInputEditText textCity;
    // ImageButton btnLanguage, btnMenu;
    Button btnAddWidget, btnAddMap;
    Switch switchTheme;
    RecyclerView recyclerHourly, recyclerDaily;
    ImageView imageWeatherIcon;
    private FusedLocationProviderClient fusedLocationClient;
    double lat, lon;
    Toolbar toolbar;
    ImageButton menu, widget, search, map, power, refresh, expandButton;
    CardView cardView;
    AdView adView;
    TextView fabLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).appOpenAdManager.showAdIfAvailable(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        boolean isInternetAvailable = activeNetwork != null && activeNetwork.isConnected();

        if (!isLocationEnabled || !isInternetAvailable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Requirements Not Met")
                    .setMessage("This app requires both Location and Internet to work. Please enable them to continue.")
                    .setCancelable(false)
                    .setPositiveButton("Settings", (dialog, which) -> {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        finish();
                    })
                    .setNegativeButton("Exit", (dialog, which) -> {
                        finish();
                    });

            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        menu = findViewById(R.id.menu);
        cardView = findViewById(R.id.cardview);

        textCity = findViewById(R.id.textCity);
        textTemperature = findViewById(R.id.textTemperature);
        textCondition = findViewById(R.id.textCondition);
        textFeelsLike = findViewById(R.id.textFeelsLike);
        textWind = findViewById(R.id.textWind);
        /* btnLanguage = findViewById(R.id.btnLanguage);
        btnMenu = findViewById(R.id.btnMenu);*/
        btnAddWidget = findViewById(R.id.btnAddWidget);
        btnAddMap = findViewById(R.id.btnAddMap);
        recyclerHourly = findViewById(R.id.recyclerHourly);
        recyclerDaily = findViewById(R.id.recyclerDaily);
        imageWeatherIcon = findViewById(R.id.imageWeatherIcon);

        recyclerHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerDaily.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            getUserLocation();
        }

        MobileAds.initialize(this, initializationStatus -> {});
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);

        // boolean adsEnabled = pref.getBoolean("ads_enabled", true);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        /* if (adsEnabled) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        } */

        String lang = pref.getString("lang", "hi");
        setLocale(lang);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode active
            cardView.setCardBackgroundColor(Color.BLACK);
        } else {
            // Light mode
            cardView.setCardBackgroundColor(Color.WHITE); // Or your light theme color
        }

        //  loadWeatherData("New York");

        // UI binds
        switchTheme = findViewById(R.id.switchTheme);
        switchTheme.setChecked(darkMode);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate(); // Recreate activity to apply theme
        });

        expandButton = findViewById(R.id.expand);

        expandButton.setOnClickListener(v -> {
            View popupView = getLayoutInflater().inflate(R.layout.popup_menu_layout, null);
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            );

            popupWindow.setElevation(10f); // optional shadow

            // Set background if you want to dismiss on outside touch
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.holo_red_light));
            popupWindow.setOutsideTouchable(true);

            // Show below the expand button
            popupWindow.showAsDropDown(v, -100, 10); // adjust x/y offset as needed

            // Handle button actions
            TextView fabLang = popupView.findViewById(R.id.fab_lang);
            SharedPreferences langPref = getSharedPreferences("settings", MODE_PRIVATE);
            String currentLang = langPref.getString("lang", "en");
            // Set next language label
            switch (currentLang) {
                case "en":
                    fabLang.setText("हिंदी");
                    break;
                case "hi":
                    fabLang.setText("English");
                    break;
            }

            ImageButton widgetBtn = popupView.findViewById(R.id.widget);
            ImageButton mapBtn = popupView.findViewById(R.id.map);

            fabLang.setOnClickListener(langView -> {
                // Toggle language logic
                String nextLang;
                switch (currentLang) {
                    case "en": nextLang = "hi"; break;
                    case "hi": default: nextLang = "en"; break;
                }

                langPref.edit().putString("lang", nextLang).apply();
                setAppLocale(nextLang);

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                popupWindow.dismiss();
            });

            widgetBtn.setOnClickListener(widgetView -> {
                // Widget logic
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AppWidgetManager appWidgetManager = getSystemService(AppWidgetManager.class);
                    ComponentName myProvider = new ComponentName(this, WeatherWidgetProvider.class);

                    if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                        appWidgetManager.requestPinAppWidget(myProvider, null, null);
                        Toast.makeText(this, "Widget available for manual adding", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Widget pinning not supported on this device", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Requires Android 8.0 or higher", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            });

            mapBtn.setOnClickListener(mapView -> {
                startActivity(new Intent(MainActivity.this, WeatherMapActivity.class));
                popupWindow.dismiss();
            });
        });

        refresh = findViewById(R.id.refresh);
        power = findViewById(R.id.power);
        textCity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String city = textCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    loadWeatherData(city); // your method
                }
                return true;
            }
            return false;
        });

        refresh.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        });

        power.setOnClickListener(view -> {
            finishAffinity();
        });

        menu.setOnClickListener(view -> {
            Context wrapper = new ContextThemeWrapper(this, R.style.CustomPopupMenuTheme);
            PopupMenu popup = new PopupMenu(wrapper, view);
            popup.getMenuInflater().inflate(R.menu.overflow_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_settings) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
                } else if (id == R.id.menu_weather_map) {
                    startActivity(new Intent(MainActivity.this, WeatherMapActivity.class));
                    return true;
                } else if (id == R.id.menu_add_widget) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        AppWidgetManager appWidgetManager = getSystemService(AppWidgetManager.class);
                        ComponentName myProvider = new ComponentName(this, WeatherWidgetProvider.class);

                        if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                            appWidgetManager.requestPinAppWidget(myProvider, null, null);
                            Toast.makeText(this, "Widget available for manual adding", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Widget pinning not supported on this device", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Requires Android 8.0 or higher", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (id == R.id.menu_change_city) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getString(R.string.enter_city_title));

                    final EditText input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    builder.setView(input);

                    builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        String city = input.getText().toString().trim();
                        try {
                            if (!city.isEmpty()) {
                                loadWeatherData(city);
                            }
                        }catch (Exception e){
                            Toast.makeText(this, getString(R.string.city_not_available), Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
                    builder.show();
                    return true;
                }
                return false;
            });

            popup.show();
        });

        /* btnLanguage.setOnClickListener(v -> {
            SharedPreferences prefs1 = getSharedPreferences("settings", MODE_PRIVATE);
            String currentLang = prefs1.getString("lang", "en");
            String newLang = currentLang.equals("en") ? "hi" : "en";

            // Save to preferences
            prefs1.edit().putString("lang", newLang).apply();

            // Update Locale
            setLocale(newLang);

            Intent intent = getIntent();
            finish();
            startActivity(intent); // Refresh UI
        }); */

        btnAddWidget.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppWidgetManager appWidgetManager = getSystemService(AppWidgetManager.class);
                ComponentName myProvider = new ComponentName(this, WeatherWidgetProvider.class);

                if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                    appWidgetManager.requestPinAppWidget(myProvider, null, null);
                    Toast.makeText(this, "Widget available for manual adding", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Widget pinning not supported on this device", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Requires Android 8.0 or higher", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddMap.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, WeatherMapActivity.class));
        });

    }

    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.menu_weather_map) {
            startActivity(new Intent(this, WeatherMapActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */

    private void setAppLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("location", lat + " " + lon);
                String unit = getSharedPreferences("settings", MODE_PRIVATE).getString("units", "metric");
                String lan = getSharedPreferences("settings", MODE_PRIVATE).getString("lang", "en");
                loadWeatherDataByCoords(lat, lon, lan, unit);

            } else {
                loadWeatherData("New York"); // fallback
            }
        });
    }

    private void getUserCity() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                Log.d("location", String.valueOf(location));
                getCityFromLocation(location);
            } else {
                Toast.makeText(this, "Could not get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCityFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                if (cityName != null) {
                    Log.d("city", cityName);
                    loadWeatherData(cityName); // 🟩 Load weather for detected city
                } else {
                    Toast.makeText(this, "City not found. Defaulting to New York.", Toast.LENGTH_SHORT).show();
                    loadWeatherData("Colombo");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadWeatherData("Colombo");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            Toast.makeText(this, "Location permission is required for weather updates.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void loadWeatherDataByCoords(double lat, double lon, String lan, String unit) {
        WeatherService service = ApiClient.getClient().create(WeatherService.class);
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", lan);

        Call<WeatherResponse> call = service.getCurrentWeatherByCoords(lat, lon, "4e0f642bc59577eadf094fa2366f5c1a", unit, lang);
        Call<ForecastResponse> call1 = service.getForecastByCoords(lat, lon, "4e0f642bc59577eadf094fa2366f5c1a", unit, lang);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse data = response.body();

                    String iconCode = data.weather.get(0).icon;
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
                    Log.d("url", iconUrl);

                    if (!MainActivity.this.isFinishing() && !MainActivity.this.isDestroyed()) {
                        Glide.with(MainActivity.this)
                                .load(iconUrl)
                                .into(imageWeatherIcon);
                    }

                    textCity.setText(data.name); // Show city name
                    String unit = getSharedPreferences("settings", MODE_PRIVATE).getString("units", "metric");
                    String symbol = unit.equals("imperial") ? "°F" : "°C";
                    textTemperature.setText(Math.round(data.main.temp) + symbol);
                    textCondition.setText(data.weather.get(0).main);
                    textFeelsLike.setText(getString(R.string.feels_like) + " " + Math.round(data.main.feels_like) + "°");
                    textWind.setText(getString(R.string.wind) + ": " + data.wind.speed + " m/s");

                    // Save for widget
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("last_temp", Math.round(data.main.temp) + "°");
                    editor.putString("last_cond", data.weather.get(0).main);
                    editor.putString("last_city", data.name);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textCondition.setText("Error: " + t.getMessage());
            }
        });

        call1.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ForecastResponse.ForecastItem> allItems = response.body().list;

                    List<ForecastResponse.ForecastItem> hourly = allItems.subList(0, Math.min(6, allItems.size()));

                    List<ForecastResponse.ForecastItem> daily = new ArrayList<>();
                    for (ForecastResponse.ForecastItem item : allItems) {
                        if (item.dt_txt.contains("12:00:00")) {
                            daily.add(item);
                        }
                    }

                    recyclerHourly.setAdapter(new HourlyForecastAdapter(hourly));
                    DailyForecastAdapter adapter = new DailyForecastAdapter(daily, recyclerDaily);
                    recyclerDaily.setAdapter(adapter);
                    loadNativeAd(adapter, recyclerDaily); // Load the native ad after setting adapter
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e("ForecastError", t.getMessage());
            }
        });
    }

    private void loadNativeAd(DailyForecastAdapter adapter, RecyclerView recyclerDaily) {
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-1606200483291446/2190873399") // Replace with your real ad unit
                .forNativeAd(nativeAd -> {
                    adapter.setNativeAd(nativeAd);// Inject into adapter

                    recyclerDaily.getLayoutParams().height = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 1175, recyclerDaily.getResources().getDisplayMetrics());
                    recyclerDaily.requestLayout();
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Log.e("NativeAd", "Failed to load: " + adError.getMessage());

                        adapter.disableAd();

                        recyclerDaily.getLayoutParams().height = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 600, recyclerDaily.getResources().getDisplayMetrics());
                        recyclerDaily.requestLayout();
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private void loadWeatherData(String city) {
        WeatherService service = ApiClient.getClient().create(WeatherService.class);

        String lang = getSharedPreferences("settings", MODE_PRIVATE).getString("lang", "en");
        Call<WeatherResponse> call = service.getCurrentWeather(city, "4e0f642bc59577eadf094fa2366f5c1a", "metric", lang);
        Call<ForecastResponse> call1 = service.getForecast(city, "4e0f642bc59577eadf094fa2366f5c1a", "metric", lang);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse data = response.body();

                    textCity.setText(city);
                    String unit = getSharedPreferences("settings", MODE_PRIVATE).getString("units", "metric");
                    String symbol = unit.equals("imperial") ? "°F" : "°C";
                    textTemperature.setText(Math.round(data.main.temp) + symbol);
                    textCondition.setText(data.weather.get(0).main);
                    textFeelsLike.setText(getString(R.string.feels_like) + " " + Math.round(data.main.feels_like) + "°");
                    textWind.setText(getString(R.string.wind) + ": " + data.wind.speed + " m/s");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textCondition.setText("Error: " + t.getMessage());
            }
        });

        call1.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ForecastResponse.ForecastItem> allItems = response.body().list;

                    // Hourly: next 6 entries
                    List<ForecastResponse.ForecastItem> hourly = allItems.subList(0, Math.min(6, allItems.size()));

                    // Daily: filter 12:00:00 entries
                    List<ForecastResponse.ForecastItem> daily = new ArrayList<>();
                    for (ForecastResponse.ForecastItem item : allItems) {
                        if (item.dt_txt.contains("12:00:00")) {
                            daily.add(item);
                        }
                    }

                    // Set adapters
                    recyclerHourly.setAdapter(new HourlyForecastAdapter(hourly));
                    DailyForecastAdapter adapter = new DailyForecastAdapter(daily, recyclerDaily);
                    recyclerDaily.setAdapter(adapter);
                    loadNativeAd(adapter, recyclerDaily);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e("ForecastError", t.getMessage());
            }
        });
    }

}
