package com.example.swifty.utils.handlers;

import androidx.annotation.NonNull;

import com.example.swifty.managers.UserSessionManager;
import com.example.swifty.models.CartItem;
import com.example.swifty.models.TransactionModel;

import java.util.List;

/*
* This class is responsible for creating a transaction object.
* It takes a user session manager and a list of cart items.
* This works as a factory for creating a transaction object.
* */

public class TransactionHandler {
    //Create a transaction object
    @NonNull
    public static TransactionModel createTransaction(UserSessionManager sessionManager, List<CartItem> cartItems) {
        //Get user credentials
        String[] credentials = sessionManager.getUserCredentials();
        Long userId = Long.parseLong(credentials[0]);
        String userEmail = credentials[4];
        //Initiate transaction object and return it
        return new TransactionModel(userId, userEmail, cartItems);
    }
}
