package com.izone.globalweather.air_quality;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.izone.globalweather.R;
import com.izone.globalweather.air_quality.adapter.City;
import com.izone.globalweather.air_quality.adapter.RecycleViewAdapter;
import com.izone.globalweather.air_quality.adapter.RecycleViewInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity1 extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView lattitude, longitude, address, city, country, nh3, NO2, S02, O3, NO, co;
    Button getLocation;
    private final static int REQUEST_CODE = 100;
    static String slattitude, slongitude, saddress, scity, scountry;
    SearchView searchView;
    int meterValue;
    private Button TurnGPSOn;
    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    String addresses3;
    List<PollutionModel> PollutionList = new ArrayList<>();
    public double Latitude;
    public double Longitude;

    private RecycleViewAdapter adapter;
    private List<City> cityArrayList;
    private RecyclerView recyclerView;
    private RecycleViewInterface recycleViewInterface;
    ImageView back, loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_main);
        back = findViewById(R.id.back);
        loc = findViewById(R.id.loc);
        getSupportActionBar();

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest =new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        searchView = (SearchView) findViewById(R.id.newwi);
//      refrencrto table textview
        co = findViewById(R.id.co);
        NO = findViewById(R.id.NO);
        NO2 = findViewById(R.id.NO2);
        O3 = findViewById(R.id.O3);
        S02 = findViewById(R.id.S02);
        nh3 = findViewById(R.id.nh3);

        getLastLocation();
        getwederdata();
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0000FF"));
        setTitle("Home");
        // Set BackgroundDrawable
        //actionBar.setBackgroundDrawable(colorDrawable);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // recyclerView hide
                recyclerView.setVisibility(View.GONE);

                // Create a Geocoder instance


                int faind = 0;
                Geocoder geocoder = new Geocoder(MainActivity1.this);

                try {
                    // Get the list of Address objects matching the city name
                    List<Address> addresses = geocoder.getFromLocationName(query, 1);

                    if (!addresses.isEmpty()) {
                        // Get the first Address object from the list
                        Address address = addresses.get(0);

                        // Get the latitude and longitude of the city
                        Latitude = address.getLatitude();
                        Longitude = address.getLatitude();
                        Log.i("qw-log", String.valueOf(Longitude));

                        faind = 1;
                        getwederdata();

                        // Do something with the latitude and longitude values
                        // (e.g. display them on a map or use them to make an API call)
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (faind == 0) {
                    Toast.makeText(MainActivity1.this, "Wrong city or country name .Try again", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Do something when the search text changes
//                getwederdata();
                if (!newText.isEmpty()){
                    List<City> collect = cityArrayList.stream().filter(r -> r.getCountry().toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList());
                    if (collect.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        return true;
                    }
                    adapter = new RecycleViewAdapter(collect, getApplicationContext(),(position)->{
                        searchView.setQuery(position.getCountry(), true);
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    //Toast.makeText(getBaseContext(),newText,Toast.LENGTH_SHORT).show();


                }else{
                    recyclerView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        cityArrayList = City.getCityList();
        recyclerView = findViewById(R.id.searchViewHolder);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleViewAdapter(cityArrayList, this, (position)->{
            searchView.setQuery(position.getCountry(), true);
        });
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity1.this, MainActivity1.class));
                finish();
            }
        });

    }

    private void getwederdata() {

        double lat = Latitude;
        double lon = Longitude;
        String appid = "9752bcab22549752ebb0a568e96eb9cd";

        RetrofitClient.getRetrofitClient().getdata(lat, lon, "9752bcab22549752ebb0a568e96eb9cd").enqueue(new Callback<PollutionModel>() {
            @Override
            public void onResponse(Call<PollutionModel> call, Response<PollutionModel> response) {
                Log.i("logi23", "onrespond");

                Log.i("logi23", String.valueOf(response));

                if (response.isSuccessful() && response.body() != null) {
                    Log.i("logi23", "data2");
                    List<PollutionModel.list> Coorddata = response.body().getEntries();
                    Log.i("valuedhi", String.valueOf(response.body().getEntries()));


                    for (PollutionModel.list quiz : Coorddata) {


                        co.setText(Double.toString(quiz.getComponents().getCo()) + " μg/m3");
                        NO.setText(Double.toString(quiz.getComponents().getNo()) + " μg/m3");
                        NO2.setText(Double.toString(quiz.getComponents().getNo2()) + " μg/m3");
                        O3.setText(Double.toString(quiz.getComponents().getO3()) + " μg/m3");
                        S02.setText(Double.toString(quiz.getComponents().getSo2()) + " μg/m3");
                        nh3.setText(Double.toString(quiz.getComponents().getNh3()) + " μg/m3");
                        meterValue = quiz.getMain().getAqi();
                        Log.i("logi2312", "data:- " + quiz.getMain().getAqi());
                    }
//                    PollutionModel.coord Coorddata2 = response.body().getCoord();

                    airmeter();
                }
            }

            @Override
            public void onFailure(Call<PollutionModel> call, Throwable t) {
                Log.i("List1", "error");
            }
        });

    }

    private void airmeter() {
        ProgressBar progressBar = findViewById(R.id.aqiProgressBar);

        int maxValue = 500; // AQI scale
        progressBar.setMax(maxValue);

        // Scale 1–5 → 100–500
        int scaledValue = meterValue * 100;
        progressBar.setProgress(scaledValue);

        // Change color based on scaled AQI
        if (scaledValue <= 50) {
            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        } else if (scaledValue <= 100) {
            progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
            //Toast.makeText(this, String.valueOf(scaledValue), Toast.LENGTH_SHORT).show();
        } else if (scaledValue <= 150) {
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_IN);
        } else if (scaledValue <= 200) {
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            //Toast.makeText(this, String.valueOf(scaledValue), Toast.LENGTH_SHORT).show();
        } else if (scaledValue <= 300) {
            progressBar.getProgressDrawable().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_IN);
        } else {
            progressBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        }
    }

    private void getLastLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(MainActivity1.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Latitude = addresses.get(0).getLatitude();
                            Longitude = addresses.get(0).getLongitude();
                            addresses3 = addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName();
                            searchView.setQuery(addresses3, true);
                            Log.i("city2", addresses3);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        } else {
            askPermission();
        }

    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity1.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void Sinout(Context applicationContext) {
        System.exit(1);
    }


}