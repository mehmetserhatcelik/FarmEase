package com.example.farmease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.farmease.MainFunction.Cities;
import com.example.farmease.MainFunction.City;
import com.example.farmease.databinding.ActivityFieldInfoBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FieldInfo extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityFieldInfoBinding binding;
    private GoogleMap mMap;
    double longitude;
    double latitude;
    String fieldName;
    String cityName;
    private ArrayList<City> cities;
    City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFieldInfoBinding.inflate(getLayoutInflater());

        cities = new Cities();

        for (int i = 0; i < cities.size(); i++) {
            if(cities.get(i).getName().equals(cityName))
            {
                city = cities.get(i);
            }
        }
        latitude = getIntent().getDoubleExtra("Latitude",0);
        longitude = getIntent().getDoubleExtra("Longitude",0);
        fieldName = getIntent().getStringExtra("Field Name");
        cityName = getIntent().getStringExtra("City Name");
        System.out.println(latitude);


        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng fieldpos = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(fieldpos).title("Marker in "+fieldName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fieldpos));
    }
    public void back(View view)
    {
        Intent intent = new Intent(FieldInfo.this, Fields.class);
        startActivity(intent);
        finish();
    }
    public void Suggest(View view)
    {
        Intent intent = new Intent(FieldInfo.this, Suggestion.class);
        System.out.println(cityName);
        intent.putExtra("City Name",cityName);
        startActivity(intent);
        finish();
    }
}