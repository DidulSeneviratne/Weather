package com.izone.globalweather.air_quality;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izone.globalweather.R;
import com.izone.globalweather.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<CityList> cityList;
    String status;
    int meterValue;
    private ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_CODE = 101;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (isMyLocationGranted()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            requestLocationPermission();
        }

        adddata();
        for (CityList city : cityList) {
            double latitude = city.getLat();
            double longitude = city.getLon();
            String name = city.getCity();

            getwederdata(latitude, longitude, name);
        }


        //bottomnavigationbar(3);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

    }


    private void getwederdata(double lat, double lon, String city) {


        String appid = "9752bcab22549752ebb0a568e96eb9cd";


        double finalLon = lon;
        double finalLat = lat;
        RetrofitClient.getRetrofitClient().getdata(lon, lat, "9752bcab22549752ebb0a568e96eb9cd").enqueue(new Callback<PollutionModel>() {
            BitmapDescriptor bitmapDescriptor;

            @Override
            public void onResponse(Call<PollutionModel> call, Response<PollutionModel> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<PollutionModel.list> Coorddata = response.body().getEntries();


                    for (PollutionModel.list quiz : Coorddata) {

                        meterValue = quiz.getMain().getAqi();
                        Log.i("meter value", "data:- " + quiz.getMain().getAqi());


                        switch (meterValue) {

                            case 1:
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red5);
                                status = "Good";
                                break;
                            case 2:
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red4);
                                status = "Fair";
                                break;

                            case 3:
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red3);
                                status = "Moderate";
                                break;
                            case 4:
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red2);
                                status = "Poor";
                                break;
                            case 5:
                                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.red1);
                                status = "Very Poor";
                                break;
                        }


                        Marker melbourne = mMap.addMarker(
                                new MarkerOptions()
                                        .position(new LatLng(finalLat, finalLon))
                                        .title(city)
                                        .snippet("Air:" + status)
                                        .icon(bitmapDescriptor));


                    }
//                    PollutionModel.coord Coorddata2 = response.body().getCoord();
                }
            }

            @Override
            public void onFailure(Call<PollutionModel> call, Throwable t) {
                Log.i("List1", "error");
            }
        });


    }


    public boolean isMyLocationGranted() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //request to enable location permission
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }


    void adddata() {
//        30.5928° N, 114.3052° E
//
        cityList = new ArrayList<>();

        cityList.add(new CityList(18.7883, 98.9853, "Chiang Mai, Thailand"));
        cityList.add(new CityList(25.2048, 55.2708, "Dubai, United Arab Emirates"));
        cityList.add(new CityList(16.8409, 96.1735, "Yangon, Myanmar"));
        cityList.add(new CityList(13.7563, 100.5018, "Bangkok, Thailand"));
        cityList.add(new CityList(27.7172, 85.3240, "Kathmandu, Nepal"));
        cityList.add(new CityList(24.8607, 67.0011, "Karachi, Pakistan, Nepal"));
        cityList.add(new CityList(37.5665, 126.9780, "Seoul, South Korea"));
        cityList.add(new CityList(1.3521, 103.8198, "Singapore, Singapore"));
        cityList.add(new CityList(45.4642, 9.1900, "Milano, Italy"));
        cityList.add(new CityList(41.9028, 12.4964, "Rome, Italy"));
        cityList.add(new CityList(45.8150, 15.9819, "Zagreb, Croatia"));
        cityList.add(new CityList(39.9042, 116.4074, "Beijing, China"));
        cityList.add(new CityList(5.6037, -0.1870, "Accra, Ghana"));
        cityList.add(new CityList(19.4326, -99.1332, "Mexico City, Mexico"));
        cityList.add(new CityList(43.6532, -79.3832, "Toronto, Canada"));
        cityList.add(new CityList(42.6977, 23.3219, "Sofia, Bulgaria"));
        cityList.add(new CityList(22.3193, 114.1694, "Hong Kong, Hong Kong SAR"));
        cityList.add(new CityList(42.6629, 21.1655, "Pristina, Kosovo"));
        cityList.add(new CityList(47.8864, 106.9057, "Ulaanbaatar, Mongolia"));
        cityList.add(new CityList(-33.4489, -70.6693, "Santiago, Chile"));
        cityList.add(new CityList(41.9965, 21.4314, "Skopje, North Macedonia"));
        cityList.add(new CityList(25.0330, 121.5654, "Taipei, Taiwan"));
        cityList.add(new CityList(22.6206, 120.3129, "Kaohsiung, Taiwan"));
        cityList.add(new CityList(42.3314, -83.0458, "Detroit, USA"));
        cityList.add(new CityList(35.0116, 135.7680, "Kyoto, Japan"));
        cityList.add(new CityList(22.5431, 114.0579, "Shenzhen, China"));
        cityList.add(new CityList(36.7529, 3.0420, "Algiers, Algeria"));
        cityList.add(new CityList(43.7102, 7.2620, "Nice, France"));
        cityList.add(new CityList(30.5928, 114.3052, "Wuhan, China"));
        cityList.add(new CityList(37.5665, 126.9780, "Seoul, South Korea"));
        cityList.add(new CityList(1.3521, 103.8198, "Singapore, Singapore"));
        cityList.add(new CityList(45.4642, 9.1900, "Milano, Italy"));
        cityList.add(new CityList(41.9028, 12.4964, "Rome, Italy"));
        cityList.add(new CityList(45.8150, 15.9819, "Zagreb, Croatia"));
        cityList.add(new CityList(39.9042, 116.4074, "Beijing, China"));
        cityList.add(new CityList(5.6037, -0.1870, "Accra, Ghana"));
        cityList.add(new CityList(19.4326, -99.1332, "Mexico City, Mexico"));
        cityList.add(new CityList(43.6532, -79.3832, "Toronto, Canada"));
        cityList.add(new CityList(42.6977, 23.3219, "Sofia, Bulgaria"));
        cityList.add(new CityList(22.3193, 114.1694, "Hong Kong, Hong Kong SAR"));
        cityList.add(new CityList(42.6629, 21.1655, "Pristina, Kosovo"));
        cityList.add(new CityList(47.9183, 106.9178, "Ulaanbaatar, Mongolia"));
        cityList.add(new CityList(-33.4489, -70.6693, "Santiago, Chile"));
        cityList.add(new CityList(42.0038, 21.4522, "Skopje, North Macedonia"));
        cityList.add(new CityList(25.0330, 121.5654, "Taipei, Taiwan"));
        cityList.add(new CityList(22.6206, 120.3129, "Kaohsiung, Taiwan"));
        cityList.add(new CityList(42.3314, -83.0458, "Detroit, USA"));
        cityList.add(new CityList(35.0116, 135.7680, "Kyoto, Japan"));
        cityList.add(new CityList(22.5470, 114.0852, "Shenzhen, China"));
        cityList.add(new CityList(36.7529, 3.0420, "Algiers, Algeria"));
        cityList.add(new CityList(43.7034, 7.2663, "Nice, France"));
        cityList.add(new CityList(40.4168, -3.7038, "Madrid, Spain"));
        cityList.add(new CityList(50.0647, 19.9450, "Krakow, Poland"));
        cityList.add(new CityList(35.6895, 139.6917, "Tokyo, Japan"));
        cityList.add(new CityList(24.7136, 46.6753, "Riyadh, Saudi Arabia"));
        cityList.add(new CityList(50.4501, 30.5234, "Kyiv, Ukraine"));
        cityList.add(new CityList(35.1796, 129.0756, "Busan, South Korea"));
        cityList.add(new CityList(-6.2146, 106.8451, "Jakarta, Indonesia"));
        cityList.add(new CityList(40.7128, -74.0060, "New York City, USA"));
        cityList.add(new CityList(-33.8688, 151.2093, "Sydney, Australia"));
        cityList.add(new CityList(-37.8136, 144.9631, "Melbourne, Australia"));
        cityList.add(new CityList(-27.4698, 153.0251, "Brisbane, Australia"));
        cityList.add(new CityList(-31.9535, 115.8570, "Perth, Australia"));
        cityList.add(new CityList(-35.2809, 149.1300, "Canberra, Australia"));
        cityList.add(new CityList(-34.9285, 138.6007, "Adelaide, Australia"));
        cityList.add(new CityList(-12.4628, 130.8417, "Darwin, Australia"));
        cityList.add(new CityList(-37.8136, 144.9631, "Melbourne, Australia"));
        cityList.add(new CityList(6.9271, 79.8612, "Colombo, Sri Lanka"));
        cityList.add(new CityList(7.2906, 80.6337, "Kandy, Sri Lanka"));
        cityList.add(new CityList(6.0535, 80.2204, "Galle, Sri Lanka"));
        cityList.add(new CityList(9.6612, 80.0255, "Trincomalee, Sri Lanka"));
        cityList.add(new CityList(6.7980, 79.9025, "Negombo, Sri Lanka"));
        cityList.add(new CityList(6.0540, 80.2112, "Unawatuna, Sri Lanka"));
        cityList.add(new CityList(8.5710, 81.2330, "Batticaloa, Sri Lanka"));
        cityList.add(new CityList(6.9059, 79.9725, "Mount Lavinia, Sri Lanka"));
        cityList.add(new CityList(7.8731, 80.7894, "Anuradhapura, Sri Lanka"));
        cityList.add(new CityList(28.7041, 77.1025, "New Delhi, India"));
        cityList.add(new CityList(19.0760, 72.8777, "Mumbai, India"));
        cityList.add(new CityList(12.9716, 77.5946, "Bengaluru, India"));
        cityList.add(new CityList(22.5726, 88.3639, "Kolkata, India"));
        cityList.add(new CityList(13.0827, 80.2707, "Chennai, India"));
        cityList.add(new CityList(17.3850, 78.4867, "Hyderabad, India"));
        cityList.add(new CityList(23.2599, 77.4126, "Bhopal, India"));
        cityList.add(new CityList(26.9124, 75.7873, "Jaipur, India"));
    }

    /*void bottomnavigationbar(int id) {
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_location_city_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_map_24));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getBaseContext(), CityActivity2.class));
                                finish();
                            }
                        }, 0);

                        break;
                    case 2:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                finish();
                            }
                        }, 0);
                        break;

                    case 3:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getBaseContext(), MapsActivity.class));
                                finish();
                            }
                        }, 0);
                        break;

                }
                // your codes
            }
        });


        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });
        bottomNavigation.show(id, true);

    }*/
}