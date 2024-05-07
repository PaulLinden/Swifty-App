package com.example.swifty.fragments;

import static com.example.swifty.utils.Message.createMessage;

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
import com.example.swifty.models.CartItem;
import com.example.swifty.view_models.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private List<CartItem> cart;
    private MainActivity activity;
    private Context context;

    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) requireActivity();
        context = requireContext();
        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);
        // Initialize adapter
        cartAdapter = new CartAdapter(new ArrayList<>()); // Initialize with an empty list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        Button checkOut = view.findViewById(R.id.checkoutButton);
        RecyclerView recyclerView = view.findViewById(R.id.cartRecyclerView);
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView));

        // Set layout for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Set adapter to RecyclerView
        recyclerView.setAdapter(cartAdapter);
        //Get cart items
        cartViewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            // Update UI with the new cart items data
            cartAdapter.setCartItems(cartItems); // Update adapter with new data
            cart = cartItems;
        });

        //Check out on click
        checkOut.setOnClickListener((v) -> activity.runOnUiThread(() -> {
            if (cart == null) {
                System.out.println("CLICK CLICK");
                createMessage(context, activity, "Empty.", "Your cart is empty.");
            } else {
                Navigation.findNavController(v).navigate(R.id.action_cartFragment_to_deliverFragment);
            }
        }));
        return view;
    }
}