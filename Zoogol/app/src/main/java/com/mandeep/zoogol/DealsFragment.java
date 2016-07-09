package com.mandeep.zoogol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.DealsBean;
import com.mandeep.zoogol.bean.UntracePurchaseBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 5/24/2016.
 */
public class DealsFragment extends Fragment {

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
    private ArrayList<DealsBean> mDealsBeen = new ArrayList<>();
    private ListView lv_deals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.deal_fragment, container, false);


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
        lv_deals = (ListView) view.findViewById(R.id.lv_deals);
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, HomeActivity.homeFragmentStack.get(HomeActivity.homeFragmentStack.size() - 2));
                HomeActivity.homeFragmentStack.pop();
                mFragmentTransaction.commit();
            }
        });

        makeJsonRquest();
    }

    private void makeJsonRquest() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/deals.php?appid=1f0914b48231e96562edd51395dd0bbb";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                mDealsBeen.clear();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_data_array = response.getJSONArray("data");
                        for (int i = 0; i < json_data_array.length(); i++) {

                            JSONObject json_data = json_data_array.getJSONObject(i);
                            DealsBean bean = new DealsBean();

                            bean.setId(json_data.getString("id"));
                            bean.setWebsiteid(json_data.getString("websiteid"));
                            bean.setOffer_name(json_data.getString("offer_name"));
                            bean.setAbout(json_data.getString("about"));
                            bean.setEnddate(json_data.getString("enddate"));
                            bean.setCoupon(json_data.getString("coupon"));
                            bean.setOffers_images(json_data.getString("offers_images"));
                            bean.setBanner(json_data.getString("banner"));
                            bean.setGrab_link(json_data.getString("grab_link"));
                            mDealsBeen.add(bean);
                        }

                        DealsAdapter dealsAdapter = new DealsAdapter(getActivity(), mDealsBeen);
                        lv_deals.setAdapter(dealsAdapter);
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

    class DealsAdapter extends ArrayAdapter<DealsBean> {
        private final Context context;
        private ArrayList<DealsBean> items;
        private boolean deleteIconVisible;

        public DealsAdapter(Context context, ArrayList<DealsBean> items) {
            super(context, 0, items);

            this.context = context;
            this.items = items;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.deals_items, parent, false);

            TextView tv_deal_name = (TextView) view.findViewById(R.id.tv_deal_name);
            TextView tv_deal_detail = (TextView) view.findViewById(R.id.tv_deal_detail);
            TextView tv_deal_endtime = (TextView) view.findViewById(R.id.tv_deal_endtime);


            tv_deal_name.setText(items.get(position).getOffer_name());
            tv_deal_detail.setText(items.get(position).getAbout());
            tv_deal_endtime.setText("Valid upto: " + items.get(position).getEnddate());


            return view;
        }
    }
}
