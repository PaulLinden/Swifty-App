package com.example.swifty.fragments;

import static com.example.swifty.utils.Endpoints.getLoginUrl;
import static com.example.swifty.utils.Message.createMessage;
import static com.example.swifty.utils.Repository.getValidUser;
import static com.example.swifty.utils.strings.LoginStrings.getStrings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.managers.UserSessionManager;
import com.example.swifty.models.UserModel;
import com.example.swifty.utils.strings.LoginStrings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private String serverUrl = null;
    private UserSessionManager sessionManager;
    private Context context;
    LoadingDialogFragment loadingDialog = LoadingDialogFragment.getInstance();
    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get context
        context = requireContext();
        //Get session manager
        sessionManager = new UserSessionManager(context);
        //Get server url
        serverUrl = getLoginUrl(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //Get views
        MainActivity activity = (MainActivity) requireActivity();
        Button loginButton = view.findViewById(R.id.loginButton);
        Button signUpButton = view.findViewById(R.id.createButton);
        TextView usernameInput = view.findViewById(R.id.loginUsername);
        TextView passwordInput = view.findViewById(R.id.loginPassword);
        LoginStrings loginStrings = getStrings();
        //Hide navbar
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(false, activity.bottomNavigationView));
        // Set up button click listener for navigation
        loginButton.setOnClickListener(v -> {
            loginButton.setBackgroundColor(ContextCompat.getColor(context, R.color.mid_grey));
            //Execute login request
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                //Show loading dialog
                loadingDialog.show(getParentFragmentManager(), "loading_dialog");
                //Get user credentials
                String username = usernameInput.getText().toString().trim(),
                        password = passwordInput.getText().toString().trim();
                try {
                    //Validate user credentials
                    UserModel validUser = getValidUser(username, password, serverUrl);
                    //If valid user, save credentials and go to home page
                    if (validUser != null) {
                        activity.runOnUiThread(() -> {
                            sessionManager.saveUserCredentials(
                                    validUser.getId().toString(),
                                    validUser.getUsername(),
                                    validUser.getFirstName(),
                                    validUser.getLastName(),
                                    validUser.getEmail(),
                                    validUser.getBirthdate()
                            );
                            //Go to home page
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                        });
                    } else { //If invalid user, show error message
                        throw new Exception();
                    }
                } catch (Exception e) {
                    createMessage(context, activity, loginStrings.failedTitle(), loginStrings.failedParagraph());
                } finally {
                    loginButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green));
                    //Hide loading dialog
                    loadingDialog.dismiss();
                    //Shutdown executor
                    executor.shutdown();
                }
            });
        });
        // Set up sing up button click listener for navigation
        signUpButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_signUpFragment));
        return view;
    }
}