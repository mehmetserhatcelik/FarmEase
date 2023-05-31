package com.example.farmease;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.farmease.Adapters.MyAdapter;
import com.example.farmease.Adapters.StockMarketAdapter;
import com.example.farmease.Adapters.WeatherAdapter;
import com.example.farmease.databinding.ActivityMainScreenBinding;
import com.example.farmease.databinding.ActivityWeatherScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WeatherScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityWeatherScreenBinding binding;
    WeatherAdapter weatherAdapter;
    ArrayList<Field> list;
    ProgressDialog progressDialog;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    CollectionReference database;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = findViewById(R.id.recyclerView2);

        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();

        recyclerView = binding.recyclerView2;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(WeatherScreen.this, list );

        recyclerView.setAdapter(weatherAdapter);


        EventChangeListener();

    }
    public void EventChangeListener()
    {
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        database = fstore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Fields");
        database.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                list.add(new Field(document.getId(),((Double) document.get("Latitude")),((Double)document.get("Longitude")),document.get("City").toString(),document.get("fieldName").toString()));
                            }
                            weatherAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(WeatherScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void back(View view)
    {
        Intent intent = new Intent(WeatherScreen.this,MainScreen.class);
        startActivity(intent);
        finish();
    }
}

