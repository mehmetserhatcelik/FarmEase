package com.example.farmease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }
    public void logOut(View view)
    {
        Intent intent = new Intent(AdminPanel.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void goMessages(View view)
    {
        Intent intent = new Intent(AdminPanel.this, ChatPageForEngineers.class);
        startActivity(intent);
        finish();
    }
}