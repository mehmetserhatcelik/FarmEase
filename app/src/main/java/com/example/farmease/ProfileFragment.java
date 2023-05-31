package com.example.farmease;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.farmease.profilesettings.ProfileSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;


public class ProfileFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String userName;
    String downloadUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        getImage();
        getUsername();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView usernameText = view.findViewById(R.id.userName);
        ImageView profileIcon = view.findViewById(R.id.imageProfile);
        usernameText.setText(userName);
        Picasso.get().load(downloadUrl).into(profileIcon);
        view.findViewById(R.id.logOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(view);
            }
        });
        view.findViewById(R.id.textViewChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword(view);
            }
        });
        view.findViewById(R.id.textViewChangeIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeIcon(view);
            }
        });
        view.findViewById(R.id.textViewChangeEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail(view);
            }
        });


    }
    public void logOut(View view){
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void changePassword(View view){
        Intent intent = new Intent(this.getContext(), ProfileSettings.class);
        intent.putExtra("setting","password");
        startActivity(intent);
    }
    public void changeIcon(View view) {
        Intent intent = new Intent(this.getContext(), ProfileSettings.class);
        intent.putExtra("setting","icon");
        startActivity(intent);
    }
    private void changeEmail(View view) {
        Intent intent = new Intent(this.getContext(), ProfileSettings.class);
        intent.putExtra("setting","email");
        startActivity(intent);
    }

    private void getUsername(){
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if(error!=null)
                Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG);
            if(value!=null){
               Map<String,Object> data = value.getData();
               userName = (String) data.get("FullName");
            }
        });
    }
    private void getImage(){
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if(error!=null)
                Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG);
            if(value!=null){
                Map<String,Object> data = value.getData();
                downloadUrl = (String) data.get("icon");
            }
        });
    }



}