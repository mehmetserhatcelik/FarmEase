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
import android.widget.Toast;

import com.example.farmease.Adapters.MyAdapter;
import com.example.farmease.databinding.ActivityFieldsBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Fields extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private FirebaseFirestore fstore;
    private CollectionReference database;
    private MyAdapter myAdapter;
    private FirebaseAuth auth;
    private ArrayList<Field> list;
    private ProgressDialog progressDialog;
    String uid;


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


        uid = getIntent().getStringExtra("Uid");
        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new MyAdapter (Fields.this, list,this );

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();



    }
    public void EventChangeListener()
    {
        fstore = FirebaseFirestore.getInstance();

        database = fstore.collection("Users").document(uid).collection("Fields");
        database.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                list.add(new Field(document.getId(),((Double) document.get("Latitude")),((Double)document.get("Longitude")),document.get("City").toString(),document.get("fieldName").toString()));
                            }
                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(Fields.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void back(View view)
    {
        Intent intent = new Intent(Fields.this, AdminPanel.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int pos) {

        Intent intent = new Intent(Fields.this, FieldInfoForEngineers.class);
        intent.putExtra("FieldUid", list.get(pos).getuid());
        System.out.println(list.get(pos).getuid());
        intent.putExtra("Uid",uid);
        startActivity(intent);
        finish();
    }

}