package com.mandeep.zoogol.instore;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 4/4/2016.
 */
public class CategoryWiseStoreFragment1 extends Fragment implements View.OnClickListener {


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
    public static String subCategoryChosssed;


    //Setting up the list
    private GridView gv_instore;
    private CityProductAdapter mCustomAdapter;
    private ArrayList<InstoreCityDataBean> al_cities = new ArrayList<>();
    private ImageView iv_grid_icon, iv_list_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.instore_main_activity, container, false);

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

        iv_grid_icon = (ImageView) view.findViewById(R.id.iv_grid_icon);
        iv_grid_icon.setOnClickListener(this);

        iv_list_icon = (ImageView) view.findViewById(R.id.iv_list_icon);
        iv_list_icon.setOnClickListener(this);

        int position = 0;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 0, 2);
        ll_scroll = (LinearLayout) view.findViewById(R.id.ll_scroll);
        for (int j = 0; j < HomeActivity.al_data.size(); j++) {

            if (HomeActivity.al_data.get(j).getStr_name().equals(CityWiseStores.categoryChosssed)) {

                position = j;
            }
        }
        for (int i = 0; i < HomeActivity.al_data.get(position).getAl_subcategory().size(); i++) {

            final TextView mTextView = new TextView(getActivity());
            mTextView.setPadding(25, 20, 25, 20);
            mTextView.setText(HomeActivity.al_data.get(position).getAl_subcategory().get(i).getStr_categoryname());
            mTextView.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextView.setTag(HomeActivity.al_data.get(position).getAl_subcategory().get(i).getStr_categoryname());
            mTextView.setLayoutParams(params);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    subCategoryChosssed = mTextView.getTag().toString();
                    mFragmentManager = getFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    CategoryWiseStoreFragment2 mStoreFragment = new CategoryWiseStoreFragment2();
                    HomeActivity.instoreFragmentStack.add(mStoreFragment);
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, mStoreFragment);
                    mFragmentTransaction.commit();
                    Toast.makeText(getActivity(), "" + mTextView.getTag(), Toast.LENGTH_LONG).show();
                }
            });
            ll_scroll.addView(mTextView);
        }


        gv_instore = (GridView) view.findViewById(R.id.gv_instore);

        gv_instore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InstoreCityDataBean bean = (InstoreCityDataBean) gv_instore.getAdapter().getItem(position);
                CityWiseStores.productID = bean.getId();

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                DetaileldProductDescriptionFragment productDetailFragment = new DetaileldProductDescriptionFragment();
                HomeActivity.instoreFragmentStack.add(productDetailFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, productDetailFragment);
                mFragmentTransaction.commit();

            }
        });

        makeJsonRquest();
    }

    private void makeJsonRquest() {

        gv_instore = (GridView) view.findViewById(R.id.gv_instore);

        showProgressDialog();
        try {
            subCategoryChosssed = subCategoryChosssed.replace("-", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String category_url = "https://www.zoogol.in/newz/api/stores.php?appid=1f0914b48231e96562edd51395dd0bbb&category=" + CityWiseStores.categoryChosssed + "&city=" + InstoreMainFragment.selectedCity;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    al_cities.clear();
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_data = response.getJSONArray("data");
                        for (int i = 0; i < json_data.length(); i++) {

                            JSONObject json_dataobject = json_data.getJSONObject(i);
                            InstoreCityDataBean obj_bean_data = new InstoreCityDataBean();
                            obj_bean_data.setCity(json_dataobject.getString("city"));
                            obj_bean_data.setImage(json_dataobject.getString("image"));
                            obj_bean_data.setCompany_name(json_dataobject.getString("company_name"));
                            obj_bean_data.setCategory(json_dataobject.getString("category"));
                            obj_bean_data.setAddress(json_dataobject.getString("address"));
                            obj_bean_data.setId(json_dataobject.getString("id"));

                            al_cities.add(obj_bean_data);

                        }
                        mCustomAdapter = new CityProductAdapter(getActivity(), al_cities);
                        gv_instore.setAdapter(mCustomAdapter);

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

            case R.id.iv_grid_icon:

                gv_instore.setNumColumns(2);
                mCustomAdapter = new CityProductAdapter(getActivity(), al_cities);
                gv_instore.setAdapter(mCustomAdapter);

                break;


            case R.id.iv_list_icon:
                gv_instore.setNumColumns(1);
                mCustomAdapter = new CityProductAdapter(getActivity(), al_cities);
                gv_instore.setAdapter(mCustomAdapter);

                break;


        }
    }
}
