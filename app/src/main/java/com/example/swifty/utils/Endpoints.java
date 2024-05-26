package com.example.swifty.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Endpoints {

    private static String BASE_URL;

    public static void initBaseUrl(Context context) {
        try (InputStream inputStream = context.getAssets().open("config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            BASE_URL = properties.getProperty("BASE_URL");
        } catch (IOException e) {
            Log.e("ConfigError", "Failed to load config.properties", e);
        }
    }

    public static String getLoginUrl(Context context) {
        return getUrlForProperty(context, "LOGIN");
    }

    public static String getTransactionUrl(Context context) {
        return getUrlForProperty(context, "TRANSACTION");
    }

    public static String getCompanyUrl(Context context) {
        return getUrlForProperty(context, "COMPANY");
    }

    public static String getCreateUserUrl(Context context) {
        return getUrlForProperty(context, "CREATE_USER");
    }

    // Helper method to avoid code duplication
    private static String getUrlForProperty(Context context, String propertyName) {
        if (BASE_URL == null) {
            initBaseUrl(context); // Ensure BASE_URL is initialized
        }

        if (BASE_URL != null) { // Check if initialization was successful
            try (InputStream inputStream = context.getAssets().open("config.properties")) {
                Properties properties = new Properties();
                properties.load(inputStream);
                return BASE_URL + properties.getProperty(propertyName);
            } catch (IOException e) {
                Log.e("ConfigError", "Failed to load config.properties", e);
            }
        }
        return null;
    }
}
