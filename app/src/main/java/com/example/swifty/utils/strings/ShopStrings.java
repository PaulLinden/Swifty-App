package com.example.swifty.utils.strings;

import androidx.annotation.NonNull;

/*
 * This class holds the strings for the Shop.java class.
 * */

public record ShopStrings(String productText, String priceText, String quantityText, String add) {
    @NonNull
    public static ShopStrings getStrings(String productName, String price, String quantity) {
        String priceText = "$" + price,
                quantityText = "Quantity: " + quantity,
                add = "add";
        return new ShopStrings(productName, priceText, quantityText, add);
    }
}
