package com.mandeep.zoogol.user_profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.help.RequestCallBackFragment;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mandeep on 4/14/2016.
 */
public class UploadPurchaseFragment extends Fragment implements View.OnClickListener {

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
    private Button btn_submit;
    private EditText et_merchant_key, et_shop_name, et_city, et_number, et_bill_number, et_bill_date, et_description,
            et_quantity, et_amount;
    public static EditText et_upload_bill;
    private String partnerName, partnerEmail, partnerZKey;

    private int year;
    private int month;
    private int day;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String transactionID;
    private Intent pictureActionIntent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_fragment, container, false);


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
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_bill_date.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        mPrefs = getActivity().getSharedPreferences("Zoogol", getActivity().MODE_PRIVATE);

        partnerName = mPrefs.getString("fname", "");
        partnerEmail = mPrefs.getString("email", "");
        partnerZKey = mPrefs.getString("zkey", "");
        et_merchant_key = (EditText) view.findViewById(R.id.et_merchant_key);

        et_merchant_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 14) {

                    makeJsonRquest();
                    Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_shop_name = (EditText) view.findViewById(R.id.et_shop_name);
        et_shop_name.setInputType(InputType.TYPE_NULL);
        et_city = (EditText) view.findViewById(R.id.et_city);
        et_city.setInputType(InputType.TYPE_NULL);
        et_number = (EditText) view.findViewById(R.id.et_number);
        et_number.setInputType(InputType.TYPE_NULL);
        et_bill_number = (EditText) view.findViewById(R.id.et_bill_number);
        et_bill_date = (EditText) view.findViewById(R.id.et_bill_date);
        et_bill_date.setInputType(InputType.TYPE_NULL);
        et_bill_date.requestFocus();
        et_description = (EditText) view.findViewById(R.id.et_description);
        et_quantity = (EditText) view.findViewById(R.id.et_quantity);
        et_amount = (EditText) view.findViewById(R.id.et_amount);

        et_upload_bill = (EditText) view.findViewById(R.id.et_upload_bill);
        et_upload_bill.setInputType(InputType.TYPE_NULL);
        et_upload_bill.requestFocus();

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPurchase();

            }
        });

        et_bill_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        et_upload_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDialog();
            }
        });

    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                pictureActionIntent.setType("image/*");
                pictureActionIntent.putExtra("return-data", true);
                getActivity().startActivityForResult(pictureActionIntent, HomeActivity.GALLERY_PICTURE_BILL);


            }

        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(pictureActionIntent, HomeActivity.CAMERA_REQUEST_BILL);

            }
        });
        myAlertDialog.show();
    }


    private void makeJsonRquest() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/zvm-key-info.php?appid=1f0914b48231e96562edd51395dd0bbb&mkey="
                + et_merchant_key.getText().toString();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONObject json_data = response.getJSONObject("data");

                        et_shop_name.setText(json_data.getString("name"));
                        et_city.setText(json_data.getString("city"));
                        et_number.setText(json_data.getString("phone"));

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

    private void uploadPurchase() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/upload-purchase-submit.php?&appid=1f0914b48231e96562edd51395dd0bbb" +
                "&merchant_zkey=" + et_merchant_key.getText().toString() + "&name_of_shop=" + et_shop_name.getText().toString()
                + "&partner_name=" + partnerName + "&partner_email=" + partnerEmail
                + "&customer_zkey=" + partnerZKey + "&mobile=" + et_number.getText().toString()
                + "&city=" + et_city.getText().toString() + "&bill_no=" + et_bill_number.getText().toString()
                + "&billdate=" + et_bill_date.getText().toString()
                + "&description=" + et_description.getText().toString()
                + "&quantity=" + et_quantity.getText().toString() + "&amount=" + et_amount.getText().toString() + "&app_request=true";


        category_url = category_url.replace(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                //  hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {


                        transactionID = response.getString("data");
                        uploadImage();
                    } else {

                        Toast.makeText(getActivity(), response.getString("data"), Toast.LENGTH_LONG).show();
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
                headers.put("Content-Type", "text/html");
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

    private void uploadImage() {
        //Showing the progress dialog
        //final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
        String UPLOAD_URL = "https://www.zoogol.in/newz/api/upload-purchase-image.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Disimissing the progress dialog
                // loading.dismiss();
                hideProgressDialog();

                //Showing toast message of the response
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //  loading.dismiss();
                        hideProgressDialog();
                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(HomeActivity.billImage);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("name", SystemClock.currentThreadTimeMillis() + HomeActivity.ImageURI);
                params.put("tr_id", transactionID);
                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        //AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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