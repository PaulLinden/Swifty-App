package com.example.swifty.fragments;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.adapters.CartAdapter;
import com.example.swifty.models.CartItem;
import com.example.swifty.utils.CoordinatesCalculator;
import com.example.swifty.utils.MyLocation;
import com.example.swifty.view_models.CartViewModel;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class DeliverFragment extends Fragment implements MyLocation.OnLocationReceivedCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 101;
    private GeoPoint startPoint, endPoint, midPoint;
    private MapView mapView;
    private Drawable person;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

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
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        cartAdapter = new CartAdapter(new ArrayList<>()); // Initialize with an empty list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliver, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.checkOutRecyclerView);
        MainActivity activity = (MainActivity) requireActivity();
        Button closeButton = view.findViewById(R.id.closeButton);

        //Hide navbar
        requireActivity().runOnUiThread(() -> {
            activity.setBottomNavigationBarVisibility(false, activity.bottomNavigationView);
        });
        //On close got to home and display navbar again
        closeButton.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_deliverFragment_to_companyDetailFragment2);
            activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView);
        });

        //Get Icon
        person = ContextCompat.getDrawable(requireContext(), org.osmdroid.library.R.drawable.person);
        //Get Map
        mapView = view.findViewById(R.id.mapView);

        //Request permission to use location
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        //Get users location
        MyLocation.getMyCoordinates(requireContext(), DeliverFragment.this);
        Configuration.getInstance().load(getContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()));


        //Set checkout list...............
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Set adapter to RecyclerView
        recyclerView.setAdapter(cartAdapter);

        // Observe changes to the cart items LiveData
        cartViewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            cartAdapter.setCartItems(cartItems); // Update adapter with new data
        });

        return view;
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        //Mock of getting position of delivery
        double mockLat = 55.55367;
        double mockLong = 12.99403;
        //Calculate midpoint of coordinates
        double[] calcMidpoint = CoordinatesCalculator.calculateMidpoint(latitude,longitude,mockLat,mockLong);
        double distanceKm = CoordinatesCalculator.calculateDistance(latitude,longitude,mockLat,mockLong);
        //Zoom
        long zoomTime = 5000L;
        double zoom = distanceKm * 2.85;// Needs to scale dependent on the distance

        //Start point is user position and endPoint is delivery's position
        startPoint = new GeoPoint(latitude, longitude);
        endPoint = new GeoPoint(mockLat, mockLong);
        midPoint = new GeoPoint(calcMidpoint[0],calcMidpoint[1]);


        // Start a new thread to perform network operation
        new Thread(() -> {
            //Get map route
            RoadManager roadManager = new OSRMRoadManager(requireContext(), "swifty/1.0-beta (Android)");
            ArrayList<GeoPoint> waypoints = new ArrayList<>();
            waypoints.add(startPoint);
            waypoints.add(endPoint);

            final Road road = roadManager.getRoad(waypoints);

            // Update UI on the main thread
            mapView.post(() -> {
                //Set marker for users current position
                Marker marker = new Marker(mapView);
                marker.setPosition(startPoint);
                marker.setTitle("User Location");
                marker.setIcon(person);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().add(marker);

                mapView.getController().animateTo(midPoint,zoom,zoomTime);
                mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
                mapView.setMultiTouchControls(true);

                if (road != null) {
                    //Set marker for users current position
                    Marker delMarker = new Marker(mapView);
                    delMarker.setPosition(endPoint);
                    delMarker.setTitle("Delivery Location");
                    delMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mapView.getOverlays().add(delMarker);
                    //Display road overlay
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    mapView.getOverlays().add(roadOverlay);
                    mapView.getController().setCenter(startPoint);
                    mapView.invalidate();
                }
            });
        }).start();
    }
}