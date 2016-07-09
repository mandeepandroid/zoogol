package com.mandeep.zoogol;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.OnlineStoresBean;
import com.mandeep.zoogol.online_store.OnlineCustomAdapter;
import com.mandeep.zoogol.online_store.StoreDetailFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 5/5/2016.
 */
public class OnlineCombineFragment extends Fragment implements View.OnClickListener {

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

       /* iv_grid_icon = (ImageView) view.findViewById(R.id.iv_grid_icon);
        iv_grid_icon.setOnClickListener(this);

        iv_list_icon = (ImageView) view.findViewById(R.id.iv_list_icon);
        iv_list_icon.setOnClickListener(this);*/

        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

        }*/

        gv_online_main = (GridView) view.findViewById(R.id.gv_online_main);

        /*gv_online_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OnlineStoresBean mDataBean = (OnlineStoresBean) gv_online_main.getAdapter().getItem(position);
                storeSelected = mDataBean.getLogo();
                storeVisitingLink = mDataBean.getLink();

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                StoreDetailFragment productDetailFragment = new StoreDetailFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, productDetailFragment);
                mFragmentTransaction.commit();

            }
        });*/

        makeJsonRquest();
    }

    private void makeJsonRquest() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/online-store.php?appid=1f0914b48231e96562edd51395dd0bbb";
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
                            OnlineStoresBean obj_bean_data = new OnlineStoresBean();
                            obj_bean_data.setId(json_dataobject.getString("id"));
                            obj_bean_data.setSite_name(json_dataobject.getString("site_name"));
                            obj_bean_data.setAbout(json_dataobject.getString("about"));
                            obj_bean_data.setLogo(json_dataobject.getString("logo"));
                            obj_bean_data.setLink(json_dataobject.getString("link"));
                            obj_bean_data.setStatus(json_dataobject.getString("status"));
                            obj_bean_data.setWebdate(json_dataobject.getString("webdate"));
                            obj_bean_data.setCategory(json_dataobject.getString("category"));
                            obj_bean_data.setSort(json_dataobject.getString("sort"));
                            mOnlineStoresBeen.add(obj_bean_data);

                        }

                        mCustomAdapter = new OnlineCustomAdapter(getActivity(), mOnlineStoresBeen);
                        gv_online_main.setAdapter(mCustomAdapter);

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

                gv_online_main.setNumColumns(2);
                mCustomAdapter = new OnlineCustomAdapter(getActivity(), mOnlineStoresBeen);
                gv_online_main.setAdapter(mCustomAdapter);

                break;


            case R.id.iv_list_icon:
                gv_online_main.setNumColumns(1);
                mCustomAdapter = new OnlineCustomAdapter(getActivity(), mOnlineStoresBeen);
                gv_online_main.setAdapter(mCustomAdapter);

                break;

        }
    }
}
