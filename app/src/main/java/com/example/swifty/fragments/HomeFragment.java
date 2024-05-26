package com.example.swifty.fragments;

import static com.example.swifty.utils.Endpoints.getCompanyUrl;
import static com.example.swifty.utils.Repository.getCompanyData;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        companyUrl = getCompanyUrl(this.requireContext());
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                List<CompanyModel> companyList = getCompanyData(companyUrl); // Use the new method
                System.out.println(companyList.size());
                for (int i = 0; i < companyList.size(); i++) {
                    CompanyModel company = companyList.get(i);
                    String url = company.getUrl();

                    int finalIndex = i;
                    activity.runOnUiThread(() -> {
                        Glide.with(this)
                                .load(url)
                                .apply(RequestOptions.bitmapTransform(transformation))
                                .into(views[finalIndex]);

                        views[finalIndex].setOnClickListener(v -> {
                            ShopFragment fragment = new ShopFragment();
                            Bundle args = new Bundle();
                            args.putSerializable(Constants.COMPANY, company); // Pass the CompanyModel directly
                            fragment.setArguments(args);
                            activity.runOnUiThread(() -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_companyDetailFragment, args));
                        });
                    });
                }
            } catch (Exception e) { // Catch a more general Exception type if needed
                Log.e("DataError", "Error fetching or processing company data", e);
            } finally {
                executor.shutdown(); // Ensure executor shutdown
            }
        });

        return view;
    }
}