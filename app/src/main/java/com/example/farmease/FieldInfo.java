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
import android.widget.Toast;

import com.example.farmease.MainFunction.Fertilizer;
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

public class FieldInfo extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String cityName;
    private ActivityFieldInfoBinding binding;
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
    Button editButton;
    int i ;
    Button button6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFieldInfoBinding.inflate(getLayoutInflater());
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
        editButton = binding.editButton;

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();
        loadData();

        button6 = binding.button6;

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FieldInfo.this, BottomMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void loadData()
    {
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        database = fstore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Fields").document(uid);
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
                    System.out.println(longitude);
                    LatLng field = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(field).title("Marker in "+etfieldName.getText().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(field, 15));


                }else {
                    Toast.makeText(FieldInfo.this, "Failed", Toast.LENGTH_SHORT).show();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(field, 15));
    }
    public void Suggest(View view)
    {
        Intent intent = new Intent(FieldInfo.this, Suggestion.class);
        intent.putExtra("Uid",uid);
        startActivity(intent);
        finish();
    }
    public void back(View view)
    {
        Intent intent = new Intent(FieldInfo.this, BottomMainActivity.class);


        startActivity(intent);
        finish();
    }
    public void Edit(View view)
    {

        if(i%2==0) {
            etfieldName.setEnabled(true);
            etpH.setEnabled(true);
            etOrganicSubstance.setEnabled(true);
            etNitrogen.setEnabled(true);
            etPhosphorus.setEnabled(true);
            etPotassium.setEnabled(true);
            etSoilType.setEnabled(true);
            etLime.setEnabled(true);
            etCalcium.setEnabled(true);
            etManganese.setEnabled(true);
            etSalt.setEnabled(true);
            etZinc.setEnabled(true);
            etBoron.setEnabled(true);
            etCopper.setEnabled(true);
            etIron.setEnabled(true);
            etMagnesium.setEnabled(true);
            editButton.setText("Load Data");
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = auth.getCurrentUser();
                    DocumentReference documentReference = fstore.collection("Users").document(user.getUid()).collection("Fields").document(uid);
                    Map<String,Object> fieldInfo = new HashMap<>();
                    fieldInfo.put("fieldName",binding.fieldName.getText().toString());
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
                    documentReference.update(fieldInfo);
                    i++;

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
                    editButton.setText("Edit");

                    editButton.setOnClickListener(FieldInfo.this::Edit);
                }
            });


        }
        else {
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
            editButton.setText("Edit");
            i++;

        }


    }
    public void Remove(View view)
    {
        user = auth.getCurrentUser();
        DocumentReference documentReference = fstore.collection("Users").document(user.getUid()).collection("Fields").document(uid);
        documentReference.delete();

        Intent intent = new Intent(FieldInfo.this,BottomMainActivity.class);
        startActivity(intent);
        finish();

    }
}