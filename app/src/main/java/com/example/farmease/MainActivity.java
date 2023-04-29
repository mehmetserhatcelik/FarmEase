package com.example.farmease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmease.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

	private ActivityMainBinding binding;
	private FirebaseAuth auth;
	private FirebaseFirestore fstore;
	private Switch simpleSwitch;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		setContentView(view);

		auth = FirebaseAuth.getInstance();
		fstore = FirebaseFirestore.getInstance();
		simpleSwitch = (Switch) findViewById(binding.switch1.getId());
		boolean switchState = simpleSwitch.isChecked();
		FirebaseUser user = auth.getCurrentUser();
		if(user != null && switchState == true)
		{
			Intent intent = new Intent(MainActivity.this , MainScreen.class);
			startActivity(intent);
			finish();
		}
		ImageView image = binding.imageView2;
		image.setImageResource(R.drawable.visibleeye);
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(binding.passwordText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
				{
					binding.passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
					image.setImageResource(R.drawable.visibleeye);

				}
				else
				{
					binding.passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					image.setImageResource(R.drawable.indir);
				}
			}
		});


	}

	public void signUpClicked(View view)
	{
		Intent intent = new Intent(MainActivity.this , Register.class);
		startActivity(intent);
		finish();

	}
	public void signInClicked(View view)
	{
		String email = binding.emailText.getText().toString();
		String password = binding.passwordText.getText().toString();

		if(email.equals("") || password.equals("")){
			Toast.makeText(this ,"Password or email cannot be empty !",Toast.LENGTH_LONG).show();
		}
		else {
			auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
				@Override
				public void onSuccess(AuthResult authResult) {
					checkIsEngineer(authResult.getUser().getUid());
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	private void checkIsEngineer(String uid)
	{

		DocumentReference df = fstore.collection("Users").document(uid);
		df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				Log.d("TAG","onSuccess: "+documentSnapshot.getData());

				if(documentSnapshot.getString("isEngineer").equals("1"))
				{
					Intent intent = new Intent(MainActivity.this , AdminPanel.class);
					startActivity(intent);
					finish();
				}
				else {
					Intent intent = new Intent(MainActivity.this , MainScreen.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
}