package com.example.swifty.utils.handlers;

import androidx.annotation.NonNull;

import com.example.swifty.managers.UserSessionManager;
import com.example.swifty.models.CartItem;
import com.example.swifty.models.TransactionModel;

import java.util.List;

public class TransactionHandler {

    @NonNull
    public static TransactionModel createTransaction(UserSessionManager sessionManager, List<CartItem> cartItems) {
        //Get user credentials
        String[] credentials = sessionManager.getUserCredentials();
        Long userId = Long.parseLong(credentials[0]);
        String userEmail = credentials[4];
        //Initiate transaction object
        return new TransactionModel(userId, userEmail, cartItems);
    }
}
