package com.example.swifty.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
* This class contains the endpoints for the API. It initializes the BASE_URL from the config.properties file
* and provides methods to retrieve the endpoints for different API calls.
* Methods are static to avoid instantiation of the class.
* Methods available are: getLoginUrl, getTransactionUrl, getCompanyUrl, getCreateUserUrl
* */

public class Endpoints {

    private static String BASE_URL;

    public static void initBaseUrl(Context context) {
        // Load the BASE_URL from config.properties
        try (InputStream inputStream = context.getAssets().open("config.properties")) {
            // Initialize the Properties object
            Properties properties = new Properties();
            // Load the properties from the input stream
            properties.load(inputStream);
            // Get the BASE_URL from the properties
            BASE_URL = properties.getProperty("BASE_URL");
        } catch (IOException e) {
            Log.e("ConfigError", "Failed to load config.properties", e);
        }
    }

    // Get the endpoints for different API calls
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
                // Initialize the Properties object
                Properties properties = new Properties();
                // Load the properties from the input stream
                properties.load(inputStream);
                // Get the BASE_URL from the properties
                return BASE_URL + properties.getProperty(propertyName);
            } catch (IOException e) {
                Log.e("ConfigError", "Failed to load config.properties", e);
            }
        }
        return null;
    }
}
