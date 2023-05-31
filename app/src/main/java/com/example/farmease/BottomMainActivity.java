package com.example.farmease;


import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.farmease.HomeFragment;
import com.example.farmease.MessagesFragment;
import com.example.farmease.ProfileFragment;
import com.example.farmease.databinding.ActivityBottomMainBinding;


public class BottomMainActivity extends AppCompatActivity {
    ActivityBottomMainBinding binding;
    HomeFragment homeFragment;
    ChatPageForUsers ChatPageForUsers;
    GuideFragment guideFragment;
    ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding=ActivityBottomMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        homeFragment = new HomeFragment();
        ChatPageForUsers = new ChatPageForUsers();
        guideFragment = new GuideFragment();
        profileFragment = new ProfileFragment();
        replaceFragment(homeFragment);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homeItem:
                    replaceFragment(homeFragment);
                    break;
                case R.id.messageItem:
                    replaceFragment(ChatPageForUsers);
                    break;
                case R.id.guideItem:
                    replaceFragment(guideFragment);
                    break;
                case R.id.profileItem:
                    replaceFragment(profileFragment);
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