package com.example.swifty.fragments;

import static com.example.swifty.utils.Endpoints.getTransactionUrl;
import static com.example.swifty.utils.Message.createMessage;
import static com.example.swifty.utils.Message.createMessageAndNavigate;
import static com.example.swifty.utils.Repository.isSuccessfulTransaction;
import static com.example.swifty.utils.handlers.TransactionHandler.createTransaction;
import static com.example.swifty.utils.strings.DeliverStrings.getStrings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.adapters.CartAdapter;
import com.example.swifty.managers.UserSessionManager;
import com.example.swifty.models.CartItem;
import com.example.swifty.models.TransactionModel;
import com.example.swifty.utils.strings.DeliverStrings;
import com.example.swifty.view_models.CartViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* Cart Fragment to display cart items and checkout button.
* Uses CartViewModel to get cart items and submit transaction
* and cartAdapter to display cart items.
* */

public class CartFragment extends Fragment {
    private UserSessionManager sessionManager;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private List<CartItem> cart;
    private MainActivity activity;
    private Context context;
    private String transactionUrl;
    public CartFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize variables
        activity = (MainActivity) requireActivity();
        context = requireContext();
        sessionManager = new UserSessionManager(context);
        cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);
        cartAdapter = new CartAdapter(new ArrayList<>());
        transactionUrl = getTransactionUrl(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        //Initialize variables
        Button checkOut = view.findViewById(R.id.checkoutButton);
        RecyclerView recyclerView = view.findViewById(R.id.cartRecyclerView);
        DeliverStrings deliverStrings = getStrings();
        //Set bottom navigation bar visibility
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView));
        // Set layout for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
            //Check if cart is empty
            if (cart == null) {
                createMessage(context, activity, "Empty.", "Your cart is empty.");
            } else {
                //Create transaction
                TransactionModel transactionModel = createTransaction(sessionManager, cart);
                //Submit transaction
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    try {
                        //If transaction is successful, navigate to deliver fragment
                        if (isSuccessfulTransaction(transactionModel, transactionUrl)) {
                            createMessageAndNavigate(
                                    view,
                                    R.id.action_cartFragment_to_deliverFragment,
                                    context,
                                    activity,
                                    deliverStrings.completeTitle(),
                                    deliverStrings.completeParagraph());
                        } else {
                            //If transaction throw error
                            throw new Exception("Transaction failed.");
                        }
                    } catch (Exception e) {
                        //display error
                        createMessage(
                                context,
                                activity,
                                deliverStrings.errorTitle(),
                                deliverStrings.errorParagraph());
                    }
                });
                //Shutdown executor
                executor.shutdown();
            }
        }));
        return view;
    }
}