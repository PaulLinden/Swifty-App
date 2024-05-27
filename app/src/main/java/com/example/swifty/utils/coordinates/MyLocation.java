package com.example.swifty.utils.coordinates;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

/*
* This class is used to get the current location of the user.
* It uses the LocationManager API to get the last known location and calls the callback with the coordinates.
* */

public class MyLocation {
    // Get the current location of the user
    public static void getMyCoordinates(Context context, OnLocationReceivedCallback callback) {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Get the location manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Get the last known location
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        // Check if location is available
        if (location != null) {
            // Location available, call the callback with coordinates
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            callback.onLocationReceived(latitude, longitude);
        } else {
            // Location not available, show a message
            Toast.makeText(context, "Location unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnLocationReceivedCallback {
        // Callback interface for receiving location coordinates
        void onLocationReceived(double latitude, double longitude);
    }
}
