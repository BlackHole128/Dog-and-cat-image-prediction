package com.example.dogimagepredection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class Videos extends AppCompatActivity {
    private YouTubePlayerView  youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        youTubePlayerView = findViewById(R.id.youtubePalyer);

    }
}