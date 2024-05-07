package com.example.swifty.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.managers.UserSessionManager;

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
        MainActivity activity = (MainActivity) requireActivity();
        ImageView profileImg = view.findViewById(R.id.profileImageView);
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(new CircleCrop());
        String[] credentials = sessionManager.getUserCredentials();
        String username = credentials[1],
                firstName = credentials[2],
                lastName = credentials[3],
                email = credentials[4],
                birthDate = credentials[5];
        TextView nameText = view.findViewById(R.id.usernameTextView),
                emailText = view.findViewById(R.id.emailTextView),
                birthDateText = view.findViewById(R.id.birthDateTextView),
                firstNameText = view.findViewById(R.id.firstNameTextView),
                lastNameText = view.findViewById(R.id.lastNameTextView);
        //Set credentials in each field
        nameText.setText(username);
        emailText.setText(email);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        birthDateText.setText(birthDate);

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