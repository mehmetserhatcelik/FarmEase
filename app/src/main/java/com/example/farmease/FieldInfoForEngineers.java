package com.example.farmease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.farmease.MainFunction.Fertilizer;
import com.example.farmease.Models.WorkaroundMapFragment;
import com.example.farmease.databinding.ActivityFieldInfoForEngineersBinding;
import com.example.farmease.databinding.ActivityMainScreenBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.farmease.databinding.ActivityFieldInfoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FieldInfoForEngineers extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String cityName;
    private ActivityFieldInfoForEngineersBinding binding;
    private FirebaseUser user;
    private String uid;
    private FirebaseFirestore fstore;
    private DocumentReference database;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private double latitude;
    private double longitude;
    EditText etfieldName;
    EditText etpH;
    EditText etOrganicSubstance;
    EditText etNitrogen;
    EditText etPhosphorus;
    EditText etPotassium;
    EditText etSoilType;
    EditText etLime;
    EditText etCalcium;
    EditText etMagnesium;
    EditText etManganese;
    EditText etSalt;
    EditText etZinc;
    EditText etBoron;
    EditText etIron;
    EditText etCopper;
    ScrollView sc5;
    int i ;
    Button button6;
    String fieldUid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFieldInfoForEngineersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        i=0;

        etfieldName = binding.fieldName;
        etpH = binding.pH;
        etOrganicSubstance = binding.OrganicSubctance;
        etNitrogen = binding.Nitrogen;
        etPhosphorus = binding.Phosphorus;
        etPotassium = binding.Potassium;
        etLime = binding.Lime;
        etSoilType = binding.soilType;
        etBoron=binding.Boron;
        etCalcium = binding.Calsium;
        etCopper = binding.Copper;
        etIron = binding.Ferrum;
        etZinc = binding.Zinc;
        etMagnesium = binding.Magnesium;
        etManganese = binding.Manganese;
        etSalt = binding.Salt;
        sc5=binding.sc5;


        etfieldName.setEnabled(false);
        etpH.setEnabled(false);
        etOrganicSubstance.setEnabled(false);
        etNitrogen.setEnabled(false);
        etPhosphorus.setEnabled(false);
        etPotassium.setEnabled(false);
        etSoilType.setEnabled(false);
        etLime.setEnabled(false);
        etCalcium.setEnabled(false);
        etManganese.setEnabled(false);
        etSalt.setEnabled(false);
        etZinc.setEnabled(false);
        etBoron.setEnabled(false);
        etCopper.setEnabled(false);
        etIron.setEnabled(false);
        etMagnesium.setEnabled(false);

        longitude = 0;
        latitude = 0;


        uid = getIntent().getStringExtra("Uid");

        fieldUid = getIntent().getStringExtra("FieldUid");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();
        loadData();

        button6 = binding.button6;

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FieldInfoForEngineers.this, AdminPanel.class);
                startActivity(intent);
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                sc5.requestDisallowInterceptTouchEvent(true);
            }
        });
        mapFragment.getMapAsync(this);
    }
    private void loadData()
    {
        fstore = FirebaseFirestore.getInstance();


        database = fstore.collection("Users").document(uid).collection("Fields").document(fieldUid);
        database.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String FieldName = documentSnapshot.getString("fieldName");
                    etfieldName.setText(FieldName);
                    String ph = documentSnapshot.getString("pH");
                    etpH.setText(ph);
                    String organic = documentSnapshot.getString("Organic Substance");
                    etOrganicSubstance.setText(organic);
                    String nitrogen = documentSnapshot.getString("Nitrogen");
                    etNitrogen.setText(nitrogen);
                    String phos = documentSnapshot.getString("Phosphorus");
                    etPhosphorus.setText(phos);
                    String potas = documentSnapshot.getString("Potassium");
                    etPotassium.setText(potas);
                    String lime = documentSnapshot.getString("Lime (CaCO3)");
                    etLime.setText(lime);
                    String soilType = documentSnapshot.getString("Soil Type");
                    etSoilType.setText(soilType);
                    String salt = documentSnapshot.getString("Salt");
                    etSalt.setText(salt);
                    String mangan = documentSnapshot.getString("Manganese");
                    etManganese.setText(mangan);
                    String magnes = documentSnapshot.getString("Magnesium");
                    etMagnesium.setText(magnes);
                    String zinc = documentSnapshot.getString("Zinc");
                    etZinc.setText(zinc);
                    String iron = documentSnapshot.getString("Iron");
                    etIron.setText(iron);
                    String boron = documentSnapshot.getString("Boron");
                    etBoron.setText(boron);
                    String copper = documentSnapshot.getString("Copper");
                    etCopper.setText(copper);
                    String calsium = documentSnapshot.getString("Calcium");
                    etCalcium.setText(calsium);
                    System.out.println(documentSnapshot.getDouble("Longitude")+10);
                    longitude = documentSnapshot.getDouble("Longitude");
                    latitude = documentSnapshot.getDouble("Latitude");

                    LatLng field = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(field).title("Marker in "+etfieldName.getText().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(field, 15));


                }else {
                    Toast.makeText(FieldInfoForEngineers.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                if(progressDialog.isShowing())
                    progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DATAFF",e.toString());
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng field = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(field).title("Marker in "+etfieldName.getText().toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(field, 8));
    }

    public void back(View view)
    {
        Intent intent = new Intent(FieldInfoForEngineers.this, BottomMainActivity.class);


        startActivity(intent);
        finish();
    }




}