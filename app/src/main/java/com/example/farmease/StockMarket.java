package com.example.farmease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.farmease.Adapters.StockMarketAdapter;
import com.example.farmease.Models.Good;
import com.example.farmease.databinding.ActivityMainScreenBinding;
import com.example.farmease.databinding.ActivityStockMarketBinding;
import com.example.farmease.databinding.ActivitySuggestionBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class StockMarket extends AppCompatActivity {
    RecyclerView recyclerView;
    StockMarketAdapter stockMarketAdapter;
    ArrayList<Good> goods;
    private ActivityStockMarketBinding binding;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockMarketBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = binding.recView6;

        button = binding.button9;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockMarket.this,BottomMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        goods = new ArrayList<>();
        loadJson();
        recyclerView.setLayoutManager(new LinearLayoutManager(StockMarket.this));
        stockMarketAdapter = new StockMarketAdapter(goods);
        recyclerView.setAdapter(stockMarketAdapter);
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
                System.out.println(goods.size());

            }


        }
        catch (Exception e)
        {
            Log.e("Tag","loadJson: error "+e);
        }
    }
}