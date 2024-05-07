package com.example.swifty.utils;

import com.example.swifty.models.CartItem;
import com.example.swifty.models.TransactionModel;
import com.example.swifty.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Repository {

    public static boolean isValidUser(String usernameInput, String passwordInput, String endpoint, UserModel currentUser) throws JSONException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", usernameInput);
        jsonBody.put("password", passwordInput);

        RequestBody requestBody = RequestBody.create(String.valueOf(jsonBody), JSON);

        Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonData = new JSONObject(responseBody);

                //Create user object with the fetched user data
                currentUser.setId(jsonData.getLong("id"));
                currentUser.setUserName(jsonData.getString("username"));
                currentUser.setEmail(jsonData.getString("email"));
                currentUser.setFirstName(jsonData.getString("name"));
                currentUser.setLastName(jsonData.getString("lastName"));
                currentUser.setBirthDate(jsonData.getString("birtDate"));

                return true;
            } else {
                return false;
            }
        } catch (IOException | JSONException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public static boolean makeTransaction(TransactionModel transaction, String endpoint) throws JSONException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        //Create request body
        JSONObject jsonBody = new JSONObject();
        //User
        jsonBody.put("userId", transaction.getUser().getId());
        jsonBody.put("userEmail", transaction.getUser().getEmail());
        //Time of transaction
        jsonBody.put("dateTime", transaction.getDateTime());
        //Items in cart
        JSONArray cartItemsArray = new JSONArray();
        for (CartItem cartItem : transaction.getCartItems()) {
            JSONObject cartItemJson = new JSONObject();
            cartItemJson.put("name", cartItem.getProductName());
            cartItemJson.put("companyName", cartItem.getCompanyName());
            cartItemJson.put("price", cartItem.getPrice());
            cartItemJson.put("quantity", cartItem.getQuantity());
            cartItemsArray.put(cartItemJson);
        }
        jsonBody.put("cartItems", cartItemsArray);

        RequestBody requestBody = RequestBody.create(String.valueOf(jsonBody), JSON);

        System.out.println(jsonBody);
        //Build request
        Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .build();
        System.out.println(request);
        //Get response status
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public static List<JSONObject> getCompanyData(String endpoint) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpoint)
                .header("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonData = new JSONObject(responseBody);
                Iterator<String> companyKeys = jsonData.keys();
                List<JSONObject> companyList = new ArrayList<>(); // Create a list to store company objects
                while (companyKeys.hasNext()) {
                    String key = companyKeys.next(); // Get the next key
                    JSONObject company = jsonData.getJSONObject(key); // Get the object associated with the key
                    companyList.add(company); // Add the company object to the list
                }
                return companyList;
            }
        } catch (IOException | JSONException e) {
            e.fillInStackTrace();
            return null;
        }
        return null;
    }
}
