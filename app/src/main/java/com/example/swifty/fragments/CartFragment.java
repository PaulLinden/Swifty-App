package com.example.swifty.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swifty.R;
import com.example.swifty.adapters.CartAdapter;
import com.example.swifty.models.CartViewModel;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    public CartFragment() {}

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        // Initialize adapter
        cartAdapter = new CartAdapter(new ArrayList<>()); // Initialize with an empty list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Find RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Set adapter to RecyclerView
        recyclerView.setAdapter(cartAdapter);


        cartViewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            // Update UI with the new cart items data
            cartAdapter.setCartItems(cartItems); // Update adapter with new data
        });

        return view;
    }
}