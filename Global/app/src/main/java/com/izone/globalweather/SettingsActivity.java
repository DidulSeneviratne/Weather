package com.izone.globalweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.content.res.Configuration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import android.content.Intent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class SettingsActivity extends AppCompatActivity {

    Switch switchTheme, switchAds;
    Button btnLang;
    RadioGroup radioUnits;
    Toolbar toolbar;
    ImageButton back;
    SharedPreferences prefs;
    AdView adView;
    private final String[] supportedLangCodes = {"en", "es", "hi", "ge", "pl", "pt", "ru", "si", "tr"};
    private final String[] supportedLangNames = {"English", "Spanish", "Hindi", "German", "Polish", "Portuguese", "Russian", "Sinhala", "Turkish"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        MobileAds.initialize(this, initializationStatus -> {});
        // switchAds = findViewById(R.id.switchAds);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        /* if (adView != null) {
            if (prefs.getBoolean("ads_enabled", true)) {
                adView.loadAd(new AdRequest.Builder().build());
            } else {
                adView.setVisibility(View.GONE);
            }
        } else {
            Log.e("SettingsActivity", "AdView not found in layout");
        } */


        /* Set saved value
        switchAds.setChecked(prefs.getBoolean("ads_enabled", true)); */

        /* Listen for changes
        switchAds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("ads_enabled", isChecked);
            editor.apply();

            Toast.makeText(this, "Ads " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        }); */

        // Apply theme before inflating layout
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Prevent recreate loop by clearing restart flag early
        if (prefs.getBoolean("restart_settings", false)) {
            prefs.edit().remove("restart_settings").apply();
        }

        setAppLocale(prefs.getString("lang", "en")); // Apply saved language before UI inflate
        // setThemeMode(); Apply theme before layout

        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.back);

        switchTheme = findViewById(R.id.switchTheme);
        btnLang = findViewById(R.id.btnLang);
        radioUnits = findViewById(R.id.radioUnits);

        // Load saved settings
        boolean dark = prefs.getBoolean("dark_mode", false);
        String lang = prefs.getString("lang", "en");
        String units = prefs.getString("units", "metric");

        switchTheme.setChecked(dark);
        if (units.equals("metric")) {
            radioUnits.check(R.id.radioMetric);
        } else {
            radioUnits.check(R.id.radioImperial);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Theme switch logic
        switchTheme.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Language switch logic
        btnLang.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle(getString(R.string.enter_city_title));

            builder.setItems(supportedLangNames, (dialog, which) -> {
                String selectedLang = supportedLangCodes[which];
                prefs.edit().putString("lang", selectedLang).apply();
                setAppLocale(selectedLang);

                // Restart to apply
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });

            builder.show();
        });

        // Units selection
        radioUnits.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedUnit = (checkedId == R.id.radioMetric) ? "metric" : "imperial";
            prefs.edit().putString("units", selectedUnit).apply();

            // Restart MainActivity
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close settings
        });
    }

    private void setAppLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setThemeMode() {
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    } */
}



