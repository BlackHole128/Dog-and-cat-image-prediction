package com.example.dogimagepredection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogimagepredection.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_in extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    public static final String SHARED_PREFS ="sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

//        login = findViewById(R.id.sign_in);
//
//        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//            }
//        });






       checkBox();




        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.username.getEditText().getText().toString();
                String password = binding.password.getEditText().getText().toString();
               progressDialog.setTitle("Loging in");
               progressDialog.show();
               try {
                   firebaseAuth.signInWithEmailAndPassword(email, password)
                           .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                               @Override
                               public void onSuccess(AuthResult authResult) {
                                   if(binding.remember.isChecked()) {
                                       SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                       editor.putString("name", "true");
                                       editor.apply();
                                       Intent intent = new Intent(getApplicationContext(), Home.class);
                                       startActivity(intent);
                                       finish();
                                   }
                                   else {
                                       SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                       editor.putString("name","");
                                       editor.apply();
                                       Intent intent = new Intent(getApplicationContext(), Home.class);
                                       startActivity(intent);
                                   }

                                   progressDialog.cancel();
                                   binding.username.getEditText().setText("");
                                   binding.password.getEditText().setText("");


                               }
                           })
                           .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(sign_in.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                   progressDialog.cancel();
                               }
                           });

               }
               catch (Exception e){
                   Toast.makeText(sign_in.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                   progressDialog.cancel();
               }
            }
        });
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SiginUp.class);
                startActivity(intent);
            }
        });
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String email = binding.username.getEditText().getText().toString();

                    progressDialog.setTitle("Sending mail");
                    progressDialog.show();
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.cancel();
                                    Toast.makeText(sign_in.this,"email sent",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(sign_in.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                catch (Exception e){
                    progressDialog.cancel();
                    Toast.makeText(sign_in.this,"enter Email in the email section",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String chake = sharedPreferences.getString("name","");
        if(chake.equals("true")){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();

        }


    }


//    protected void onStart(){
//        super.onStart();
//
////        SessionManeger sessionManeger = new SessionManeger(sign_in.this);
////        int userID = sessionManeger.getSession();
////        if (userID !=-1){
////            Intent intent = new Intent(getApplicationContext(), Home.class);
////            startActivity(intent);
////        }
//
//        if(binding.remember.isChecked()) {
//
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (currentUser != null) {
//                // User is logged in
//                String uid = currentUser.getUid();
//
//                Intent intent = new Intent(getApplicationContext(), Home.class);
//                startActivity(intent);
////            FirebaseAuth.getInstance().signOut();
//            }
//            else
//            {
//
//            }
//        }
//
//    }



}