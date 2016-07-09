package com.mandeep.zoogol.instore;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.adapter.CustomAdapterPhotos;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.Data;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 5/29/2016.
 */
public class ProductImageFragment extends Fragment {

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = ProductImageFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private ArrayList<Data> mDataBeen = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private View view;
    private LinearLayout ll_scroll;
    private ListView listPhoto;
   private CustomAdapterPhotos customAdapterPhotos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_photo, container, false);

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
        listPhoto = (ListView)view.findViewById(R.id.listPhoto);
        makeJsonRquest();
    }

    private void makeJsonRquest() {


        showProgressDialog();

        String photo_url = "https://www.zoogol.in/newz/api/merchant-page-img.php?appid=1f0914b48231e96562edd51395dd0bbb&category="
                +ProductDetailFragment.category+"&subcat="+ProductDetailFragment.sub_category;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, photo_url, null, new Response.Listener<JSONObject>() {

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
                             Data data = new Data();
                            data.setId(json_dataobject.getString("id"));
                            data.setCategory(json_dataobject.getString("category"));
                            data.setSub_category(json_dataobject.getString("sub_category"));

                            data.setImage_name(json_dataobject.getString("image_name"));
                            mDataBeen.add(data);
                        }
                        customAdapterPhotos = new CustomAdapterPhotos(getActivity(),mDataBeen);
                        listPhoto.setAdapter(customAdapterPhotos);

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

}
