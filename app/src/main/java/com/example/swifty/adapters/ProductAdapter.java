package com.example.swifty.adapters;

import static com.example.swifty.utils.strings.ShopStrings.getStrings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swifty.R;
import com.example.swifty.models.CartItem;
import com.example.swifty.models.CompanyModel;
import com.example.swifty.utils.Constants;
import com.example.swifty.utils.strings.ShopStrings;
import com.example.swifty.view_models.CartViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<String> productList;
    private CartViewModel cartViewModel;
    private CompanyModel companyModel;

    public ProductAdapter(List<String> productList, CartViewModel cartViewModel, CompanyModel companyModel) {
        this.productList = productList;
        this.cartViewModel = cartViewModel;
        this.companyModel = companyModel;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        String productString = productList.get(position);
        holder.bind(productString, cartViewModel, companyModel);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    //Inner class for ViewHolder
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, priceTextView, quantityTextView;
        Button addButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            addButton = itemView.findViewById(R.id.addButton);
        }

        // Bind data to the ViewHolder
        public void bind(String productString, CartViewModel cartViewModel, CompanyModel companyModel) {
            try {
                JSONObject productInfo = new JSONObject(productString);
                String productName = productInfo.getString(Constants.NAME);
                String quantity = productInfo.getString(Constants.QUANTITY);
                String price = productInfo.getString(Constants.PRICE);
                ShopStrings shopStrings = getStrings(productName, price, quantity);

                productNameTextView.setText(shopStrings.productText());
                priceTextView.setText(shopStrings.priceText());
                quantityTextView.setText(shopStrings.quantityText());

                addButton.setOnClickListener(v -> {
                    CartItem cartItem = new CartItem(companyModel.getCompanyName(), productName, Double.parseDouble(price), 1);
                    cartViewModel.addToCart(cartItem);
                });
            } catch (JSONException e) {
                // ... (handle JSON parsing errors)
            }
        }
    }
}



