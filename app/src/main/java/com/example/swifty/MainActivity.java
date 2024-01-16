package com.example.swifty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView usernameInput = findViewById(R.id.loginUsername);
        TextView passwordInput = findViewById(R.id.loginPassword);

        boolean isUser = isValidUser(usernameInput.getText().toString(), passwordInput.getText().toString());

        if (isUser){
            //Navigate to homepage
        }else {
            // Create an AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            // Set the dialog title and message
            builder.setTitle("Error!")
                    .setMessage("username or password is not valid.");
        }
    }

    public boolean isValidUser(String usernameInput, String passwordInput){
        if(true){
            return true;
        }
        return false;
    }

}