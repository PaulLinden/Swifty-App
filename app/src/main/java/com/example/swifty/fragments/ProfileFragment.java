package com.example.swifty.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.managers.UserSessionManager;

/*
* This fragment is used to display user profile. For now only the user credentials are displayed.
* Later on the functionality for the profile image will be implemented.
* */

public class ProfileFragment extends Fragment {

    private UserSessionManager sessionManager;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = requireContext();
        sessionManager = new UserSessionManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Get activity
        MainActivity activity = (MainActivity) requireActivity();
        //Get profile image
        ImageView profileImg = view.findViewById(R.id.profileImageView);
        //Create multi transformation
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CircleCrop());
        //Get user credentials
        String[] credentials = sessionManager.getUserCredentials();
        String username = credentials[1],
                firstName = credentials[2],
                lastName = credentials[3],
                email = credentials[4],
                birthDate = credentials[5];
        //Get text views
        TextView nameText = view.findViewById(R.id.usernameTextView),
                emailText = view.findViewById(R.id.emailTextView),
                birthDateText = view.findViewById(R.id.birthDateTextView),
                firstNameText = view.findViewById(R.id.firstNameTextView),
                lastNameText = view.findViewById(R.id.lastNameTextView);
        //Get logout button
        Button logoutButton = view.findViewById(R.id.logoutButton);
        //Set credentials in each field
        nameText.setText(username);
        emailText.setText(email);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        birthDateText.setText(birthDate);
        //Set logout button listener
        logoutButton.setOnClickListener(v -> {
            //Clear user session
            sessionManager.logout();
            Navigation.findNavController(v).navigate( R.id.action_profileFragment_to_loginFragment);
        });
        //Set navbar visible
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView));
        //Set placeholder image for profile
        Glide.with(this)
                .load(R.drawable.profile_placeholder)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(profileImg);

        return view;
    }
}