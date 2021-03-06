package com.mandeep.zoogol.help;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.OnlineStoresBean;
import com.mandeep.zoogol.online_store.OnlineCustomAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 3/30/2016.
 */
public class RequestCallBackFragment extends Fragment {

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    private EditText et_name, et_email, et_number;
    private Spinner spinner_timimg;
    private View view;
    private Button btn_submit;
    private String selectedTime = "Morning 09:00-12:00 PM";
    private ImageView iv_navigationDrawer;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.call_back_fragment, container, false);

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

        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, HomeActivity.helpFragmentStack.get(HomeActivity.helpFragmentStack.size() - 2));
                HomeActivity.helpFragmentStack.pop();
                mFragmentTransaction.commit();
            }
        });

        et_name = (EditText) view.findViewById(R.id.et_name);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_number = (EditText) view.findViewById(R.id.et_number);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeJsonRquest();
            }
        });


        spinner_timimg = (Spinner) view.findViewById(R.id.spinner_timimg);
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Morning 09:00-12:00 PM");
        categories.add("Afternoon 12:00-04:00 PM");
        categories.add("Evening 04:00-07:00 PM");
        categories.add("Anytime");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_timimg.setAdapter(dataAdapter);

        spinner_timimg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                selectedTime = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void makeJsonRquest() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/request-call-back.php?&appid=1f0914b48231e96562edd51395dd0bbb&name="
                + et_name.getText().toString() + "&email="
                + et_email.getText().toString() + "&mobile=" + et_number.getText().toString() + "&prefer_time="
                + selectedTime + "& app_request = true";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {

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