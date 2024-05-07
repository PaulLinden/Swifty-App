package com.example.swifty.models;

import java.io.Serializable;

public class CartItem implements Serializable {

    private final String companyName;
    private final String productName;
    private double price;
    private int quantity;

    public CartItem(String companyName ,String productName, double price, int quantity) {
        this.companyName = companyName;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void increasePrice() {
        this.price += price;
    }

}




