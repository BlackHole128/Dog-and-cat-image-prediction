package com.example.dogimagepredection;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogimagepredection.databinding.ActivityPostingBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Posting extends AppCompatActivity {
    ActivityPostingBinding binding;

    FirebaseFirestore firebaseFirestore;
    String name ;
    FirebaseUser user;
    Uri imageUri;
    ProgressDialog mLoadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityPostingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}