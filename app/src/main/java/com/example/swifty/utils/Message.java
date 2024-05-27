package com.example.swifty.utils;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

/*
* This class is for creating messages. It also has a method
* for creating messages and navigating to another fragment.
* */

public class Message {
    // For creating messages.
    public static void createMessage(Context context, FragmentActivity activity, String title, String message) {
        activity.runOnUiThread(() -> {
            // Create an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Set the alert dialog title and message
            builder.setTitle(title).setMessage(message);
            // Set the positive button action
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    // For creating messages and navigating to another fragment.
    public static void createMessageAndNavigate(View view, int action, Context context, FragmentActivity activity, String title, String message) {
        activity.runOnUiThread(() -> {
            // Create an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Set the alert dialog title and message
            builder.setCancelable(false);
            builder.setTitle(title).setMessage(message);
            // Set the positive button action
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                Navigation.findNavController(view).navigate(action);
            });
            builder.show();
        });
    }

}
