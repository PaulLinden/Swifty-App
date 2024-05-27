package com.example.swifty.utils.strings;

import androidx.annotation.NonNull;

/*
 * This class holds the strings for the Login.java class.
 * */

public record LoginStrings(String failedTitle, String failedParagraph, String errorTitle, String errorParagraph) {

    @NonNull
    public static LoginStrings getStrings() {
        String failedTitle = "Login Failed.",
                failedParagraph = "Username or password is not valid, Please check your input fields.",
                errorTitle = "Error.",
                errorParagraph = "An unknown error occurred.";
        return new LoginStrings(failedTitle, failedParagraph, errorTitle, errorParagraph);
    }
}
