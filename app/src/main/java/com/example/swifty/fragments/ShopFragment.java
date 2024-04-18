package com.example.swifty.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swifty.R;
import com.example.swifty.models.CartItem;
import com.example.swifty.view_models.CartViewModel;
import com.example.swifty.models.CompanyModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopFragment extends Fragment {

    CompanyModel company;
    private CartViewModel cartViewModel;

    public ShopFragment() {
    }

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        if (getArguments() != null) {
            System.out.println(getArguments());
            try {
                company = (CompanyModel) getArguments().getSerializable("company");

                if (company != null) {
                    System.out.println("Company Name: " + company.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        TextView companyName = view.findViewById(R.id.nameTextView);
        ImageView companyImg = view.findViewById(R.id.companyImageView);
        LinearLayout productContainer = view.findViewById(R.id.productContainer);

        companyName.setText(company.getName());
        Picasso.get().load(company.getUrl()).into(companyImg);


        // Iterate over the product list and add views for each product
        for (String product : company.getProductList()) {
            try {
                // Parse the JSON string for the product information
                JSONObject productInfo = new JSONObject(product);
                String productName = productInfo.getString("name");
                String quantity = productInfo.getString("quantity");
                String price = productInfo.getString("price");

                // Create a new LinearLayout to hold the TextViews for each product
                LinearLayout productLayout = new LinearLayout(requireContext());
                productLayout.setOrientation(LinearLayout.VERTICAL);

                // Create TextViews for product name, price, and quantity
                TextView productNameTextView = new TextView(requireContext());
                productNameTextView.setText("Product: " + productName);

                TextView priceTextView = new TextView(requireContext());
                priceTextView.setText("Price: $" + price);

                TextView quantityTextView = new TextView(requireContext());
                quantityTextView.setText("Quantity: " + quantity);

                // Create an "Add" button for each product
                Button addButton = new Button(requireContext());
                addButton.setText("Add");

                addButton.setOnClickListener(v -> {
                    // Create a new CartItem and add it to the cart
                    CartItem cartItem = new CartItem(productName, Double.parseDouble(price) , 1);
                    cartViewModel.addToCart(cartItem);
                });

                // Set margins between button and text
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                buttonParams.topMargin = 30; // Adjust the top margin as needed
                addButton.setLayoutParams(buttonParams);

                // Add TextViews to the product layout
                productLayout.addView(productNameTextView);
                productLayout.addView(priceTextView);
                productLayout.addView(quantityTextView);

                // Add the "Add" button to the product layout
                productLayout.addView(addButton);

                // Set margins for the product layout
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 40, 0, 40); // Set margins (left, top, right, bottom)
                productLayout.setLayoutParams(layoutParams);

                // Add the product layout to the product container
                productContainer.addView(productLayout);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return view;
    }
}