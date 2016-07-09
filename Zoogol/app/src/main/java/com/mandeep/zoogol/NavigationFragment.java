package com.mandeep.zoogol;

import android.app.ProgressDialog;
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

import com.mandeep.zoogol.share.ShareFragment;

/**
 * Created by mandeep on 5/5/2016.
 */
public class NavigationFragment extends Fragment {

    private WebView webview;
    private View view;
    private ProgressDialog pDialog;
    private RelativeLayout rl_container;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private TextView tv_online, tv_instore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.both_fragment, container, false);


        initView();

        return view;
    }

    private void initView() {

        tv_online = (TextView) view.findViewById(R.id.tv_online);
        tv_instore = (TextView) view.findViewById(R.id.tv_instore);

        tv_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getChildFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                OnlineCombineFragment combineFragment = new OnlineCombineFragment();
                mFragmentTransaction.replace(R.id.rl_container, combineFragment);
                mFragmentTransaction.commit();
            }
        });

        tv_instore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFragmentManager = getChildFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                InstoreCombineFragment instoreCombineFragment = new InstoreCombineFragment();
                mFragmentTransaction.replace(R.id.rl_container, instoreCombineFragment);
                mFragmentTransaction.commit();

            }
        });

        mFragmentManager = getChildFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        OnlineCombineFragment combineFragment = new OnlineCombineFragment();
        mFragmentTransaction.replace(R.id.rl_container, combineFragment);
        mFragmentTransaction.commit();


    }
}
