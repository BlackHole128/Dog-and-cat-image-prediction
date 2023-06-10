package com.example.dogimagepredection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

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

        WebView webView = findViewById(R.id.webview);

// Enable JavaScript (optional, if your HTML page requires it)
        webView.getSettings().setJavaScriptEnabled(true);

// Load the HTML page
        webView.loadUrl("file:///android_asset/gsap.html");


        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this,sign_in.class);
                startActivity(i);
                finish();
            }
        },5000);




    }
}