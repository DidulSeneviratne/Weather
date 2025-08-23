package com.izone.globalweather.air_quality;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.izone.globalweather.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsLocationActivity extends AppCompatActivity {

    private Button TurnGPSOn;
    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    private final static int REQUEST_CODE = 100;


    String gpsstatuas="off";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        getgpsenable();
        setTitle("Location");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("tag_what","gps chek sgsin");

                if(gpsstatuas =="off")
                {
                    //gotoActivity();
                    getgpsenable();
                }else {
                    getLastLocation();
                }




                handler.postDelayed(this, 3000); // repeat after 1 second
            }
        }, 0);

    }

    private void getgpsenable() {
        {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(2000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);

                        getLastLocation();


                    } catch (ApiException e) {

                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(GpsLocationActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException ex) {
                                    ex.printStackTrace();
                                }
                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                //Device does not have location
                                break;
                        }
                    }
                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            switch (resultCode) {
                case Activity.RESULT_OK:
//                         Toast.makeText(this, "GPS is tured on", Toast.LENGTH_SHORT).show();
                    Log.i("tag_what","gps on"+Activity.RESULT_OK);
                    gpsstatuas="on";

                    getLastLocation();


                case Activity.RESULT_CANCELED:
                    Log.i("tag_what","gps get result"+Activity.RESULT_OK);
                    Toast.makeText(getBaseContext(), "Please wait....we waiting for gps signal", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(this, "GPS required to be tured on", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void gotoActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GpsLocationActivity.this, MainActivity1.class));
                System.exit(0);
                finish();

            }
        }, 700);

    }


    private void getLastLocation() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(GpsLocationActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            gotoActivity();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else
                    {
                        getLastLocation();
                    }
                }
            });
        } else {
            askPermission();
        }

    }



    private void askPermission() {
        ActivityCompat.requestPermissions(GpsLocationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

    }
}