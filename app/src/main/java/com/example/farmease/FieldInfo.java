package com.example.farmease;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.farmease.databinding.ActivityFieldInfoBinding;

public class FieldInfo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityFieldInfoBinding binding;
    private double longitude;
    private double latitude;
    private String cityName;
    private String fieldName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFieldInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        longitude = getIntent().getDoubleExtra("Longitude",0);
        latitude = getIntent().getDoubleExtra("Latitude",0);
        cityName = getIntent().getStringExtra("City Name");
        fieldName = getIntent().getStringExtra("Field Name");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng field = new LatLng(39.87002839200435, 32.75168716907501);
        mMap.addMarker(new MarkerOptions().position(field).title("Marker in "+fieldName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(field, 15));
    }
    public void back(View view)
    {
        Intent intent = new Intent(FieldInfo.this, Fields.class);


        startActivity(intent);
        finish();
    }
}