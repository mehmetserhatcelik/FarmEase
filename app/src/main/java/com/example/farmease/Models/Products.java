package com.example.farmease.Models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Policy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Products extends ArrayList<Product> {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    public Products()
    {
        add(new Product("Potato","2023-3-15","2023-6-15",1));
        add(new Product("Wheat","2022-11-1","2023-7-1",2));
    }
    public void setCalendar(String selectedDate, String event)
    {
            CollectionReference cf = fstore.collection("Users").document(user.getUid()).collection("Events");
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("Event",event);
            cf.document(selectedDate).set(eventInfo);
    }
}
