package com.example.swifty.fragments;

import static com.example.swifty.utils.Endpoints.getLoginUrl;
import static com.example.swifty.utils.Message.createMessage;
import static com.example.swifty.utils.Repository.isValidUser;
import static com.example.swifty.utils.strings.LoginStrings.getStrings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.managers.UserSessionManager;
import com.example.swifty.models.UserModel;
import com.example.swifty.utils.strings.LoginStrings;

import org.json.JSONException;

import java.io.IOException;

public class LoginFragment extends Fragment {

    private final UserModel CURRENT_USER = new UserModel();
    private String serverUrl = null;
    private UserSessionManager sessionManager;
   private Context context;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        sessionManager = new UserSessionManager(context);
        try {
            serverUrl = getLoginUrl(context);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        MainActivity activity = (MainActivity) requireActivity();
        Button loginButton = view.findViewById(R.id.loginButton);
        TextView usernameInput = view.findViewById(R.id.loginUsername);
        TextView passwordInput = view.findViewById(R.id.loginPassword);
        LoginStrings loginStrings = getStrings();
        //Hide navbar
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(false, activity.bottomNavigationView));
        // Set up button click listener for navigation
        loginButton.setOnClickListener(v -> new Thread(() -> {
            String username = usernameInput.getText().toString(),
                    password = passwordInput.getText().toString();
            try {
                //Validate user -- Create flag?
                if (isValidUser(username, password, serverUrl, CURRENT_USER)) {
                    activity.runOnUiThread(() -> {
                        //Save user credentials for later use
                        sessionManager.saveUserCredentials(
                                CURRENT_USER.getId().toString(),
                                CURRENT_USER.getUserName(),
                                CURRENT_USER.getFirstName(),
                                CURRENT_USER.getLastName(),
                                CURRENT_USER.getEmail(),
                                CURRENT_USER.getBirthDate()
                        );
                        //Go to home page
                        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                    });
                } else {
                    //Show alert if user isn't valid
                    createMessage(context, activity, loginStrings.failedTitle(), loginStrings.failedParagraph());
                }
            } catch (JSONException e) {
                //Show alert if error
                createMessage(context, activity, loginStrings.errorTitle(), loginStrings.errorParagraph());
                e.fillInStackTrace();
            }
        }).start());

        return view;
    }
}