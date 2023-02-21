package com.example.dogimagepredection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dogimagepredection.databinding.ActivitySplashBinding;

public class splash extends AppCompatActivity {
      Handler h = new Handler();

      ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.textanimation);
        binding.welcome.startAnimation(animation);
        binding.spalashImage.startAnimation(animation);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this,sign_in.class);
                startActivity(i);
                finish();
            }
        },3000);




    }
}