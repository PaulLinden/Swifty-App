package com.example.swifty.fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.swifty.R;
import com.example.swifty.utils.MyLocation;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class DeliverFragment extends Fragment implements MyLocation.OnLocationReceivedCallback{

    GeoPoint startPoint;
    private static final int REQUEST_LOCATION_PERMISSION = 101;
    public DeliverFragment() {
    }

    public static DeliverFragment newInstance() {
        DeliverFragment fragment = new DeliverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliver, container, false);

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        MyLocation.getMyCoordinates(requireContext(), DeliverFragment.this);
        Configuration.getInstance().load(getContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()));
        MapView mapView = view.findViewById(R.id.mapView);

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        mapView.setMultiTouchControls(true);

        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("My Location");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapView.getController().setCenter(startPoint);
        mapView.getController().setZoom(19);

        return view;
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        startPoint = new GeoPoint(latitude,longitude);
    }
}