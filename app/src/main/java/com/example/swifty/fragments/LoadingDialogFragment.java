package com.example.swifty.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.swifty.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

/*
* This is the loading dialog fragment. This fragment can be used wherever you
* want to show a loading dialog. Just declare an instance of this fragment and show() to
* start the dialog. For closing the dialog, call shutdown().
* */

public class LoadingDialogFragment extends DialogFragment {

    // Singleton instance
    private static LoadingDialogFragment instance;
    // Singleton method
    public static LoadingDialogFragment getInstance() {
        if (instance == null) {
            instance = new LoadingDialogFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate a simple layout containing just the progress indicator
        View view = inflater.inflate(R.layout.fragment_loading_dialog, container, false);
        // Get a reference to the progress indicator
        CircularProgressIndicator circularProgressIndicator = view.findViewById(R.id.loading_indicator);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            // Remove dialog title and make it transparent
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // Set dialog to be non-cancelable by default (you can change this behavior)
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        return dialog;
    }
}
