package com.example.farmease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.farmease.MainFunction.Cities;
import com.example.farmease.MainFunction.City;
import com.example.farmease.MainFunction.Fertilizer;
import com.example.farmease.databinding.ActivityRegisterBinding;
import com.example.farmease.databinding.ActivitySuggestionBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Suggestion extends AppCompatActivity {

    String uid;
    City city;
    Cities cities;
    String suggestions;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    DocumentReference database;
    ProgressDialog progressDialog;
    private ActivitySuggestionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuggestionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        suggestions ="";
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();

        city = null;
        cities = new Cities();
        uid = getIntent().getStringExtra("Uid");
        loadData();



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
                    String cityName = documentSnapshot.getString("City");

                    for (int i = 0; i < cities.size(); i++) {

                        if(cities.get(i).getName().equals(cityName))
                        {
                            Toast.makeText(Suggestion.this,cities.get(i).getName(),Toast.LENGTH_SHORT);
                            city = cities.get(i);
                        }
                    }


                    for (int i = 0; i < city.getGoods().size(); i++) {
                        suggestions = suggestions + city.getGoods().get(i)+"\n"+"\n";
                    }

                    binding.suggesteds.setText(suggestions);

                    fstore = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();

                    database = fstore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Fields").document(uid);
                    database.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String FieldName = documentSnapshot.getString("fieldName");

                                String ph = documentSnapshot.getString("pH");

                                String organic = documentSnapshot.getString("Organic Substance");

                                String nitrogen = documentSnapshot.getString("Nitrogen");

                                String phos = documentSnapshot.getString("Phosphorus");

                                String potas = documentSnapshot.getString("Potassium");
                                String lime = documentSnapshot.getString("Lime (CaCO3)");

                                String soilType = documentSnapshot.getString("Soil Type");

                                String salt = documentSnapshot.getString("Salt");
                                String mangan = documentSnapshot.getString("Manganese");

                                String magnes = documentSnapshot.getString("Magnesium");

                                String zinc = documentSnapshot.getString("Zinc");

                                String iron = documentSnapshot.getString("Iron");

                                String boron = documentSnapshot.getString("Boron");

                                String copper = documentSnapshot.getString("Copper");

                                String calsium = documentSnapshot.getString("Calcium");


                                ArrayList<String> arr;
                                String temp = "";
                                Fertilizer fertilizer =  new Fertilizer();
                                arr = fertilizer.getSuggest(Double.valueOf(ph),Double.valueOf(salt) ,Double.valueOf(lime) ,Double.valueOf(organic) ,Double.valueOf(phos) ,Double.valueOf(potas),
                                        Double.valueOf(nitrogen),Double.valueOf(iron),Double.valueOf(calsium),Double.valueOf(magnes), Double.valueOf(mangan),
                                        Double.valueOf(zinc),Double.valueOf(boron) ,Double.valueOf(copper));

                                for (int j = 0; j < arr.size(); j++) {
                                    temp+= arr.get(j)+"\n"+"\n";
                                }
                                binding.suggesteds1.setText(temp);


                            }else {
                                Toast.makeText(Suggestion.this, "Failed", Toast.LENGTH_SHORT).show();
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

                }else {
                    Toast.makeText(Suggestion.this, "Failed", Toast.LENGTH_SHORT).show();
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
    public void back(View view)
    {
        Intent intent = new Intent(Suggestion.this, FieldInfo.class);
        intent.putExtra("Uid",uid);
        startActivity(intent);
        finish();
    }

}