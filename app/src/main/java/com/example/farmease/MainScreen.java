package com.example.farmease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmease.Adapters.EmptyAdapter;
import com.example.farmease.Adapters.MyAdapter;
import com.example.farmease.Adapters.StockMarketAdapter;
import com.example.farmease.Models.Good;
import com.example.farmease.databinding.ActivityMainScreenBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainScreen extends AppCompatActivity implements RecyclerViewInterface{

    RecyclerView recyclerView2;
    StockMarketAdapter stockMarketAdapter;
    ArrayList<Good> goods;
    private ActivityMainScreenBinding binding;

    ArrayList<Field> list;
    FirebaseFirestore fstore;
    CollectionReference database;
    private FirebaseAuth auth;
    TextView textView;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    MyAdapter myAdapter;
    String temp;
    ImageView imageView;
    DecimalFormat df = new DecimalFormat("#.##");
    EmptyAdapter emptyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        textView = binding.textView4;

        recyclerView2 = findViewById(R.id.recView);
        imageView = binding.asas;
        goods = new ArrayList<>();
        loadJson();
        recyclerView2.setLayoutManager(new LinearLayoutManager(MainScreen.this));
        stockMarketAdapter = new StockMarketAdapter(goods);
        recyclerView2.setAdapter(stockMarketAdapter);


        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();

        recyclerView = binding.recyclerView;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new MyAdapter(MainScreen.this, list,this );
        emptyAdapter = new EmptyAdapter(MainScreen.this, new RecyclerViewInterface() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(MainScreen.this,AddField.class);
                startActivity(intent);
                finish();
            }
        });



        EventChangeListener();


    }
    public void EventChangeListener()
    {
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        temp ="Hello, ";

        fstore.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot =task.getResult();
                            if(documentSnapshot != null && documentSnapshot.exists())
                            {
                                temp+=documentSnapshot.getString("FullName");
                                textView.setText(temp);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

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
                            if(list.isEmpty()){
                                recyclerView.setAdapter(emptyAdapter);
                            }else{
                                recyclerView.setAdapter(myAdapter);
                            }
                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(MainScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void loadJson()
    {
        try {

            InputStream inputStream =getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json ;
            int max;

            json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            max =jsonArray.length();

            String name, price;


            for (int i = 0; i < max; i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name =jsonObject.getString("currency");
                price =jsonObject.getString("price");

                goods.add(new Good(name,price));

            }


        }
        catch (Exception e)
        {
            Log.e("Tag","loadJson: error "+e);
        }
    }
    public void logOut(View view)
    {
        Intent intent = new Intent(MainScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void goMessages(View view)
    {
        Intent intent = new Intent(MainScreen.this, ChatPageForEngineers.class);
        startActivity(intent);
        finish();
    }
    public void goFields(View view)
    {
        Intent intent = new Intent(MainScreen.this, Fields.class);

        startActivity(intent);
        finish();
    }
    public void showWeather(View view)
    {
        Intent intent = new Intent(MainScreen.this, WeatherScreen.class);
        startActivity(intent);
        finish();
    }
    public void showCalendar(View view)
    {
        Intent intent = new Intent(MainScreen.this,Calendar.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(MainScreen.this, FieldInfo.class);
        intent.putExtra("Uid",list.get(pos).getuid());
        startActivity(intent);
        finish();
    }


}