package com.example.farmease;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.farmease.Models.Image;
import com.example.farmease.Models.Images;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class CalendarDialog extends AppCompatDialogFragment {

    DocumentReference df;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView image;
    EditText event;
    String selectedDate;
    Context context;
    Button button;
    private Switch simpleSwitch;
    boolean isExist;


    public CalendarDialog(Context context, String selectedDate)
    {
        this.context = context;
        this.selectedDate = selectedDate;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calendar_panel, null);

        image = view.findViewById(R.id.eventImage);
        event = view.findViewById(R.id.Event);

        isExist=false;
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        button = view.findViewById(R.id.editCalendar);

        button.setEnabled(false);
        event.setEnabled(false);
        simpleSwitch = view.findViewById(R.id.switch2);

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    button.setEnabled(true);
                    event.setEnabled(true);
                }
            }
        });


            df = fstore.collection("Users").document(user.getUid()).collection("Events").document(selectedDate);
             df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            String eventName = documentSnapshot.getString("Event");
                            Images images = new Images();
                            for (int i = 0; i < images.size(); i++) {
                                System.out.println(eventName);
                                System.out.println(images.get(i).getName());
                                if (eventName.equals(images.get(i).getName()))
                                {
                                    System.out.println("Elma");
                                    image.setImageResource(images.get(i).getImageID());
                                    break;
                                }
                                else {
                                    image.setImageResource(R.drawable.plan);
                                }
                            }
                            event.setText(eventName);
                            isExist =true;

                        }else {
                            event.setText("Not Found");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {Log.d("DADA",e.toString());}
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isExist) {
                    user = auth.getCurrentUser();
                    DocumentReference documentReference = fstore.collection("Users").document(user.getUid()).collection("Events").document(selectedDate);
                    Map<String, Object> eventInfo = new HashMap<>();
                    eventInfo.put("Event", event.getText().toString());
                    documentReference.update(eventInfo);

                }
                else {
                    CollectionReference cf = fstore.collection("Users").document(user.getUid()).collection("Events");
                    Map<String, Object> eventInfo = new HashMap<>();
                    eventInfo.put("Event", event.getText().toString());
                    cf.document(selectedDate).set(eventInfo);

                }
                event.setEnabled(false);
                button.setEnabled(false);
                simpleSwitch.setChecked(false);


            }
        });
        view.findViewById(R.id.removeCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = auth.getCurrentUser();
                DocumentReference documentReference = fstore.collection("Users").document(user.getUid()).collection("Events").document(selectedDate);
                documentReference.delete();
                Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        view.findViewById(R.id.exit1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        builder.setView(view);
        return builder.create();
    }
}
