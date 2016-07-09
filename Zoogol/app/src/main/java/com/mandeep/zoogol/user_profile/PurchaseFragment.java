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
import android.widget.GridView;
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
import com.mandeep.zoogol.bean.InstoreCityDataBean;
import com.mandeep.zoogol.help.RequestCallBackFragment;
import com.mandeep.zoogol.help.SendQueryFragment;
import com.mandeep.zoogol.instore.CityProductAdapter;
import com.mandeep.zoogol.instore.InstoreMainFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 3/31/2016.
 */


public class PurchaseFragment extends Fragment implements View.OnClickListener {

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private RelativeLayout rl_call_now, rl_callback, rl_faq, rl_sendquery, rl_tips;
    private TextView tv_purchase_amount, tv_no_of_purchase, tv_intore_purchase, tv_onile_purchase, tv_cancel_charges, tv_online_cancel, tv_instore_cancel;
    private ImageView iv_navigationDrawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.purchase_activity, container, false);


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
        tv_purchase_amount = (TextView) view.findViewById(R.id.tv_purchase_amount);
        tv_no_of_purchase = (TextView) view.findViewById(R.id.tv_no_of_purchase);
        tv_intore_purchase = (TextView) view.findViewById(R.id.tv_intore_purchase);
        tv_onile_purchase = (TextView) view.findViewById(R.id.tv_onile_purchase);
        tv_cancel_charges = (TextView) view.findViewById(R.id.tv_cancel_charges);
        tv_online_cancel = (TextView) view.findViewById(R.id.tv_online_cancel);
        tv_instore_cancel = (TextView) view.findViewById(R.id.tv_instore_cancel);

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
        String category_url = "https://www.zoogol.in/newz/api/purchases.php?appid=1f0914b48231e96562edd51395dd0bbb&zkey=" + mPrefs.getString("zkey", "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONObject json_data = response.getJSONObject("data");


                        tv_purchase_amount.setText("Rs. " + json_data.getString("amount_purchase"));
                        tv_no_of_purchase.setText(json_data.getString("number_purchase"));
                        tv_intore_purchase.setText(json_data.getString("instore_purchase"));
                        tv_onile_purchase.setText(json_data.getString("online_purchase"));
                        tv_cancel_charges.setText("Rs. " + json_data.getString("amount_cancel"));
                        tv_online_cancel.setText(json_data.getString("online_calcel"));
                        tv_instore_cancel.setText(json_data.getString("instore_cancel"));


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

            case R.id.rl_call_now:

                break;

            case R.id.rl_callback:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                RequestCallBackFragment callBackFragment = new RequestCallBackFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, callBackFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_faq:


                break;

            case R.id.rl_sendquery:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                SendQueryFragment mSendQueryFragment = new SendQueryFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mSendQueryFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.rl_tips:

                break;


        }
    }

}