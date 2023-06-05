package com.example.dogimagepredection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dogimagepredection.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityHomeBinding binding;
    FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


















        binding.prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        binding.Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity2.class);
                startActivity(intent);
            }
        });
        binding.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Videos.class);
                startActivity(intent);
            }
        });
//        binding.logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                if (currentUser != null) {
//                    // User is logged in
//                    String uid = currentUser.getUid();
//                    Intent intent = new Intent(getApplicationContext(),sign_in.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    FirebaseAuth.getInstance().signOut();
//                }
//
//
//            }
//        });
        binding.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Rating.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_logout:

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // User is logged in
                    String uid = currentUser.getUid();
                    Intent intent = new Intent(getApplicationContext(),sign_in.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
                }
                return true;
            case R.id.nav_home:
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);

                return true;
            case android.R.id.home:

                     binding.drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:

                break;
            case R.id.nav_profile:
                Intent intent1 = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent1);

                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // User is logged in
                    String uid = currentUser.getUid();
                    Intent intent = new Intent(getApplicationContext(),sign_in.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}