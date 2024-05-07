package com.example.swifty.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREFERENCES_NAME = "UserPrefs";
    private static final String KEY_USERID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTHDATE = "birthdate";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserCredentials(String userId, String username, String firstName, String lastName, String email, String birthDate) {
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_BIRTHDATE, birthDate);
        editor.apply();
    }

    public String[] getUserCredentials() {
        String userId = sharedPreferences.getString(KEY_USERID, "");
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String firstName = sharedPreferences.getString(KEY_FIRSTNAME, "");
        String lastName = sharedPreferences.getString(KEY_LASTNAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String birthDate = sharedPreferences.getString(KEY_BIRTHDATE, "");
        return new String[]{userId, username, firstName, lastName, email, birthDate};
    }
}
