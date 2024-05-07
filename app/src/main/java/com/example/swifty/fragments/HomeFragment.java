package com.example.swifty.fragments;

import static com.example.swifty.utils.Endpoints.getCompanyUrl;
import static com.example.swifty.utils.Repository.getCompanyData;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.models.CompanyModel;
import com.example.swifty.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class HomeFragment extends Fragment {
    private String companyUrl = null;
    MainActivity activity;
    MultiTransformation<Bitmap> transformation = new MultiTransformation<>(
            new RoundedCornersTransformation(180, 20, RoundedCornersTransformation.CornerType.ALL)
    );

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) requireActivity();
        try {
            companyUrl = getCompanyUrl(this.requireContext());
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView[] views = {
                view.findViewById(R.id.image_view1),
                view.findViewById(R.id.image_view2),
                view.findViewById(R.id.image_view3),
                view.findViewById(R.id.image_view4),
                view.findViewById(R.id.image_view5),};
        //Set navbar visible
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView));
        //Get company data
        Thread dataThread = new Thread(() -> {
            try {
                List<JSONObject> companyList = getCompanyData(companyUrl);
                int index = 0;
                assert companyList != null;
                for (JSONObject company : companyList) {
                    //Json objects info and product list
                    JSONObject info = company.getJSONObject(Constants.INFO),
                            productList = company.getJSONObject(Constants.PRODUCT_LIST);
                    //Company specifics
                    String companyName = info.getString(Constants.NAME),
                            url = info.getString(Constants.URL);
                    // Initialize a list to hold the products and img-urls
                    List<String> products = new ArrayList<>();
                    // Iterate over the keys of the "productList" object
                    Iterator<String> keys = productList.keys();
                    while (keys.hasNext()) {
                        // Get the key (product name)
                        String key = keys.next();
                        // Add the product to the list
                        products.add(productList.getString(key));
                    }
                    //Init company object
                    CompanyModel newCompany = new CompanyModel(companyName, url, products);
                    //Update UI
                    int finalIndex = index;
                    activity.runOnUiThread(() -> {
                        //Load image into imageview
                        Glide.with(this)
                                .load(url)
                                .apply(RequestOptions.bitmapTransform(transformation))
                                .into(views[finalIndex]);
                        //Make images clickable
                        views[finalIndex].setOnClickListener(v -> {
                            ShopFragment fragment = new ShopFragment();
                            Bundle args = new Bundle();
                            args.putSerializable(Constants.COMPANY, newCompany);
                            fragment.setArguments(args);
                            activity.runOnUiThread(() -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_companyDetailFragment, args));
                        });
                    });
                    index++;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        dataThread.start();
        //Waits for this thread to die.
        try {
            dataThread.join();
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
        return view;
    }
}