package com.example.swifty.utils;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

public class Message {

    public static void createMessage(Context context, FragmentActivity activity, String title, String message) {
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title).setMessage(message);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    public static void createMessageAndNavigate(View view, int action, Context context, FragmentActivity activity, String title, String message) {
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle(title).setMessage(message);
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                Navigation.findNavController(view).navigate(action);
            });
            builder.show();
        });
    }

}
