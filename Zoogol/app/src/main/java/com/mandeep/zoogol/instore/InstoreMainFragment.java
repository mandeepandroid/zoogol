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
import android.widget.HorizontalScrollView;
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
import com.mandeep.zoogol.adapter.ExpandableAdapter;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.Bean;
import com.mandeep.zoogol.bean.Bean_sub_category;
import com.mandeep.zoogol.bean.InstoreDataBean;
import com.mandeep.zoogol.online_store.OnlineCustomAdapter;
import com.mandeep.zoogol.online_store.OnlineMainFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 3/27/2016.
 */
public class InstoreMainFragment extends Fragment implements View.OnClickListener {


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
    private LinearLayout ll_scroll;

    //Setting up the list
    private GridView gv_instore;
    private HorizontalScrollView horizontal_scroll;
    private CustomAdapter mCustomAdapter;
    private ArrayList<InstoreDataBean> al_cities = new ArrayList<>();
    public static String selectedCity;
    private ImageView iv_grid_icon, iv_list_icon;
    private ImageView iv_navigationDrawer;

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

        HomeActivity.rl_topbar.setVisibility(View.VISIBLE);
        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(this);

        iv_grid_icon = (ImageView) view.findViewById(R.id.iv_grid_icon);
        iv_grid_icon.setOnClickListener(this);

        iv_list_icon = (ImageView) view.findViewById(R.id.iv_list_icon);
        iv_list_icon.setOnClickListener(this);

        horizontal_scroll = (HorizontalScrollView) view.findViewById(R.id.horizontal_scroll);
        horizontal_scroll.setVisibility(View.GONE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 0, 2);
        ll_scroll = (LinearLayout) view.findViewById(R.id.ll_scroll);
        for (int i = 0; i < HomeActivity.al_data.size(); i++) {

            final TextView mTextView = new TextView(getActivity());
            mTextView.setPadding(25, 20, 25, 20);
            mTextView.setText(HomeActivity.al_data.get(i).getStr_name());
            mTextView.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextView.setTag(HomeActivity.al_data.get(i).getStr_name());
            mTextView.setLayoutParams(params);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity(), "" + mTextView.getTag(), Toast.LENGTH_LONG).show();
                }
            });
            ll_scroll.addView(mTextView);

        }

        gv_instore = (GridView) view.findViewById(R.id.gv_instore);

        gv_instore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InstoreDataBean mDataBean = (InstoreDataBean) gv_instore.getAdapter().getItem(position);
                selectedCity = mDataBean.getCity();

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                CityWiseStores mCityWiseStores = new CityWiseStores();
                HomeActivity.instoreFragmentStack.add(mCityWiseStores);

                mFragmentTransaction.replace(R.id.containerRelativeLayout, mCityWiseStores);
                mFragmentTransaction.commit();


            }
        });

        makeJsonRquest();
    }

    private void makeJsonRquest() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/cities.php?appid=1f0914b48231e96562edd51395dd0bbb&lat=30.733315&lng=76.779418";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                al_cities.clear();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_data = response.getJSONArray("data");
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject json_dataobject = json_data.getJSONObject(i);
                            InstoreDataBean obj_bean_data = new InstoreDataBean();
                            obj_bean_data.setCity(json_dataobject.getString("city"));
                            obj_bean_data.setImage(json_dataobject.getString("image"));
                            obj_bean_data.setQuantity(json_dataobject.getInt("quantity"));
                            al_cities.add(obj_bean_data);

                        }
                        mCustomAdapter = new CustomAdapter(getActivity(), al_cities);
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


            case R.id.iv_navigationDrawer:

                HomeActivity.mDrawerLayout.openDrawer(HomeActivity.ll_drawer_list);

                break;


            case R.id.iv_grid_icon:

                gv_instore.setNumColumns(2);
                mCustomAdapter = new CustomAdapter(getActivity(), al_cities);
                gv_instore.setAdapter(mCustomAdapter);

                break;


            case R.id.iv_list_icon:
                gv_instore.setNumColumns(1);
                mCustomAdapter = new CustomAdapter(getActivity(), al_cities);
                gv_instore.setAdapter(mCustomAdapter);

                break;

        }
    }
}
