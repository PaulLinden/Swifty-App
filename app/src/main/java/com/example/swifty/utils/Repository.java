package com.example.swifty.utils;

import android.util.Log;

import com.example.swifty.models.CompanyModel;
import com.example.swifty.models.TransactionModel;
import com.example.swifty.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
* This class is responsible for making HTTP requests to the server.
* It provides methods for validating users, fetching company data, and creating users.
* */

public class Repository {
    // Constants
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    public static UserModel getValidUser(String username, String password, String endpoint) {
        try {
            // Create the JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);

            RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
            // Create the HTTP request
            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(requestBody)
                    .build();
            // Execute the request and parse the response
            try (Response response = client.newCall(request).execute()) {
                // Check if the response is successful
                if (!response.isSuccessful()) return null;
                //Assert that the response body is not null
                assert response.body() != null;
                //Return the parsed JSON object
                return gson.fromJson(response.body().string(), UserModel.class);
            }
        } catch (IOException | JsonSyntaxException | JSONException e) {
            Log.e("NetworkError", "Error validating user", e);
            return null;
        }
    }
    public static boolean isSuccessfulTransaction(TransactionModel transaction, String endpoint) throws IOException {
        // Convert the transaction object to JSON
        String jsonBody = gson.toJson(transaction);
        // Create the HTTP request
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .header("Content-Type", "application/json") // Essential for some APIs
                .build();
        // Execute the request and parse the response
        try (Response response = client.newCall(request).execute()) {
            //Return response status. Returns true if the response is successful
            return response.isSuccessful();
        }
    }
    public static List<CompanyModel> getCompanyData(String endpoint) {
        // Create the HTTP request
        Request request = new Request.Builder()
                .url(endpoint)
                .build();
        // Execute the request and parse the response
        try (Response response = client.newCall(request).execute()) {
            // Early return if not successful
            if (!response.isSuccessful()) return Collections.emptyList();
            // Extract the response body
            ResponseBody responseBody = response.body();
            // Parse the JSON response
            if (responseBody != null) {
                // Convert the response body to a JSON object
                JSONObject jsonData = new JSONObject(responseBody.string());
                // Extract the company data
                List<CompanyModel> companies = new ArrayList<>();
                // Iterate through the company keys
                Iterator<String> companyKeys = jsonData.keys();
                while (companyKeys.hasNext()) {
                    // Get the company name
                    String companyName = companyKeys.next();
                    // Get the data for this company
                    JSONObject companyData = jsonData.getJSONObject(companyName);
                    // Extract company details
                    String url = companyData.getJSONObject("info").getString("url");
                    // Extract product names into a list
                    JSONObject productList = companyData.getJSONObject("productList");
                    // Create a list to store product names
                    List<String> products = new ArrayList<>();
                    // Iterate through the product keys
                    Iterator<String> productKeys = productList.keys();
                    // Add product to the list
                    while (productKeys.hasNext()) {
                        // Get the product ID
                        String productId = productKeys.next();
                        products.add(productList.getString(productId));
                    }
                    // Create the CompanyModel object and add it to the list
                    companies.add(new CompanyModel(companyName, url, products));
                }

                return companies;
            } else {
                Log.e("NetworkError", "Empty response body received.");
                return Collections.emptyList();
            }
        } catch (IOException | JSONException e) {
            Log.e("NetworkError", "Error fetching company data: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public static boolean createUser(String endpoint, UserModel user) {
        try {
            // Convert the user object to JSON
            String jsonBody = gson.toJson(user);
            // Create the HTTP request
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(requestBody)
                    .build();
            // Execute the request and parse the response
            try (Response response = client.newCall(request).execute()) {
                //Return response status. Returns true if the response is successful
                return response.isSuccessful();
            }
        } catch (IOException e) {
            Log.e("NetworkError", "Error creating user", e);
            return false;
        }
    }

}
