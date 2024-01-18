package com.example.swifty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView usernameInput = findViewById(R.id.loginUsername);
        TextView passwordInput = findViewById(R.id.loginPassword);
        Button submit = findViewById(R.id.loginButton);

        submit.setOnClickListener(v -> new Thread(() -> {
            boolean isUser;

            try {
                isUser = isValidUser(usernameInput.getText().toString(), passwordInput.getText().toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Log.d("MyApp", "IS USER" + isUser);
            if (isUser) {
                //Navigate to homepage
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                //pass data to the new activity
                //intent.putExtra("key", "value");

                // Start the new activity
                startActivity(intent);

            } else {
                runOnUiThread(() -> {
                    // Display an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error!").setMessage("Username or password is not valid.");
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        // You can add code to handle the "OK" button click if needed
                        dialog.dismiss(); // Dismiss the dialog
                    });
                    builder.show(); // Show the dialog
                });
            }
        }).start());
    }

    public boolean isValidUser(String usernameInput, String passwordInput) throws JSONException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String url = "adress";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", usernameInput);
        jsonBody.put("password", passwordInput);

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonBody));

        Request request = new Request.Builder()
                .url(url)
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
}