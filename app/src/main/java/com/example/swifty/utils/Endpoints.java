package com.example.swifty.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Endpoints {

    public static String getLoginUrl(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("loginUrl.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static String getTransactionUrl(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("transactionUrl.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static String getCompanyUrl(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("companyUrl.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        } catch (IOException e) {
            return e.toString();
        }
    }
}
