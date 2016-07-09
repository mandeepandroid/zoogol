package com.mandeep.zoogol.explore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.help.CashBackFragment;
import com.mandeep.zoogol.help.FaqFragment;
import com.mandeep.zoogol.help.RequestCallBackFragment;
import com.mandeep.zoogol.help.SendQueryFragment;

/**
 * Created by mandeep on 4/30/2016.
 */
public class ExploreMainFragment extends Fragment implements View.OnClickListener {


    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private RelativeLayout rl_call_now, rl_callback, rl_faq, rl_sendquery, rl_tips, rl_suggests;
    public static String urlToLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore_fragment, container, false);


        initView();
        return view;
    }

    private void initView() {

        HomeActivity.rl_topbar.setVisibility(View.GONE);

        rl_call_now = (RelativeLayout) view.findViewById(R.id.rl_call_now);
        rl_call_now.setOnClickListener(this);

        rl_callback = (RelativeLayout) view.findViewById(R.id.rl_callback);
        rl_callback.setOnClickListener(this);

        rl_faq = (RelativeLayout) view.findViewById(R.id.rl_faq);
        rl_faq.setOnClickListener(this);

        rl_sendquery = (RelativeLayout) view.findViewById(R.id.rl_sendquery);
        rl_sendquery.setOnClickListener(this);

        rl_tips = (RelativeLayout) view.findViewById(R.id.rl_tips);
        rl_tips.setOnClickListener(this);

        rl_suggests = (RelativeLayout) view.findViewById(R.id.rl_suggests);
        rl_suggests.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_call_now:

                urlToLoad = "https://www.zoogol.in/newz/app/what-is-zoogol.php";

                loadFragment();
                break;

            case R.id.rl_callback:

                urlToLoad = "https://www.zoogol.in/newz/app/why-to-zoogol.php";
                loadFragment();
                break;

            case R.id.rl_faq:
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                YouTubeActivity tubeActivity = new YouTubeActivity();
                HomeActivity.homeFragmentStack.add(tubeActivity);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, tubeActivity);
                mFragmentTransaction.commit();
                break;


            case R.id.rl_sendquery:

                urlToLoad = "https://www.zoogol.in/newz/app/what-is-zoogol.php";
                loadFragment();
                break;

            case R.id.rl_tips:

                urlToLoad = "https://www.zoogol.in/newz/app/what-is-zoogol.php";
                loadFragment();


                break;

            case R.id.rl_suggests:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                SuggestFragment suggestFragment = new SuggestFragment();
                HomeActivity.homeFragmentStack.add(suggestFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, suggestFragment);
                mFragmentTransaction.commit();
                break;


        }
    }

    private void loadFragment() {

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        WebFragment mWebFragment = new WebFragment();
        HomeActivity.homeFragmentStack.add(mWebFragment);
        mFragmentTransaction.replace(R.id.containerRelativeLayout, mWebFragment);
        mFragmentTransaction.commit();
    }

}