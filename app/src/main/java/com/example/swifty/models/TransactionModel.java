package com.example.swifty.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionModel implements Serializable {

    private final UserModel user;
    private final LocalDateTime dateTime;
    private final List<CartItem> cartItems;

    public UserModel getUser() {
        return user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TransactionModel(UserModel user, LocalDateTime dateTime, List<CartItem> cartItems){
        this.user = user;
        this.dateTime = dateTime;
        this.cartItems = cartItems;
    }

}
