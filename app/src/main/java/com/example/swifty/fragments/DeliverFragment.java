package com.example.swifty.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import com.example.swifty.R;

public class DeliverFragment extends Fragment {

    private MapView mapView;

    public DeliverFragment() {}

    public static DeliverFragment newInstance(String param1, String param2) {
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

        // Initialize osmdroid configuration (required)
        Configuration.getInstance().load(getContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(getContext()));

        // Initialize the map view
        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        //Needs Visibility pVisibility
        //mapView.getZoomController().setVisibility();
        mapView.setMultiTouchControls(true);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume(); // Required for osmdroid
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause(); // Required for osmdroid
    }

}