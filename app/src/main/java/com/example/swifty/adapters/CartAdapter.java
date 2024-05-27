package com.example.swifty.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swifty.R;
import com.example.swifty.models.CartItem;
import com.example.swifty.view_models.CartViewModel;

import java.util.List;

/*
* This class is responsible for displaying the cart items in a RecyclerView.
* It extends the RecyclerView.Adapter class and implements the ViewHolder class.
* */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private final CartViewModel cartViewModel;
    private final boolean showRemoveButton;
    public CartAdapter(List<CartItem> cartItems, CartViewModel cartViewModel, boolean showRemoveButton) {
        this.cartItems = cartItems;
        this.cartViewModel = cartViewModel;
        this.showRemoveButton = showRemoveButton;
    }

    @NonNull
    @Override
    // Create a new ViewHolder
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    // Bind the data to the ViewHolder
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);

        // Control button visibility:
        holder.removeButton.setVisibility(showRemoveButton ? View.VISIBLE : View.GONE);
    }

    @Override
    // Return the number of items in the dataset
    public int getItemCount() {
        return cartItems.size();
    }
    // Update the cart items list
    @SuppressLint("NotifyDataSetChanged")
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged(); // Notify RecyclerView that the dataset has changed
    }

    //Inner class for the ViewHolder class
    public class CartViewHolder extends RecyclerView.ViewHolder {
        // Initialize views
        private final TextView productNameTextView;
        private final TextView priceTextView;
        private final TextView quantityTextView;
        private final Button removeButton;
        // Constructor
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
        // Bind data to views
        public void bind(CartItem cartItem) {
            Context context = itemView.getContext();
            String price = String.valueOf(cartItem.getPrice());
            String quantity = String.valueOf(cartItem.getQuantity());
            // Set the text of the views
            productNameTextView.setText(cartItem.getProductName());
            priceTextView.setText(context.getString(R.string.quantity, price));
            quantityTextView.setText(context.getString(R.string.quantity, quantity));

            // Remove button click listener
            removeButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    cartItems.remove(currentPosition);
                    cartViewModel.removeFromCart(cartItem);
                    notifyItemRemoved(currentPosition);
                }
            });
        }
    }
}

