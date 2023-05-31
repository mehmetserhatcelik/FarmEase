package com.example.farmease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.farmease.AdminPanelPackage.AdminHomeFragment;
import com.example.farmease.AdminPanelPackage.AdminprofileFragment;
import com.example.farmease.AdminPanelPackage.StockMarketFragment;
import com.example.farmease.databinding.ActivityAdminPanelBinding;
import com.example.farmease.databinding.ActivityBottomMainBinding;

public class AdminPanel extends AppCompatActivity {
    private ActivityAdminPanelBinding binding;
    AdminprofileFragment adminprofileFragment;
    ChatPageForEngineers chatPageForEngineers;
    StockMarketFragment stockMarketFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adminprofileFragment=new AdminprofileFragment();
        chatPageForEngineers = new ChatPageForEngineers();
        stockMarketFragment=new StockMarketFragment();
        replaceFragment(chatPageForEngineers);
        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeItem:
                    replaceFragment(chatPageForEngineers);
                    break;
                case R.id.stockMarketItem:
                    replaceFragment(stockMarketFragment);
                    break;
                case R.id.profileItem:
                    replaceFragment(adminprofileFragment);
                    break;
            }
            return true;
        });

    }
    public void replaceFragment(Fragment fragment){
        FragmentManager manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }
}