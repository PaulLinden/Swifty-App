package com.example.swifty.utils.strings;

import androidx.annotation.NonNull;

/*
* This class holds the strings for the Deliver.java class.
* */

public record DeliverStrings(String completeTitle, String completeParagraph, String errorTitle, String errorParagraph) {
    @NonNull
    public static DeliverStrings getStrings() {
        String completeTitle = "Complete.",
                completeParagraph = "Thank you for your purchase.",
                errorTitle = "Error!",
                errorParagraph = "An error occurred during the transaction";
        return new DeliverStrings(completeTitle, completeParagraph, errorTitle, errorParagraph);
    }
}
