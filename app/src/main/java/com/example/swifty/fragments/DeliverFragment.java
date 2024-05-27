package com.example.swifty.fragments;

import static com.example.swifty.utils.coordinates.CoordinatesCalculator.calculateDistance;
import static com.example.swifty.utils.coordinates.CoordinatesCalculator.calculateMidpoint;
import static com.example.swifty.utils.handlers.MapHandler.setUpMap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.adapters.CartAdapter;
import com.example.swifty.utils.coordinates.MyLocation;
import com.example.swifty.view_models.CartViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

/*
* This fragment displays the delivery of the order and shows
* closest route between user and delivery, this is only a mock
* for now and will be further developed.
* */

public class DeliverFragment extends Fragment implements MyLocation.OnLocationReceivedCallback {
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private Context context;
    private MainActivity activity;
    private MapView mapView;

    public DeliverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize variables
        context = requireContext();
        activity = (MainActivity) requireActivity();
        cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);
        cartAdapter = new CartAdapter(new ArrayList<>(), cartViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliver, container, false);
        //Initialize views
        mapView = view.findViewById(R.id.mapView);
        RecyclerView recyclerView = view.findViewById(R.id.checkOutRecyclerView);
        Button closeButton = view.findViewById(R.id.closeButton);
        //Get user position
        MyLocation.getMyCoordinates(context, DeliverFragment.this);
        //Load preferences
        Configuration.getInstance().load(getContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(context));

        //Hide navbar
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(false, activity.bottomNavigationView));
        //On close empty cart, got to home and display navbar again
        closeButton.setOnClickListener((v) -> {
            cartViewModel.emptyCart();
            Navigation.findNavController(v).navigate(R.id.action_deliverFragment_to_companyDetailFragment2);
        });
        //Set layout
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Set adapter to RecyclerView
        recyclerView.setAdapter(cartAdapter);
        // Observe changes to the cart items LiveData
        cartViewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            cartAdapter.setCartItems(cartItems); // Update adapter with new data
        });
        return view;
    }
    //Callback for getting user position
    @Override
    public void onLocationReceived(double latitude, double longitude) {
        //Mock of getting position of delivery
        double mockLat = 55.55367, mockLong = 12.99403;
        //Calculate midpoint of coordinates
        double[] calcMidpoint = calculateMidpoint(latitude, longitude, mockLat, mockLong);
        double distanceKm = calculateDistance(latitude, longitude, mockLat, mockLong);
        //Zoom
        long zoomTime = 5000L;
        double zoom = distanceKm * 2.85;// Needs to scale dependent on the distance
        //Start point is user position and endPoint is delivery's position
        GeoPoint startPoint = new GeoPoint(latitude, longitude),
                endPoint = new GeoPoint(mockLat, mockLong),
                midPoint = new GeoPoint(calcMidpoint[0], calcMidpoint[1]);
        // Start a new thread to perform network operation
        new Thread(() -> setUpMap(context, mapView, startPoint, endPoint, midPoint, zoom, zoomTime)).start();
    }
}