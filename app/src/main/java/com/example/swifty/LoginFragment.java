package com.example.swifty;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private String serverUrl = null;
    public LoginFragment(){}
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            serverUrl = getUrl(this.requireContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Set up button click listener for navigation
        Button nextButton = view.findViewById(R.id.loginButton);
        TextView usernameInput = view.findViewById(R.id.loginUsername);
        TextView passwordInput = view.findViewById(R.id.loginPassword);

        nextButton.setOnClickListener(v -> new Thread(() ->{
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            try {
                if (isValidUser(username,password)){
                    requireActivity().runOnUiThread(() -> Navigation.findNavController(v).navigate(R.id.homeFragment));
                }else {
                    requireActivity().runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Error!").setMessage("Username or password is not valid.");
                        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        builder.show();
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start());

        return view;
    }
    private boolean isValidUser(String usernameInput, String passwordInput) throws JSONException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", usernameInput);
        jsonBody.put("password", passwordInput);

        RequestBody requestBody = RequestBody.create(String.valueOf(jsonBody),JSON);

        Request request = new Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonData = new JSONObject(responseBody);

                // Log JSON data
                Log.d("MyApp", "JSON data: " + jsonData);
                return true;
            }else {
                return false;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    private String getUrl(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("serverUrl.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}