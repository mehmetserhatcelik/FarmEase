
package com.example.farmease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class Calendar extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore fstore;


    CalendarView calendarView;

    String selectedDate;
    String eventDetails;



    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        fstore = FirebaseFirestore.getInstance();
        calendarView = findViewById(R.id.calendarView);
        selectedDate = "";
        eventDetails = "";

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, BottomMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate =  year + "-" + (month+1)+ "-" + dayOfMonth;
                openDialog();
            }
        });
    }
    public void openDialog()
    {
        CalendarDialog calendarDialog = new CalendarDialog(this,selectedDate);
        calendarDialog.show(getSupportFragmentManager(),"Calendar Dialog");
    }



}