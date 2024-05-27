package com.example.swifty.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swifty.models.CartItem;

import java.util.ArrayList;
import java.util.List;

/*
* This class is responsible for adding and removing items from the cart.
* It also provides a method to empty the cart.
* It uses the LiveData pattern to notify the UI when the cart changes.
* */

public class CartViewModel extends ViewModel {
    private final MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private final List<CartItem> cartItems = new ArrayList<>();

    //Get the cart items
    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }
    // Add an item to the cart
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
        // Notify the UI that the cart has changed
        cartItemsLiveData.setValue(cartItems);
    }
    // Remove an item from the cart
    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItemsLiveData.setValue(cartItems);
    }
    // Remove all items from the cart
    public void emptyCart() {
        cartItems.clear();
        cartItemsLiveData.setValue(cartItems);
    }
}

