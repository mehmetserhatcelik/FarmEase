package com.example.farmease;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.farmease.databinding.ActivityFieldsUBinding;
import com.example.farmease.databinding.ActivityMainScreenBinding;
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


public class GuideFragment extends Fragment implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private FirebaseFirestore fstore;
    private CollectionReference database;
    private MyAdapter myAdapter;
    private FirebaseAuth auth;
    private ArrayList<Field> list;
    private ProgressDialog progressDialog;
    private ActivityFieldsUBinding binding;

    private FrameLayout button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityFieldsUBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        button = binding.button2;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddField(v);
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        myAdapter = new MyAdapter(requireContext(), list, this);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

        return view;
    }

    public void EventChangeListener() {
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        database = fstore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Fields");
        database.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    list.add(new Field(document.getId(), ((Double) document.get("Latitude")), ((Double) document.get("Longitude")), document.get("City").toString(), document.get("fieldName").toString()));
                }
                myAdapter.notifyDataSetChanged();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), FieldInfo.class);
        intent.putExtra("Uid", list.get(pos).getuid());
        startActivity(intent);
        getActivity().finish();
    }
    public void goAddField(View view)
    {
        Intent intent = new Intent(getActivity(), AddField.class);
        startActivity(intent);
        getActivity().finish();
    }

}
