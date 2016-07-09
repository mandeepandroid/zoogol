package com.mandeep.zoogol.user_profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.squareup.picasso.Picasso;

/**
 * Created by mandeep on 4/16/2016.
 */

//https://youtu.be/07IJKGk9NTI
public class FreeProductFragment extends Fragment {

    private ImageView iv_navigationDrawer;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private ImageView iv_free_product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.free_product_fragment, container, false);


        initView();

        return view;
    }

    private void initView() {
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, HomeActivity.profileFragmentStack.get(HomeActivity.profileFragmentStack.size() - 2));
                HomeActivity.profileFragmentStack.pop();
                mFragmentTransaction.commit();
            }
        });

        iv_free_product = (ImageView) view.findViewById(R.id.iv_free_product);
        Picasso.with(getActivity()).load("https://www.zoogol.in/profile/images/1300001.jpg").into(iv_free_product);
    }

}
