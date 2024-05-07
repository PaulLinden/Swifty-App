package com.example.swifty.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Endpoints {

    private static String BASE_URL;

    public static void initBaseUrl(Context context) throws IOException {

        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
           BASE_URL = properties.getProperty("BASE_URL");
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static String getLoginUrl(Context context) throws IOException {

        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            String login = properties.getProperty("LOGIN");

            return BASE_URL.concat(login);
        } catch (IOException e) {
            e.fillInStackTrace();
            return null;
        }
    }

    public static String getTransactionUrl(Context context) throws IOException {

        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            String transaction = properties.getProperty("TRANSACTION");

            return BASE_URL.concat(transaction);
        } catch (IOException e) {
            e.fillInStackTrace();
            return null;
        }
    }

    public static String getCompanyUrl(Context context) throws IOException {
        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            String company = properties.getProperty("COMPANY");

            return BASE_URL.concat(company);
        } catch (IOException e) {
            e.fillInStackTrace();
            return null;
        }
    }
}
