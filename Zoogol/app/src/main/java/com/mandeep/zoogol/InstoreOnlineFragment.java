package com.mandeep.zoogol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mandeep.zoogol.instore.InstoreMainFragment;
import com.mandeep.zoogol.online_store.OnlineMainFragment;

/**
 * Created by mandeep on 3/27/2016.
 */
public class InstoreOnlineFragment extends Fragment implements View.OnClickListener {


    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private TextView tv_instore_button, tv_online_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_store_activity, container, false);

        initView();
        return view;
    }

    private void initView() {

        tv_instore_button = (TextView) view.findViewById(R.id.tv_instore_button);
        tv_instore_button.setOnClickListener(this);

        tv_online_button = (TextView) view.findViewById(R.id.tv_online_button);
        tv_online_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_instore_button:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                InstoreMainFragment mInstoreMainFragment = new InstoreMainFragment();

                mFragmentTransaction.replace(R.id.containerRelativeLayout, mInstoreMainFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.tv_online_button:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                OnlineMainFragment mOnlineMainFragment = new OnlineMainFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mOnlineMainFragment);
                mFragmentTransaction.commit();
                break;


        }
    }
}
