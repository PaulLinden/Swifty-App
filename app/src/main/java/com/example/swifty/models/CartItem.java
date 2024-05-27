package com.example.swifty.models;

import java.io.Serializable;

/*
* This class represents a single item in a shopping cart.
* It contains the company name, product name, price, and quantity.
* Methods are provided to increase the quantity and price of the
* item in case the item is already in the cart.
* It also implements the Serializable interface for serialization.
* */

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




