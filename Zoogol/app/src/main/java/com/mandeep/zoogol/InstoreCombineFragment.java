package com.mandeep.zoogol;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mandeep.zoogol.bean.OnlineStoresBean;
import com.mandeep.zoogol.online_store.OnlineCustomAdapter;

import java.util.ArrayList;

/**
 * Created by mandeep on 5/5/2016.
 */
public class InstoreCombineFragment extends Fragment implements View.OnClickListener {

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private TextView tv_instore_button;
    private TabLayout tabLayout;
    private GridView gv_online_main;
    private LinearLayout ll_scroll;
    private ArrayList<OnlineStoresBean> mOnlineStoresBeen = new ArrayList<>();
    private OnlineCustomAdapter mCustomAdapter;
    public static String storeSelected, storeVisitingLink;
    private ImageView iv_grid_icon, iv_list_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.online_combine_fragment, container, false);

        initView();
        return view;
    }

    private void initView() {
    }


    private void showProgressDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_grid_icon:

                gv_online_main.setNumColumns(2);
                mCustomAdapter = new OnlineCustomAdapter(getActivity(), mOnlineStoresBeen);
                gv_online_main.setAdapter(mCustomAdapter);

                break;

        }
    }
}
