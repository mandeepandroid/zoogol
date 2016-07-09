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
import com.mandeep.zoogol.help.RequestCallBackFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 5/28/2016.
 */
public class MyPartnersFragment extends Fragment implements View.OnClickListener {

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private ImageView iv_navigationDrawer;

    private TextView tv_my_partner, tv_active_partners, tv_active_circle, tv_partner_circle1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_partners_fragment, container, false);


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
        tv_my_partner = (TextView) view.findViewById(R.id.tv_my_partner);
        tv_active_partners = (TextView) view.findViewById(R.id.tv_active_partners);
        tv_active_circle = (TextView) view.findViewById(R.id.tv_active_circle);
        tv_partner_circle1 = (TextView) view.findViewById(R.id.tv_partner_circle1);

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
        String category_url = "https://www.zoogol.in/newz/api/my-partners.php?appid=1f0914b48231e96562edd51395dd0bbb&zkey=" + mPrefs.getString("zkey", "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONObject json_data = response.getJSONObject("data");

                        tv_my_partner.setText(json_data.getString("total partners"));
                        tv_active_partners.setText(json_data.getString("active friends"));
                        tv_active_circle.setText(json_data.getString("active circle"));
                        tv_partner_circle1.setText(json_data.getString("partners in circle 1"));


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


            case R.id.rl_tips:

                break;


        }
    }

}