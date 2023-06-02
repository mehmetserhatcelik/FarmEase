package com.example.farmease;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmease.Adapters.EmptyAdapter;
import com.example.farmease.Adapters.MyAdapter;
import com.example.farmease.Adapters.StockMarketAdapter;
import com.example.farmease.Models.Good;
import com.example.farmease.databinding.ActivityMainScreenBinding;
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
import java.util.Map;


public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private RecyclerView recyclerView2;
    private StockMarketAdapter stockMarketAdapter;
    private ArrayList<Good> goods;
    private ActivityMainScreenBinding binding;

    private ArrayList<Field> list;
    private FirebaseFirestore fstore;
    private CollectionReference database;
    private FirebaseAuth auth;
    private TextView textView;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    private MyAdapter myAdapter;
    private String temp;
    private ImageView imageView;
    private DecimalFormat df = new DecimalFormat("#.##");
    private EmptyAdapter emptyAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityMainScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        textView = binding.textView4;
        recyclerView2 = view.findViewById(R.id.recView);
        imageView = binding.asas;
        goods = new ArrayList<>();
        loadJson();
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        stockMarketAdapter = new StockMarketAdapter(goods);
        recyclerView2.setAdapter(stockMarketAdapter);

        list = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(), list, this);
        emptyAdapter = new EmptyAdapter(getActivity(), new RecyclerViewInterface() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getActivity(), AddField.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setSwitchState(true);
                logOut(v);

            }
        });
        binding.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(v);
            }
        });
        binding.Borsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStockMarket(v);
            }
        });
        binding.Weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeather(v);
            }
        });
        EventChangeListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView profileIcon = view.findViewById(R.id.asas);
        profileIcon.setVisibility(View.INVISIBLE);
        getImage(view);
    }

    private void EventChangeListener() {
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        temp = "Hello, ";

        fstore.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        temp += documentSnapshot.getString("FullName");
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
        database.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(new Field(document.getId(), ((Double) document.get("Latitude")), ((Double) document.get("Longitude")), document.get("City").toString(), document.get("fieldName").toString()));
                    }
                    if (list.isEmpty()) {
                        recyclerView.setAdapter(emptyAdapter);
                    } else {
                        recyclerView.setAdapter(myAdapter);
                    }
                    myAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadJson() {
        try {
            InputStream inputStream = getActivity().getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            int max = jsonArray.length();

            String name, price;

            for (int i = 0; i < max; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.getString("currency");
                price = jsonObject.getString("price");

                goods.add(new Good(name, price));
                System.out.println(goods.size());
            }
        } catch (Exception e) {
            Log.e("Tag", "loadJson: error " + e);
        }
    }

    public void logOut(View view) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void showStockMarket(View view)
    {
        Intent intent = new Intent(getActivity(), StockMarket.class);
        startActivity(intent);
        getActivity().finish();
    }


    public void showWeather(View view) {
        Intent intent = new Intent(getActivity(), WeatherScreen.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void showCalendar(View view) {
        Intent intent = new Intent(getActivity(), Calendar.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), FieldInfo.class);
        intent.putExtra("Uid", list.get(pos).getuid());
        startActivity(intent);
        getActivity().finish();
    }

    private void getImage(View view){
        fstore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if(error!=null)
                Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG);
            if(value!=null){
                Map<String,Object> data = value.getData();
                String downloadUrl = (String) data.get("icon");
                ImageView profileIcon = view.findViewById(R.id.asas);
                Picasso.get().load(downloadUrl).into(profileIcon);
                profileIcon.setVisibility(View.VISIBLE);
            }
        });
    }
}
