package com.example.swifty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swifty.R;
import com.example.swifty.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;



    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged(); // Notify RecyclerView that the dataset has changed
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView productNameTextView;
        private final TextView priceTextView;
        private final TextView quantityTextView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }

        public void bind(CartItem cartItem) {
            Context context = itemView.getContext();
            String price = String.valueOf(cartItem.getPrice());
            String quantity = String.valueOf(cartItem.getQuantity());

            productNameTextView.setText(cartItem.getProductName());
            priceTextView.setText(context.getString(R.string.quantity, price));
            quantityTextView.setText(context.getString(R.string.quantity, quantity));

        }
    }
}

