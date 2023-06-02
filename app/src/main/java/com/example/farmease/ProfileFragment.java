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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        ImageView profileIcon = view.findViewById(R.id.imageProfile);
        profileIcon.setVisibility(View.INVISIBLE);
        TextView usernameText = view.findViewById(R.id.userName);
        usernameText.setVisibility(View.INVISIBLE);
        super.onViewCreated(view, savedInstanceState);
        getImage(view);
        getUsername(view);


        view.findViewById(R.id.logOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setSwitchState(true);
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

    private void getUsername(View view){
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if(error!=null)
                Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG);
            if(value!=null){
               Map<String,Object> data = value.getData();
               userName = (String) data.get("FullName");
                TextView usernameText = view.findViewById(R.id.userName);
                usernameText.setText(userName);
                usernameText.setVisibility(View.VISIBLE);
            }
        });
    }
    private void getImage(View view){
        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if(error!=null) {
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            if(value!=null){
                Map<String,Object> data = value.getData();
                String downloadUrl = (String) data.get("icon");
                ImageView profileIcon = view.findViewById(R.id.imageProfile);
                Picasso.get().load(downloadUrl).into(profileIcon);
                profileIcon.setVisibility(View.VISIBLE);
            }
        });
    }



}