package com.example.farmease;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.farmease.databinding.ActivityAddFieldBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddField extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private FirebaseFirestore fstore;
    private FirebaseAuth auth;
    private ActivityAddFieldBinding binding;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private double longitude;
    private double latitude;
    private String cityName;


    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityAddFieldBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        registerLauncher();
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void addField(View view)
    {

        FirebaseUser user = auth.getCurrentUser();
        CollectionReference cf = fstore.collection("Users").document(user.getUid()).collection("Fields");

        Map<String,Object> fieldInfo = new HashMap<>();
        fieldInfo.put("Field Name",binding.fieldName.getText().toString());
        fieldInfo.put("pH",binding.pH.getText().toString());
        fieldInfo.put("Organic Substance",binding.OrganicSubctance.getText().toString());
        fieldInfo.put("Nitrogen",binding.Nitrogen.getText().toString());
        fieldInfo.put("Phosphorus",binding.Phosphorus.getText().toString());
        fieldInfo.put("Potassium",binding.Potassium.getText().toString());
        fieldInfo.put("Soil Type",binding.soilType.getText().toString());
        fieldInfo.put("Lime (CaCO3)",binding.Lime.getText().toString());
        fieldInfo.put("Calcium",binding.Calsium.getText().toString());
        fieldInfo.put("Magnesium",binding.Magnesium.getText().toString());
        fieldInfo.put("Manganese",binding.Manganese.getText().toString());
        fieldInfo.put("Zinc",binding.Zinc.getText().toString());
        fieldInfo.put("Boron",binding.Boron.getText().toString());
        fieldInfo.put("Iron",binding.Ferrum.getText().toString());
        fieldInfo.put("Copper",binding.Copper.getText().toString());
        fieldInfo.put("Salt",binding.Salt.getText().toString());
        fieldInfo.put("City",binding);
        fieldInfo.put("Latitude",latitude);
        fieldInfo.put("Longitude",longitude);
        cf.add(fieldInfo);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                SharedPreferences sharedPreferences = AddField.this.getSharedPreferences("com.example.farmease",MODE_PRIVATE);
                boolean info = sharedPreferences.getBoolean("info",false);
                if(!info) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    sharedPreferences.edit().putBoolean("info",true).apply();
                }
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.getRoot(), "Permission needed for maps.", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }).show();
            }
            else
            {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastLocation != null)
            {
                LatLng userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
            }
            mMap.setMyLocationEnabled(true);
        }
    }
    private void registerLauncher()
    {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result)
                {
                    if(ContextCompat.checkSelfPermission(AddField.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(lastLocation != null)
                        {
                            LatLng userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                        }
                    }
                }
                else
                {
                    Toast.makeText(AddField.this, "Permission needed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;

        Geocoder geocoder  = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude, 1);
            System.out.println(addressList.get(0).getAddressLine(0));
            cityName = addressList.get(0).getAdminArea();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        System.out.println(latitude+"    "+ longitude);
        mMap.addMarker(new MarkerOptions().position(latLng));
    }
    public void back(View view)
    {
        Intent intent = new Intent(AddField.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}