package com.mandeep.zoogol.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mandeep.zoogol.utils.ConnectionDetector;
import com.mandeep.zoogol.utils.Const;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 3/2/2016.
 */
public class ForgotPassword extends Activity implements View.OnClickListener {


    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    // CHECK INTERNET CONNECTION
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private EditText et_email;
    private TextView tv_mail_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgot_password_activity);

        initView();
    }

    private void initView() {

        tv_mail_password = (TextView) findViewById(R.id.tv_mail_password);
        tv_mail_password.setOnClickListener(this);

        et_email = (EditText) findViewById(R.id.et_email);

    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(ForgotPassword.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Making json object request
     */
    private void makeJsonRequest() {

        showProgressDialog();
        String forgot_password_url = Const.FORGOT_PASSWORD_URL + "email=" + et_email.getText().toString();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, forgot_password_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {
                        Toast.makeText(ForgotPassword.this, "An email has been sent to your email addresss to reset your password.", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(ForgotPassword.this, "Invalid Email address", Toast.LENGTH_LONG).show();
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

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.tv_mail_password:
                makeJsonRequest();
                break;


        }
    }
}
