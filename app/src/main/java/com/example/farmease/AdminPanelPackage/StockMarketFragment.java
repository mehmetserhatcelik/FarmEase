package com.example.farmease.AdminPanelPackage;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.farmease.Models.Good;
import com.example.farmease.R;
import com.example.farmease.Adapters.StockMarketAdapter;
import com.example.farmease.databinding.FragmentStockMarketBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;




    public class StockMarketFragment extends Fragment {
        private RecyclerView recyclerView;
        private StockMarketAdapter stockMarketAdapter;
        private ArrayList<Good> goods;
        private FragmentStockMarketBinding binding;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = FragmentStockMarketBinding.inflate(inflater, container, false);
            View view = binding.getRoot();

            recyclerView = binding.recView6;

            goods = new ArrayList<>();
            loadJson();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            stockMarketAdapter = new StockMarketAdapter(goods);
            recyclerView.setAdapter(stockMarketAdapter);

            return view;
        }



        private void loadJson() {
            try {
                InputStream inputStream = requireActivity().getAssets().open("data.json");
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
    }



