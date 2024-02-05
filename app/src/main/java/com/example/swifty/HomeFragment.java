package com.example.swifty;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
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

        //Behöver testas!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Kanske skicka detta direkt från hämt metoden eller extrahera
        //Till en egen method senare som hanterar dynamisk hämtning av data.

        new Thread(() -> {
            try {
                JSONObject companies = Objects.requireNonNull(getCompanies()).getJSONObject("companies");
                Iterator<String> companyKeys = companies.keys();

                while(companyKeys.hasNext()){

                    String info = companies.getString("info");
                    String productList = companies.getString("productList");

                    // Process the extracted values (you can log or do other operations)
                    Log.d("MyApp", "Name: " + info);
                    Log.d("MyApp", "Location: " + productList);
                    Log.d("MyApp", "---");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //Lägg till dynamsik inhämtning och lägg till där efter
        //URL finns i produkt info.

        List<String> imageUrls = Arrays.asList(
                /*netonet*/    "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.c1UFdRSLG2QQeOBvzVcWBQAAAA%26pid%3DApi&f=1&ipt=19f98810ce1c0e03c305437ad314650d9519013e1713af0a6e7559a110bb42aa&ipo=images",
                /*ikea*/       "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.explicit.bing.net%2Fth%3Fid%3DOIP.l5wiwIZ_Ya2J5NHsi7svzQAAAA%26pid%3DApi&f=1&ipt=e7d3266441b8a348db0518cea546f8116d6d8a67e180c94b3163ca870315c49e&ipo=images",
                /*stadium*/    "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.9lSxO3X-48MLYsm21UuQdwAAAA%26pid%3DApi&f=1&ipt=84cd3fae696331fbd1b88b0c57060041d4b8e6a2994c6a62fe3d8e10149fb2c3&ipo=images",
                /*guldfynd*/   "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.RVV2w1Yj6Db3mv_7IhXQYQAAAA%26pid%3DApi&f=1&ipt=556a7b0bc7a0131d077988c8237243d41596a73b95b71653602bd2adbef126a1&ipo=images",
                /*h&m*/         "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.d_MK5ElPAzhxUps0pxdYzgAAAA%26pid%3DApi&f=1&ipt=5ce74ee9ef305f5a7e3904d8fc60b62f3bc4c7845a820c01b1aa92f5b0247b28&ipo=images"
        );

        ImageView imageView1 = view.findViewById(R.id.image_view1);
        ImageView imageView2 = view.findViewById(R.id.image_view2);
        ImageView imageView3 = view.findViewById(R.id.image_view3);
        ImageView imageView4 = view.findViewById(R.id.image_view4);
        ImageView imageView5 = view.findViewById(R.id.image_view5);

        Picasso.get().load(imageUrls.get(0)).into(imageView1);
        Picasso.get().load(imageUrls.get(1)).into(imageView2);
        Picasso.get().load(imageUrls.get(2)).into(imageView3);
        Picasso.get().load(imageUrls.get(3)).into(imageView4);
        Picasso.get().load(imageUrls.get(4)).into(imageView5);

        return view;
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
                Log.d("MyApp", "!!!!!!!!!Couldnt fetch data for companies!!!!!!!!!!!!!!!!!");
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