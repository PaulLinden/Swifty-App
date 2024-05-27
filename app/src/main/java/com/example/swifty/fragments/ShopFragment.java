package com.example.swifty.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swifty.R;
import com.example.swifty.activities.MainActivity;
import com.example.swifty.adapters.ProductAdapter;
import com.example.swifty.models.CartItem;
import com.example.swifty.models.CompanyModel;
import com.example.swifty.utils.Constants;
import com.example.swifty.utils.strings.ShopStrings;
import com.example.swifty.view_models.CartViewModel;

/*
 * Here the specific company is displayed with all the products.
 * The user can add products to the cart.
 * */
public class ShopFragment extends Fragment {

    private CompanyModel company;
    private CartViewModel cartViewModel;
    private MainActivity activity;

    public ShopFragment() {
    } // Empty constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) requireActivity();
        cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);
        assert getArguments() != null;
        company = (CompanyModel) getArguments().getSerializable(Constants.COMPANY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        Context context = requireContext();
        // Get references to UI elements
        ImageView companyImageView = view.findViewById(R.id.companyImageView);
        RecyclerView productRecyclerView = view.findViewById(R.id.productContainer);
        // Set the visibility of the bottom navigation bar
        activity.runOnUiThread(() -> activity.setBottomNavigationBarVisibility(true, activity.bottomNavigationView));
        Glide.with(this).load(company.getUrl()).into(companyImageView);

        productRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        ProductAdapter adapter = new ProductAdapter(company.getProductList(), cartViewModel, company);
        productRecyclerView.setAdapter(adapter);

        return view;
    }

    private void addViewsToLayout(LinearLayout productLayout, TextView productNameTextView, TextView priceTextView, TextView quantityTextView, Button addButton, LinearLayout.LayoutParams layoutParams) {
        productLayout.addView(productNameTextView);
        productLayout.addView(priceTextView);
        productLayout.addView(quantityTextView);
        productLayout.addView(addButton);
        productLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.round_corners_black_border, null));
        productLayout.setGravity(Gravity.CENTER);
        productLayout.setLayoutParams(layoutParams);
    }

    private void createAddButton(Button addButton, ShopStrings shopStrings, LinearLayout.LayoutParams buttonParams, String productName, String price) {
        addButton.setText(shopStrings.add());
        buttonParams.topMargin = 30; // Adjust the top margin as needed
        addButton.setLayoutParams(buttonParams);
        addButton.setHighlightColor(ResourcesCompat.getColor(getResources(), R.color.dark_blue, null));
        addButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.light_green, null));
        addButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dark_blue, null));
        addButton.setFocusableInTouchMode(true);

        addButton.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(company.getCompanyName(), productName, Double.parseDouble(price), 1);
            cartViewModel.addToCart(cartItem);;
        });
    }

    // Method to style TextViews
    private void styleTextView(TextView textView, String text, int textSize, int gravity) {
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setTextSize(textSize);
        textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.dark_blue, null));
    }
}