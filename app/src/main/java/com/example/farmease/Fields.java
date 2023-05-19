package com.example.farmease;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.farmease.databinding.ActivityAddFieldBinding;
import com.example.farmease.databinding.ActivityFieldsBinding;
import com.example.farmease.databinding.ActivityRegisterBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class Fields extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private MyAdapter myAdapter;
    private FirebaseAuth auth;
    private ArrayList<Field> list;
    private ProgressDialog progressDialog;


    private ActivityFieldsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFieldsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data..."  );
        progressDialog.show();

        recyclerView = binding.recyclerView;
        database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new MyAdapter (Fields.this, list,this );

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();



    }
    private void EventChangeListener()
    {
        FirebaseUser user = auth.getCurrentUser();
        database.collection("Users").document(user.getUid()).collection("Fields").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null)
                {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc : value.getDocumentChanges() ){
                    if(dc.getType() == DocumentChange.Type.ADDED)
                    {
                        list.add(dc.getDocument().toObject(Field.class));
                    }
                    myAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }
    public void back(View view)
    {
        Intent intent = new Intent(Fields.this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int pos) {

        Intent intent = new Intent(Fields.this, FieldInfo.class);

        intent.putExtra("Latitude",list.get(pos).getLatitude());
        intent.putExtra("Longitude",list.get(pos).getLongitude());
        intent.putExtra("Field Name",list.get(pos).getFieldName());
        intent.putExtra("City Name",list.get(pos).getCityName());

        startActivity(intent);
        finish();
    }
}