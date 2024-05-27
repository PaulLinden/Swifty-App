package com.example.swifty.activities;

import static com.example.swifty.utils.Endpoints.initBaseUrl;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.swifty.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

/*
* Main activity for the application. Here we set up the navigation controller
* and the bottom navigation bar, we also request permission to use location and
* handel back button press. Here the initBaseUrl method is called to set the base url for the application.
* */

public class MainActivity extends AppCompatActivity {
    //Request code for location permission
    private static final int REQUEST_LOCATION_PERMISSION = 101;
    //Navigation controller
    private NavController navController;
    //Bottom navigation bar
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set base url
        initBaseUrl(this);
        //Initialize view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setup for navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        //Setup for bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //Setup for back button
        OnBackPressedDispatcher onBackPressedDispatcher = this.getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Check if current destination is deliverFragment
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.deliverFragment) {
                    navController.navigate(R.id.action_deliverFragment_to_companyDetailFragment2);
                    setBottomNavigationBarVisibility(true, bottomNavigationView);
                }
            }
        });
        //Hide bottom navigation bar
        bottomNavigationView.setVisibility(View.GONE);
        //Request permission to use location
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    //Check if back button is pressed
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
    //Set bottom navigation bar visibility
    public void setBottomNavigationBarVisibility(boolean isVisible, BottomNavigationView bottomNavigationView) {

        if (isVisible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

}