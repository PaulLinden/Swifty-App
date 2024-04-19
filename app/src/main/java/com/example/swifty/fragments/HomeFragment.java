package com.example.swifty.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.swifty.R;
import com.example.swifty.models.CompanyModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    String companyUrl = null;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            companyUrl = getUrl(this.requireContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        List<String> imageUrls = new ArrayList<>();
        ImageView [] views = {
                view.findViewById(R.id.image_view1),
                view.findViewById(R.id.image_view2),
                view.findViewById(R.id.image_view3),
                view.findViewById(R.id.image_view4),
                view.findViewById(R.id.image_view5),};

        Thread dataThread = new Thread(() -> {
            try {
                List<JSONObject> companyList = getCompanyList();

                int index = 0;
                for (JSONObject company : companyList) {
                    JSONObject info = company.getJSONObject("info"); // Get the "info" object
                    JSONObject productList = company.getJSONObject("productList");

                    String industry = info.getString("industry");
                    String name = info.getString("name");
                    String url = info.getString("url");

                    // Initialize a list to hold the products
                    List<String> products = new ArrayList<>();

                    // Iterate over the keys of the "productList" object
                    Iterator<String> keys = productList.keys();
                    while (keys.hasNext()) {
                        // Get the key (product name)
                        String key = keys.next();
                        // Add the product to the list
                        products.add(productList.getString(key));
                    }


                    CompanyModel newCompany = new CompanyModel();
                    newCompany.setIndustry(industry);
                    newCompany.setName(name);
                    newCompany.setUrl(url);
                    newCompany.setProductList(products);

                    imageUrls.add(url);

                    int finalIndex = index;
                    requireActivity().runOnUiThread(() -> {
                        Picasso.get().load(imageUrls.get(finalIndex)).into(views[finalIndex]);

                        views[finalIndex].setOnClickListener(v -> {
                            ShopFragment fragment = new ShopFragment();
                            Bundle args = new Bundle();
                            args.putSerializable("company", newCompany);
                            fragment.setArguments(args);

                            requireActivity().runOnUiThread(() -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_companyDetailFragment, args));
                        });
                    });

                    index++;
                }
            } catch (JSONException e) {
                //throw new RuntimeException(e);
                System.out.println(e);
            }
        });
        dataThread.start();

        try {
            dataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return view;
    }

    @NonNull
    private List<JSONObject> getCompanyList() throws JSONException {
        JSONObject companies = Objects.requireNonNull(getCompanies());
        Iterator<String> companyKeys = companies.keys();

        List<JSONObject> companyList = new ArrayList<>(); // Create a list to store company objects

        while(companyKeys.hasNext()){
            String key = companyKeys.next(); // Get the next key
            JSONObject company = companies.getJSONObject(key); // Get the object associated with the key

            companyList.add(company); // Add the company object to the list
        }
        return companyList;
    }

    private JSONObject getCompanies() throws JSONException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(companyUrl)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonData = new JSONObject(responseBody);

                // Log JSON data
                Log.d("MyApp", "JSON data: " + jsonData);
                return jsonData;
            }else {
                Log.d("MyApp", "!!!!!!!!!Couldn't fetch data for companies!!!!!!!!!!!!!!!!!");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private String getUrl(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("companyUrl.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}