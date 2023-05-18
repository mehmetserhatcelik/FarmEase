package com.example.farmease;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.farmease.MainFunction.Cities;
import com.example.farmease.MainFunction.City;
import com.example.farmease.databinding.ActivityRegisterBinding;
import com.example.farmease.databinding.ActivitySuggestionBinding;

public class Suggestion extends AppCompatActivity {

    String cityName;
    City city;
    Cities cities;
    String suggestions;
    private ActivitySuggestionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuggestionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        suggestions ="";


        cities = new Cities();
        cityName = getIntent().getStringExtra("City Name");
        System.out.println(cities.size());
        System.out.println(cityName);

        for (int i = 0; i < cities.size(); i++) {

            if(cities.get(i).getName().equals(cityName))
            {
                city = cities.get(i);
            }
        }
        System.out.println(suggestions+"asd");
        for (int i = 0; i < city.getGoods().size(); i++) {
            suggestions = suggestions + city.getGoods().get(i)+"\n";
        }

        binding.suggesteds.setText(suggestions);
    }
}