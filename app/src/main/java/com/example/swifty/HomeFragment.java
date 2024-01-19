package com.example.swifty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {}
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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
}