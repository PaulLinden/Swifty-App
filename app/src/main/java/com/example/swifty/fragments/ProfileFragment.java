package com.example.swifty.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swifty.R;
import com.example.swifty.managers.UserSessionManager;

public class ProfileFragment extends Fragment {

    private UserSessionManager sessionManager;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new UserSessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String[] credentials = sessionManager.getUserCredentials();
        String username = credentials[0];
        String firstName = credentials[1];
        String lastName = credentials[2];
        String email = credentials[3];
        String birthDate = credentials[4];

        TextView nameText = view.findViewById(R.id.usernameTextView);
        TextView emailText = view.findViewById(R.id.emailTextView);
        TextView birthDateText = view.findViewById(R.id.birthDateTextView);
        TextView firstNameText = view.findViewById(R.id.firstNameTextView);
        TextView lastNameText = view.findViewById(R.id.lastNameTextView);

        nameText.setText(username);
        emailText.setText(email);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        birthDateText.setText(birthDate);

        return view;
    }
}