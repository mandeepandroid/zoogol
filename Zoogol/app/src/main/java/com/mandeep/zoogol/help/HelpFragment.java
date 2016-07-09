package com.mandeep.zoogol.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.instore.InstoreMainFragment;
import com.mandeep.zoogol.online_store.OnlineMainFragment;

/**
 * Created by mandeep on 3/23/2016.
 */
public class HelpFragment extends Fragment implements View.OnClickListener {

    private ImageView iv_navigationDrawer;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private RelativeLayout rl_call_now, rl_callback, rl_faq, rl_sendquery, rl_tips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help_fragment, container, false);


        initView();
        return view;
    }

    private void initView() {

        HomeActivity.rl_topbar.setVisibility(View.VISIBLE);

        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(this);

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_call_now:


                //  "9569696910"
                break;

            case R.id.rl_callback:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                RequestCallBackFragment callBackFragment = new RequestCallBackFragment();
                HomeActivity.helpFragmentStack.add(callBackFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, callBackFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_faq:
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                FaqFragment faqFragment = new FaqFragment();
                HomeActivity.helpFragmentStack.add(faqFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, faqFragment);
                mFragmentTransaction.commit();
                break;


            case R.id.rl_sendquery:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                SendQueryFragment mSendQueryFragment = new SendQueryFragment();
                HomeActivity.helpFragmentStack.add(mSendQueryFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mSendQueryFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_tips:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                CashBackFragment cashBackFragment = new CashBackFragment();
                HomeActivity.helpFragmentStack.add(cashBackFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, cashBackFragment);
                mFragmentTransaction.commit();


                break;

            case R.id.iv_navigationDrawer:

                HomeActivity.mDrawerLayout.openDrawer(HomeActivity.ll_drawer_list);

                break;


        }
    }

}