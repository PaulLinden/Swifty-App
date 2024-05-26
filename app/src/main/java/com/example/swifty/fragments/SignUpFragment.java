package com.example.swifty.fragments;

import static com.example.swifty.R.*;
import static com.example.swifty.utils.Endpoints.getCreateUserUrl;
import static com.example.swifty.utils.Message.createMessage;
import static com.example.swifty.utils.Message.createMessageAndNavigate;
import static com.example.swifty.utils.Repository.createUser;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.example.swifty.R;
import com.example.swifty.models.UserModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* Fragment to create a new user account.
* */

public class SignUpFragment extends Fragment {

    FragmentActivity activity;
    Context context;
    String createUserUrl = null;
    LoadingDialogFragment loadingDialog = LoadingDialogFragment.getInstance();
    public SignUpFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
        context = requireContext();
        createUserUrl = getCreateUserUrl(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        //Get views
        TextView errorBanner = view.findViewById(R.id.bannerTextView);
        EditText
                username = view.findViewById(R.id.usernameTextView),
                email = view.findViewById(R.id.emailTextView),
                firstName = view.findViewById(R.id.firstNameTextView),
                lastName = view.findViewById(R.id.lastNameTextView),
                birthdate = view.findViewById(R.id.birthDateTextView),
                password = view.findViewById(R.id.passwordTextView);
        //Gather inputs to check
        EditText[] inputs = {username, email, password, firstName, lastName, birthdate};
        Button
                confirmButton = view.findViewById(R.id.create_button),
                cancelButton = view.findViewById(R.id.cancel_button);
        //Add text watcher to birthdate
        birthdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                // Insert the hyphen so that the birthdate == YYYY-mm-DD
                if (input.length() == 4 && Character.isDigit(input.charAt(3))) {
                    s.insert(4, "-");
                } else if (input.length() == 7 && Character.isDigit(input.charAt(6))) {
                    s.insert(7, "-");
                }
            }
        });
        //Create user
        UserModel newUser = new UserModel();
        //Set click listeners
        confirmButton.setOnClickListener(v -> {
            //Check inputs
            if (checkInputs(inputs, errorBanner)) {//If all input is valid
                //Show loading dialog
                loadingDialog.show(activity.getSupportFragmentManager(), "loading_dialog");
                //Disable button
                confirmButton.setBackgroundColor(ContextCompat.getColor(context, R.color.mid_grey));
                //Set user data
                newUser.setEmail(email.getText().toString());
                newUser.setUsername(username.getText().toString());
                newUser.setFirstName(firstName.getText().toString());
                newUser.setLastName(lastName.getText().toString());
                newUser.setBirthdate(birthdate.getText().toString());
                newUser.setPassword(password.getText().toString());
                //Create user in background thread
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    try {
                        //Send user to server
                        boolean isUserCreated = createUser(createUserUrl, newUser);
                        //If user is created navigate to login
                        if (isUserCreated) {
                            createMessageAndNavigate(
                                    v,
                                    id.action_signUpFragment_to_loginFragment,
                                    context, activity,
                                    "Success",
                                    "User created successfully."
                            );
                        } else {//If user is not created throw error
                            throw new Exception("User not created");
                        }
                    } catch (Exception e) {
                        createMessage(context, activity, e.getMessage(), "Something went wrong. Try again later.");
                        Log.e("Error", "Error creating account", e);
                    } finally {
                        //Enable button
                        confirmButton.setBackgroundColor(ContextCompat.getColor(context, color.light_green));
                        //Dismiss loading dialog
                        loadingDialog.dismiss();
                        //Shutdown executor
                        executor.shutdown();
                    }
                });
            }
        });
        //Cancel button set click listener
        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_loginFragment));
        return view;
    }

    //Method to check inputs
    private boolean checkInputs(EditText[] inputs, TextView errorBanner) {
        for (EditText input : inputs) {
            //If input is empty
            if (emptyField(input) ||
                    (input.getId() == R.id.emailTextView && emailField(input)) ||
                    (input.getId() == R.id.passwordTextView && passwordField(input)) ||
                    (input.getId() == R.id.birthDateTextView && birthdateField(input))) {
                //Set error banner
                errorBanner.setVisibility(View.VISIBLE);
                return false;
            } else {
                input.setBackgroundResource(drawable.round_corners_black_border);
            }
        }
        //If all inputs are valid
        errorBanner.setVisibility(View.GONE);
        return true;
    }

    //Methods to check input fields
    public boolean emptyField(EditText input) {
        if (input.getText().toString().isEmpty()) {
            input.setError("Field can't be empty");
            input.setBackgroundResource(R.drawable.round_corners_red_border);
            return true;
        }
        return false;
    }
    public boolean passwordField(EditText input) {
        if (input.getText().toString().length() < 8) {
            input.setError("Password must be at least 8 characters long");
            input.setBackgroundResource(R.drawable.round_corners_red_border);
            return true;
        }
        return false;
    }
    public boolean emailField(EditText input) {

        String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        if (!input.getText().toString().matches(regex)) {
            input.setError("Please add a valid email, example: validmail@email.com");
            input.setBackgroundResource(R.drawable.round_corners_red_border);

            return true;
        }
        return false;
    }
    public boolean birthdateField(EditText input) {
        String regex = "^([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
        if (!input.getText().toString().matches(regex)) {
            input.setError("Please enter a valid format (YYYY-mm-DD)");
            input.setBackgroundResource(R.drawable.round_corners_red_border);

            return true;
        }
        return false;
    }
}
