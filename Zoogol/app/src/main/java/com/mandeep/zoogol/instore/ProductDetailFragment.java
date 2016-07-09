package com.mandeep.zoogol.instore;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 4/4/2016.
 */
public class ProductDetailFragment extends Fragment implements View.OnClickListener {


    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private LinearLayout ll_scroll;


    //Setting up the list
    private ImageView iv_product;
    private TextView tv_company_name, tv_company_address, tv_highlights;
    public static String category, sub_category, streetAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_detail, container, false);

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
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        iv_product = (ImageView) view.findViewById(R.id.iv_product);
        tv_company_name = (TextView) view.findViewById(R.id.tv_company_name);
        tv_company_address = (TextView) view.findViewById(R.id.tv_company_address);
        tv_highlights = (TextView) view.findViewById(R.id.tv_highlights);

        makeJsonRquest();
    }

    private void makeJsonRquest() {


        showProgressDialog();


        String category_url = "https://www.zoogol.in/newz/api/product.php?pid=" + CityWiseStores.productID + "&appid=1f0914b48231e96562edd51395dd0bbb";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {

                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_data = response.getJSONArray("data");
                        for (int i = 0; i < json_data.length(); i++) {

                            JSONObject json_dataobject = json_data.getJSONObject(i);

                            tv_company_name.setText(json_dataobject.getString("company_name"));
                            tv_company_address.setText(json_dataobject.getString("street_address_2") + ", " + json_dataobject.getString("city"));

                            String highlight = json_dataobject.getString("highlights_description");

                            streetAddress = json_dataobject.getString("street_address_2") + ", " + json_dataobject.getString("city");
                            category = json_dataobject.getString("category");
                            sub_category = json_dataobject.getString("sub_category");
                            sub_category = sub_category.replace("*", "");
                            sub_category = sub_category.replace(" ", "%20");

                            tv_highlights.setText(Html.fromHtml(highlight));
                            Picasso.with(getActivity()).load("https://www.zoogol.in/merchantsimages/image/" + json_dataobject.getString("image")).into(iv_product);

                        }


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

            case R.id.tv_instore_button:


                break;

        }
    }
}
