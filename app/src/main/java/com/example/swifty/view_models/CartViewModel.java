package com.example.swifty.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swifty.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private final List<CartItem> cartItems = new ArrayList<>();
    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public void addToCart(CartItem cartItem) {
        boolean itemExists = false;

        // Check if the item already exists in the cart
        for (CartItem item : cartItems) {
            if (item.getProductName().equals(cartItem.getProductName())) {
                item.increaseQuantity();
                item.increasePrice();
                itemExists = true;
                break;
            }
        }
        // If the item doesn't exist, add it to the cart
        if (!itemExists) {
            cartItems.add(cartItem);
        }
        cartItemsLiveData.setValue(cartItems);
    }


    public void emptyCart() {
        cartItems.clear();
        cartItemsLiveData.setValue(cartItems);
    }
}

