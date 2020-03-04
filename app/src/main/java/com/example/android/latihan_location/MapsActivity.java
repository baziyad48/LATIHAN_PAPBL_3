package com.example.android.latihan_location;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnSuccessListener<Location> {

    private GoogleMap mMap;
    private double LATITUDE, LONGITUDE;


    Button mylocation;
    private FusedLocationProviderClient fusedLocationClient;
    Task<Location> lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mylocation = findViewById(R.id.mylocation);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastLocation = fusedLocationClient.getLastLocation();
                lastLocation.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null)
                        {
                            LATITUDE = location.getLatitude();
                            LONGITUDE = location.getLongitude();

                            Toast.makeText(getBaseContext(),"" + LATITUDE + " , " + LONGITUDE,Toast.LENGTH_SHORT).show();

                            if(mMap != null) {
                                LatLng target = new LatLng(LATITUDE, LONGITUDE);
                                mMap.addMarker(new MarkerOptions().position(target));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                            }

                        }
                    }
                });
            }
        });

        fusedLocationClient = getFusedLocationProviderClient(this);
        createLocationRequest();

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Add Polygon
        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(-7.952916, 112.614468), new LatLng(-7.953032, 112.615550), new LatLng(-7.954886, 112.615468), new LatLng(-7.954870, 112.613558))
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
    }


    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                                LATITUDE = locationResult.getLastLocation().getLatitude();
                                LONGITUDE = locationResult.getLastLocation().getLongitude();
                    }
                },
                Looper.myLooper());
    }

    @Override
    public void onSuccess(Location location) {

    }
}
