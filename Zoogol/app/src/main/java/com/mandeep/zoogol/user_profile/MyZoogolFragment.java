package com.mandeep.zoogol.user_profile;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.help.*;
import com.mandeep.zoogol.share.ShareFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 4/14/2016.
 */
public class MyZoogolFragment extends Fragment implements View.OnClickListener {

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;

    private TextView tv_eligible_cashback, tv_virtual_cashback, tv_approved_cashback, tv_paid_cashback;
    private RelativeLayout rl_free_product, rl_invite_friend, rl_help;
    private ImageView iv_navigationDrawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_zoogol_fragment, container, false);


        initView();
        return view;
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

    private void initView() {

        mPrefs = getActivity().getSharedPreferences("Zoogol", getActivity().MODE_PRIVATE);
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        tv_eligible_cashback = (TextView) view.findViewById(R.id.tv_eligible_cashback);
        tv_virtual_cashback = (TextView) view.findViewById(R.id.tv_virtual_cashback);
        tv_approved_cashback = (TextView) view.findViewById(R.id.tv_approved_cashback);
        tv_paid_cashback = (TextView) view.findViewById(R.id.tv_paid_cashback);

        rl_free_product = (RelativeLayout) view.findViewById(R.id.rl_free_product);
        rl_free_product.setOnClickListener(this);

        rl_invite_friend = (RelativeLayout) view.findViewById(R.id.rl_invite_friend);
        rl_invite_friend.setOnClickListener(this);

        rl_help = (RelativeLayout) view.findViewById(R.id.rl_help);
        rl_help.setOnClickListener(this);

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

        makeJsonRquest();
    }

    private void makeJsonRquest() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/cashback.php?appid=1f0914b48231e96562edd51395dd0bbb&zkey=" + mPrefs.getString("zkey", "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONObject json_data = response.getJSONObject("data");


                        tv_eligible_cashback.setText("Rs. " + json_data.getString("eligible_cashbook"));
                        tv_virtual_cashback.setText("Rs. " + json_data.getString("virtual_cashbook"));
                        tv_approved_cashback.setText("Rs. " + json_data.getString("approved_cashbook"));
                        tv_paid_cashback.setText("Rs. " + json_data.getString("total_paid_cashbook"));


                    } else {

                        // Toast.makeText(HomeActivity.this, "Invalid Email or password.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("store_id", "13");
                /*params.put("latitude", "30.7129809");
                params.put("longitude", "76.7091668");*/


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_free_product:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                FreeProductFragment mFreeProductFragment = new FreeProductFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mFreeProductFragment);
                mFragmentTransaction.commit();

                break;


            case R.id.rl_invite_friend:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                ShareFragment mShareFragment = new ShareFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mShareFragment);
                mFragmentTransaction.commit();

                break;

            case R.id.rl_help:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                HelpFragment mHelpFragment = new HelpFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mHelpFragment);
                mFragmentTransaction.commit();

                break;


        }
    }

}