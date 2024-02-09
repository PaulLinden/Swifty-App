package com.example.swifty.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private final List<CartItem> cartItems = new ArrayList<>();
    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public void addToCart(CartItem cartItem) {
        cartItems.add(cartItem);
        System.out.println(cartItem);
        cartItemsLiveData.setValue(cartItems);
        System.out.println(cartItems.size());
    }
}

