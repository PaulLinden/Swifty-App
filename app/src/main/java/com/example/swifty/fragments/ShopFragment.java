package com.example.swifty.fragments;

import static com.example.swifty.utils.strings.ShopStrings.getStrings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.models.CartItem;
import com.example.swifty.models.CompanyModel;
import com.example.swifty.utils.Constants;
import com.example.swifty.utils.strings.ShopStrings;
import com.example.swifty.view_models.CartViewModel;

import org.json.JSONException;
import org.json.JSONObject;

/*
* Here the specific company is displayed with all the products.
* The user can add products to the cart.
* */
public class ShopFragment extends Fragment {

    private CompanyModel company;
    private CartViewModel cartViewModel;
    private Context context;
    MainActivity activity;

    public ShopFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) requireActivity();
        context = requireContext();
        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);
        if (getArguments() != null) {
            System.out.println(getArguments());
            try {
                company = (CompanyModel) getArguments().getSerializable(Constants.COMPANY);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        //Get views
        ImageView companyImageView = view.findViewById(R.id.companyImageView);
        LinearLayout productContainer = view.findViewById(R.id.productContainer);
        //Set navbar visible
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView));
        //Load image in to imageview
        Glide.with(this).load(company.getUrl()).into(companyImageView);
        // Iterate over the product list and add views for each product
        for (String product : company.getProductList()) {
            try {
                // Parse the product JSON object
                JSONObject productInfo = new JSONObject(product);
                // Get the product name, price, and quantity
                String productName = productInfo.getString(Constants.NAME),
                        quantity = productInfo.getString(Constants.QUANTITY),
                        price = productInfo.getString(Constants.PRICE);
                // Create a new product layout
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                TextView productNameTextView = new TextView(context),
                        priceTextView = new TextView(context),
                        quantityTextView = new TextView(context);
                LinearLayout productLayout = new LinearLayout(context);
                ShopStrings shopStrings = getStrings(productName, price, quantity);
                Button addButton = new Button(context);
                //Set text for fields
                productNameTextView.setText(shopStrings.productText());
                priceTextView.setText(shopStrings.priceText());
                quantityTextView.setText(shopStrings.quantityText());
                // Create an "Add" button for each product
                addButton.setText(shopStrings.add());
                addButton.setOnClickListener(v -> {
                    // Create a new CartItem and add it to the cart
                    CartItem cartItem = new CartItem(company.getCompanyName(), productName, Double.parseDouble(price), 1);
                    cartViewModel.addToCart(cartItem);
                });
                // Set margins between button and text
                buttonParams.topMargin = 30; // Adjust the top margin as needed
                addButton.setLayoutParams(buttonParams);
                addButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.light_green, null));
                addButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dark_blue, null));
                // Add TextViews to the product layout
                productLayout.setOrientation(LinearLayout.VERTICAL);
                productLayout.addView(productNameTextView);
                productLayout.addView(priceTextView);
                productLayout.addView(quantityTextView);
                // Add the "Add" button to the product layout
                productLayout.addView(addButton);
                // Set margins for the product layout
                layoutParams.setMargins(0, 40, 0, 40); // Set margins (left, top, right, bottom)
                productLayout.setLayoutParams(layoutParams);
                // Add the product layout to the product container
                productContainer.addView(productLayout);
            } catch (JSONException e) {
                e.fillInStackTrace();
            }
        }

        return view;
    }
}