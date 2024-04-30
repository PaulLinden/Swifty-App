package com.example.swifty.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.swifty.R;
import com.example.swifty.managers.UserSessionManager;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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

        //Make img round
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new CircleCrop()
        );
        //Set profile pic in future
        ImageView profileImg = view.findViewById(R.id.profileImageView);
            Glide.with(this)
                    .load(R.drawable.profile_placeholder)
                    .apply(RequestOptions.bitmapTransform(multiTransformation))
                    .into(profileImg);

        nameText.setText(username);
        emailText.setText(email);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        birthDateText.setText(birthDate);

        return view;
    }
}