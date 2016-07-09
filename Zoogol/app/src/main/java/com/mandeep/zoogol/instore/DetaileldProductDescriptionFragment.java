package com.mandeep.zoogol.instore;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.InstoreCombineFragment;
import com.mandeep.zoogol.OnlineCombineFragment;
import com.mandeep.zoogol.R;

/**
 * Created by mandeep on 5/26/2016.
 */
public class DetaileldProductDescriptionFragment extends Fragment {

    private WebView webview;
    private View view;
    private ProgressDialog pDialog;
    private RelativeLayout rl_container;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment fragment;
    private TextView tv_detail, tv_photos, tv_deals, tv_map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.both_product_fragment, container, false);


        initView();

        return view;
    }

    private void initView() {
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        tv_detail = (TextView) view.findViewById(R.id.tv_detail);
        tv_photos = (TextView) view.findViewById(R.id.tv_photos);
        tv_deals = (TextView) view.findViewById(R.id.tv_deals);
        tv_map = (TextView) view.findViewById(R.id.tv_map);
        fragment = new ProductDetailFragment();
        changeFragment();
        tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ProductDetailFragment();
                changeFragment();
                changeTabColor(true, false, false, false);
            }
        });

        tv_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ProductImageFragment();
                changeFragment();
                changeTabColor(false, true, false, false);
            }
        });

        tv_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTabColor(false, false, true, false);
            }
        });
        tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new MapFragment();
                changeFragment();
                changeTabColor(false, false, false, true);
            }
        });


    }

    private void changeFragment() {
        mFragmentManager = getChildFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.rl_container, fragment);
        mFragmentTransaction.commit();
    }

    public void changeTabColor(boolean detail, boolean photo, boolean deals, boolean map) {

        if (detail) {
            tv_detail.setBackgroundColor(Color.parseColor("#4fa6c2"));
            tv_detail.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tv_detail.setBackgroundColor(Color.parseColor("#e6e7e8"));
            tv_detail.setTextColor(Color.parseColor("#000000"));
        }
        if (photo) {
            tv_photos.setBackgroundColor(Color.parseColor("#4fa6c2"));
            tv_photos.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tv_photos.setBackgroundColor(Color.parseColor("#e6e7e8"));
            tv_photos.setTextColor(Color.parseColor("#000000"));
        }

        if (deals) {
            tv_deals.setBackgroundColor(Color.parseColor("#4fa6c2"));
            tv_deals.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tv_deals.setBackgroundColor(Color.parseColor("#e6e7e8"));
            tv_deals.setTextColor(Color.parseColor("#000000"));
        }
        if (map) {
            tv_map.setBackgroundColor(Color.parseColor("#4fa6c2"));
            tv_map.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tv_map.setBackgroundColor(Color.parseColor("#e6e7e8"));
            tv_map.setTextColor(Color.parseColor("#000000"));
        }


    }
}
