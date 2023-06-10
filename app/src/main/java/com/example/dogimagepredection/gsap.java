package com.example.dogimagepredection;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class gsap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsap);

        WebView webView = findViewById(R.id.webview);

// Enable JavaScript (optional, if your HTML page requires it)
        webView.getSettings().setJavaScriptEnabled(true);

// Load the HTML page
        webView.loadUrl("file:///android_asset/gsap.html");

    }
}