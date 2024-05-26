package com.example.swifty.utils;

import android.util.Log;

import com.example.swifty.models.CompanyModel;
import com.example.swifty.models.TransactionModel;
import com.example.swifty.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class Repository {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    public static UserModel getValidUser(String username, String password, String endpoint) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);

            RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) return null;
                assert response.body() != null;
                return gson.fromJson(response.body().string(), UserModel.class);
            }
        } catch (IOException | JsonSyntaxException | JSONException e) {
            Log.e("NetworkError", "Error validating user", e);
            return null;
        }
    }
    public static boolean isSuccessfulTransaction(TransactionModel transaction, String endpoint) throws IOException {
        String jsonBody = gson.toJson(transaction);
        // For debugging, print formatted JSON
        Gson gsonPrettyPrinting = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gsonPrettyPrinting.toJson(transaction));

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .header("Content-Type", "application/json") // Essential for some APIs
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                assert response.body() != null;
                System.out.println("Error Response Body: " + response.body().string());
            }

            return response.isSuccessful();
        }
    }
    public static List<CompanyModel> getCompanyData(String endpoint) {
        Request request = new Request.Builder()
                .url(endpoint)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Collections.emptyList();

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                JSONObject jsonData = new JSONObject(responseBody.string());

                List<CompanyModel> companies = new ArrayList<>();
                Iterator<String> companyKeys = jsonData.keys();
                while (companyKeys.hasNext()) {
                    String companyName = companyKeys.next();
                    // Get the data for this company
                    JSONObject companyData = jsonData.getJSONObject(companyName);
                    // Extract company details
                    String url = companyData.getJSONObject("info").getString("url");
                    // Extract product names into a list
                    JSONObject productList = companyData.getJSONObject("productList");
                    List<String> products = new ArrayList<>();
                    Iterator<String> productKeys = productList.keys();
                    while (productKeys.hasNext()) {
                        String productId = productKeys.next();
                        products.add(productList.getString(productId)); // Just get the name
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
            String jsonBody = gson.toJson(user); // Use Gson for serialization
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);
            System.out.println(jsonBody);
            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println(response);
                return response.isSuccessful();
            }
        } catch (IOException e) {
            Log.e("NetworkError", "Error creating user", e);
            return false;
        }
    }

}
