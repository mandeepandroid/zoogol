package com.mandeep.zoogol.user_profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.mandeep.zoogol.bean.UntracePurchaseBean;
import com.mandeep.zoogol.help.RequestCallBackFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mandeep on 5/21/2016.
 */
public class UntracedPurchaseFragment extends Fragment implements View.OnClickListener {

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
    private Button btn_submit, btn_add;
    private EditText et_select_date, et_order, et_amount, et_remark, et_coupan;
    public static EditText et_bill;
    private ListView lv_data;
    private ArrayList<UntracePurchaseBean> mUntracePurchaseBeanArrayList = new ArrayList<>();
    private UntraceAdapter mUntraceAdapter;
    private LinearLayout ll_form, ll_list_header;
    private Spinner spinner_website_name;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Intent pictureActionIntent;
    private String transactionID;
    private String partnerName, partnerEmail, partnerZKey, partnerMobile, websiteName;
    private ArrayList<String> al_websites = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.untrace_fragment, container, false);


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
        partnerName = mPrefs.getString("fname", "");
        partnerEmail = mPrefs.getString("email", "");
        partnerZKey = mPrefs.getString("zkey", "");
        partnerMobile = mPrefs.getString("mobile", "");
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        makeJsonRquest();
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_select_date.setText(dateFormatter.format(newDate.getTime()));

                updateSpinner();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        lv_data = (ListView) view.findViewById(R.id.lv_data);

        et_select_date = (EditText) view.findViewById(R.id.et_select_date);
        et_select_date.setInputType(InputType.TYPE_NULL);
        et_select_date.requestFocus();
        et_order = (EditText) view.findViewById(R.id.et_order);
        et_amount = (EditText) view.findViewById(R.id.et_amount);
        et_remark = (EditText) view.findViewById(R.id.et_remark);
        et_bill = (EditText) view.findViewById(R.id.et_bill);
        et_bill.setInputType(InputType.TYPE_NULL);
        et_bill.requestFocus();

        et_coupan = (EditText) view.findViewById(R.id.et_coupan);

        ll_form = (LinearLayout) view.findViewById(R.id.ll_form);
        ll_list_header = (LinearLayout) view.findViewById(R.id.ll_list_header);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lv_data.setVisibility(View.GONE);
                ll_list_header.setVisibility(View.GONE);
                ll_form.setVisibility(View.VISIBLE);
            }
        });

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lv_data.setVisibility(View.VISIBLE);
                ll_list_header.setVisibility(View.VISIBLE);
                ll_form.setVisibility(View.GONE);
                uploadPurchase();
            }
        });

        et_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        et_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDialog();
            }
        });


        spinner_website_name = (Spinner) view.findViewById(R.id.spinner_website_name);
    }

    private void updateSpinner() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/untrace-date-record-web.php?appid=1f0914b48231e96562edd51395dd0bbb&key="
                + partnerZKey + "&untracedate=" + et_select_date.getText().toString();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {

                    al_websites.clear();
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_array_data = response.getJSONArray("data");


                        for (int i = 0; i < json_array_data.length(); i++) {

                            JSONObject json_data = json_array_data.getJSONObject(i);


                            al_websites.add(json_data.getString("sitename"));

                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, al_websites);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_website_name.setAdapter(dataAdapter);

                        spinner_website_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String item = parent.getItemAtPosition(position).toString();
                                websiteName = item;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


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
        // AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
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
        String category_url = "https://www.zoogol.in/newz/api/untrace-purchases.php?appid=1f0914b48231e96562edd51395dd0bbb&key="
                + partnerZKey;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                mUntracePurchaseBeanArrayList.clear();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_array_data = response.getJSONArray("data");


                        for (int i = 0; i < json_array_data.length(); i++) {

                            JSONObject json_data = json_array_data.getJSONObject(i);

                            UntracePurchaseBean mPurchaseBean = new UntracePurchaseBean();


                            mPurchaseBean.setId(json_data.getString("id"));
                            mPurchaseBean.setBillno(json_data.getString("billno"));
                            mPurchaseBean.setDate(json_data.getString("date"));
                            mPurchaseBean.setTr_id(json_data.getString("tr_id"));
                            mPurchaseBean.setPhone(json_data.getString("phone"));
                            mPurchaseBean.setEmail(json_data.getString("email"));
                            mPurchaseBean.setRemarks(json_data.getString("remarks"));
                            mPurchaseBean.setZkey(json_data.getString("zkey"));

                            mPurchaseBean.setTime(json_data.getString("time"));
                            mPurchaseBean.setAmount(json_data.getString("amount"));
                            mPurchaseBean.setBill_copy(json_data.getString("bill_copy"));
                            mPurchaseBean.setCoupon_detail(json_data.getString("coupon_detail"));

                            mPurchaseBean.setStatus(json_data.getString("status"));

                            mUntracePurchaseBeanArrayList.add(mPurchaseBean);
                        }

                        mUntraceAdapter = new UntraceAdapter(getActivity(), mUntracePurchaseBeanArrayList);
                        lv_data.setAdapter(mUntraceAdapter);
                        setListViewHeightBasedOnChildren(lv_data);


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
        //AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }


    private void uploadPurchase() {


        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/untrace-purchase-submit.php?&appid=1f0914b48231e96562edd51395dd0bbb"
                + "&zkye=" + partnerZKey + "&web=" + websiteName + "&date=" + et_select_date.getText().toString()
                + "&orderid=" + et_order.getText().toString() + "&phoneno=" + partnerMobile
                + "&email=" + partnerEmail + "&amount=" + et_amount.getText().toString()
                + "&remarks=" + et_remark.getText().toString()
                + "&coupon=" + et_coupan.getText().toString()
                + "&partnername=" + partnerName + "&app_request=true";


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
        String UPLOAD_URL = "https://www.zoogol.in/newz/api/upload-untrace-purchase-image.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Disimissing the progress dialog
                // loading.dismiss();
                hideProgressDialog();

                //Showing toast message of the response
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

                makeJsonRquest();
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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