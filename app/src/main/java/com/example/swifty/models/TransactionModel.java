package com.example.swifty.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import java.util.ArrayList;

public class TransactionModel implements Serializable {

    private final Long userId;
    private final String userEmail;
    private final String dateTime;;
    private final List<CartItem> transactionItems;

    public TransactionModel(Long userId, String userEmail, List<CartItem> transactionItems) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.dateTime = LocalDateTime.now().toString();
        this.transactionItems = new ArrayList<>(transactionItems);
    }

    public String getDateTime() {
        return dateTime;
    }

}
