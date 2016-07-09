package com.mandeep.zoogol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.mandeep.zoogol.adapter.ExpandableAdapter;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.Bean;
import com.mandeep.zoogol.bean.Bean_sub_category;
import com.mandeep.zoogol.bean.NotificationBean;
import com.mandeep.zoogol.gcm.NotificationActivity;
import com.mandeep.zoogol.help.HelpFragment;
import com.mandeep.zoogol.instore.InstoreMainFragment;
import com.mandeep.zoogol.online_store.OnlineMainFragment;
import com.mandeep.zoogol.share.ShareFragment;
import com.mandeep.zoogol.user_profile.ProfileFragment;
import com.mandeep.zoogol.user_profile.UntracedPurchaseFragment;
import com.mandeep.zoogol.user_profile.UploadPurchaseFragment;
import com.mandeep.zoogol.utils.Const;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by mandeep on 3/2/2016.
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener {


    private String selectedImagePath, userLogedInEmail;
    private Bitmap bitmap;
    public static int GALLERY_PICTURE = 1;
    public static int CAMERA_REQUEST = 2;
    public static int GALLERY_PICTURE_BILL = 3;
    public static int CAMERA_REQUEST_BILL = 4;

    private ExpandableListView navigation_expandablelist;
    List<String> listDataHeader;
    public static LinearLayout ll_drawer_list;
    public static DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    ExpandableAdapter obj_adapter;
    ArrayList<String> al_child;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private TextView tv_nameZMP, tv_status, tv_zkey, tv_location;
    private ImageView iv_home, iv_online, iv_instore, iv_profile, iv_share, iv_help;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    public static ArrayList<Bean> al_data;
    private ArrayList<Bean_sub_category> al_category;
    private ArrayList<Integer> placeholder;


    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    public static Stack<Fragment> homeFragmentStack = new Stack<>();
    public static Stack<Fragment> onlineFragmentStack = new Stack<>();
    public static Stack<Fragment> instoreFragmentStack = new Stack<>();
    public static Stack<Fragment> profileFragmentStack = new Stack<>();
    public static Stack<Fragment> shareFragmentStack = new Stack<>();
    public static Stack<Fragment> helpFragmentStack = new Stack<>();
    private int tabClicked = 1;
    private CircularImageView circular_profilePicDrawer;

    public static ArrayList<NotificationBean> gernalNotificationBean = new ArrayList<>();
    public static ArrayList<String> gernalNotificationCOunt = new ArrayList<>();
    public static ArrayList<NotificationBean> cashBackNotificationBean = new ArrayList<>();
    public static ArrayList<String> cashBackNotificationCount = new ArrayList<>();
    public static ArrayList<NotificationBean> userNotificationBean = new ArrayList<>();
    public static ArrayList<String> userNotificationCount = new ArrayList<>();

    public static String ImageURI;
    public static Bitmap billImage;



    public static RelativeLayout rl_topbar;
    private ImageView iv_gernal_notification, iv_cash_back_notification, iv_friend_notification, iv_navigationDrawer;
    private TextView tv_gernal_notification, tv_cash_back_notification, tv_friend_notification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        homeFragmentStack.clear();
        homeFragmentStack.add(homeFragment);
        mFragmentTransaction.replace(R.id.containerRelativeLayout, homeFragment);
        mFragmentTransaction.commit();

        initView();
    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void initView() {


        mPrefs = getSharedPreferences("Zoogol", MODE_PRIVATE);
        tv_nameZMP = (TextView) findViewById(R.id.tv_nameZMP);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_zkey = (TextView) findViewById(R.id.tv_zkey);
        tv_location = (TextView) findViewById(R.id.tv_location);
        ll_drawer_list = (LinearLayout) findViewById(R.id.ll_drawer_list);

        tv_nameZMP.setText(mPrefs.getString("fname", ""));
        tv_status.setText(mPrefs.getString("status", ""));
        tv_zkey.setText(mPrefs.getString("zkey", ""));
        tv_location.setText(mPrefs.getString("user_city", ""));
        userLogedInEmail = mPrefs.getString("email", "");

        rl_topbar = (RelativeLayout) findViewById(R.id.rl_topbar);
        tv_gernal_notification = (TextView) findViewById(R.id.tv_gernal_notification);
        tv_cash_back_notification = (TextView) findViewById(R.id.tv_cash_back_notification);
        tv_friend_notification = (TextView) findViewById(R.id.tv_friend_notification);

        circular_profilePicDrawer = (CircularImageView) findViewById(R.id.circular_profilePicDrawer);

        Picasso.with(HomeActivity.this).load(Const.IMAGE_BASE_URL + mPrefs.getString("profilepick", "")).placeholder(R.drawable.user_pic)
                .error(R.drawable.user_pic).into(circular_profilePicDrawer);

        listDataHeader = new ArrayList<>();
        al_data = new ArrayList<Bean>();
        placeholder = new ArrayList<Integer>();
        placeholder.add(R.drawable.automobile);
        placeholder.add(R.drawable.education);
        placeholder.add(R.drawable.electronics);
        placeholder.add(R.drawable.entertainment);
        placeholder.add(R.drawable.fashion);
        placeholder.add(R.drawable.food);
        placeholder.add(R.drawable.health);
        placeholder.add(R.drawable.home_living);
        placeholder.add(R.drawable.investment);
        placeholder.add(R.drawable.realestate);
        placeholder.add(R.drawable.travel);
        placeholder.add(R.drawable.mixbag);


        navigation_expandablelist = (ExpandableListView) findViewById(R.id.navigation_expandablelist);
        navigation_expandablelist.setGroupIndicator(null);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();


        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_online = (ImageView) findViewById(R.id.iv_online);
        iv_instore = (ImageView) findViewById(R.id.iv_instore);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_help = (ImageView) findViewById(R.id.iv_help);

        iv_home.setOnClickListener(this);
        iv_online.setOnClickListener(this);
        iv_instore.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_help.setOnClickListener(this);

        iv_gernal_notification = (ImageView) findViewById(R.id.iv_gernal_notification);
        iv_gernal_notification.setOnClickListener(this);


        iv_cash_back_notification = (ImageView) findViewById(R.id.iv_cash_back_notification);
        iv_cash_back_notification.setOnClickListener(this);

        iv_friend_notification = (ImageView) findViewById(R.id.iv_friend_notification);
        iv_friend_notification.setOnClickListener(this);

        iv_navigationDrawer = (ImageView) findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(this);

        addDrawerItems();
        setupDrawer();


        navigation_expandablelist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //String childname = (String) navigation_expandablelist.getAdapter().getItem(childPosition);
                //Toast.makeText(getApplicationContext(),childname, Toast.LENGTH_LONG).show();

                mDrawerLayout.closeDrawers();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                NavigationFragment mNavigationFragment = new NavigationFragment();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mNavigationFragment);
                mFragmentTransaction.commit();
                return false;
            }
        });


    }

    private void addDrawerItems() {


        showProgressDialog();
        String category_url = "http://www.zoogol.in/newz/api/category-api.php?appid=1f0914b48231e96562edd51395dd0bbb";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                getNotificationData();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_data = response.getJSONArray("data");
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject json_dataobject = json_data.getJSONObject(i);
                            Bean obj_bean_data = new Bean();
                            obj_bean_data.setStr_name(json_dataobject.getString("name"));
                            obj_bean_data.setStr_link(json_dataobject.getString("link"));
                            obj_bean_data.setPlaceholder(placeholder.get(i));

                            al_category = new ArrayList<Bean_sub_category>();

                            JSONArray json_array_subcategory = json_dataobject.getJSONArray("sub_category");
                            for (int j = 0; j < json_array_subcategory.length(); j++) {
                                JSONObject json_categoryObject = json_array_subcategory.getJSONObject(j);
                                Bean_sub_category obj_bean_category = new Bean_sub_category();
                                obj_bean_category.setStr_categorylink(json_categoryObject.getString("name"));
                                obj_bean_category.setStr_categoryname(json_categoryObject.getString("link"));
                                al_category.add(obj_bean_category);
                            }

                            obj_bean_data.setAl_subcategory(al_category);
                            al_data.add(obj_bean_data);

                        }

                        obj_adapter = new ExpandableAdapter(al_data, getApplicationContext());
                        navigation_expandablelist.setAdapter(obj_adapter);

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

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);

        navigation_expandablelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getNotificationData() {

        mPrefs = getSharedPreferences("Zoogol", MODE_PRIVATE);
        String userZKEY = mPrefs.getString("zkey", "");

        gernalNotificationCOunt.clear();
        userNotificationCount.clear();
        cashBackNotificationCount.clear();
        gernalNotificationBean.clear();
        userNotificationBean.clear();
        cashBackNotificationBean.clear();
        showProgressDialog();
        String notifiactionURL = "https://www.zoogol.in/newz/api/notification.php?appid=1f0914b48231e96562edd51395dd0bbb&zkey="
                + userZKEY;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, notifiactionURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray mainArray = response.getJSONArray("data");

                        for (int i = 0; i < mainArray.length(); i++) {

                            JSONObject mainObject = mainArray.getJSONObject(i);

                            NotificationBean bean = new NotificationBean();
                            bean.setId(mainObject.getString("id"));
                            String message = mainObject.getString("message");
                            message = message.replace("<span>", "<b>");
                            message = message.replace("</span>", "</b>");
                            bean.setMessage(message);
                            bean.setNewnoti(mainObject.getString("newnoti"));
                            bean.setReadstatus(mainObject.getString("readstatus"));
                            bean.setStatus(mainObject.getString("status"));
                            bean.setZkey(mainObject.getString("zkey"));


                            if (mainObject.getString("status").equals("General") && mainObject.getString("newnoti").equals("1")) {

                                gernalNotificationCOunt.add(mainObject.getString("id"));
                            } else if (mainObject.getString("status").equals("User") && mainObject.getString("newnoti").equals("1")) {

                                userNotificationCount.add(mainObject.getString("id"));
                            } else if (mainObject.getString("status").equals("Cashback") && mainObject.getString("newnoti").equals("1")) {

                                cashBackNotificationCount.add(mainObject.getString("id"));
                                ;
                            }

                            if (mainObject.getString("status").equals("General")) {

                                gernalNotificationBean.add(bean);
                            } else if (mainObject.getString("status").equals("User")) {
                                userNotificationBean.add(bean);
                            } else if (mainObject.getString("status").equals("Cashback")) {
                                cashBackNotificationBean.add(bean);
                            }
                        }

                        if (gernalNotificationCOunt.size() == 0) {
                            tv_gernal_notification.setVisibility(View.GONE);
                        } else {
                            tv_gernal_notification.setVisibility(View.VISIBLE);
                            tv_gernal_notification.setText("" + gernalNotificationCOunt.size());
                        }
                        if (cashBackNotificationCount.size() == 0) {
                            tv_cash_back_notification.setVisibility(View.GONE);
                        } else {
                            tv_cash_back_notification.setVisibility(View.VISIBLE);
                            tv_cash_back_notification.setText("" + cashBackNotificationCount.size());
                        }

                        if (userNotificationCount.size() == 0) {
                            tv_friend_notification.setVisibility(View.GONE);
                        } else {
                            tv_friend_notification.setVisibility(View.VISIBLE);
                            tv_friend_notification.setText("" + userNotificationCount.size());

                        }


                    } else {

                        Toast.makeText(HomeActivity.this, "Unknown exception.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.iv_home:
                changeTabColor(true, false, false, false, false, false);
                tabClicked = 1;
                if (homeFragmentStack.size() == 0) {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragmentStack.add(homeFragment);
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, homeFragment);
                    mFragmentTransaction.commit();
                } else {

                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, homeFragmentStack.peek());
                    mFragmentTransaction.commit();
                }
                break;

            case R.id.iv_online:

                changeTabColor(false, true, false, false, false, false);
                tabClicked = 2;
                if (onlineFragmentStack.size() == 0) {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    OnlineMainFragment onlineMainFragment = new OnlineMainFragment();
                    onlineFragmentStack.add(onlineMainFragment);
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, onlineMainFragment);
                    mFragmentTransaction.commit();
                } else {

                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, onlineFragmentStack.peek());
                    mFragmentTransaction.commit();
                }
                break;
            case R.id.iv_instore:
                changeTabColor(false, false, true, false, false, false);
                tabClicked = 3;
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                InstoreMainFragment instoreMainFragment = new InstoreMainFragment();
                instoreFragmentStack.add(instoreMainFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, instoreMainFragment);
                mFragmentTransaction.commit();

                break;
            case R.id.iv_profile:

                changeTabColor(false, false, false, true, false, false);
                tabClicked = 4;
                if (profileFragmentStack.size() == 0) {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragmentStack.push(profileFragment);
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, profileFragment);
                    mFragmentTransaction.commit();
                } else {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, profileFragmentStack.peek());
                    mFragmentTransaction.commit();
                }
                break;
            case R.id.iv_share:

                changeTabColor(false, false, false, false, true, false);
                tabClicked = 5;
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                ShareFragment shareFragment = new ShareFragment();
                shareFragmentStack.add(shareFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, shareFragment);
                mFragmentTransaction.commit();
                break;
            case R.id.iv_help:
                changeTabColor(false, false, false, false, false, true);
                tabClicked = 6;
                if (helpFragmentStack.size() == 0) {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    HelpFragment helpFragment = new HelpFragment();
                    helpFragmentStack.push(helpFragment);
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, helpFragment);
                    mFragmentTransaction.commit();
                } else {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerRelativeLayout, helpFragmentStack.peek());
                    mFragmentTransaction.commit();
                }
                break;


            case R.id.iv_navigationDrawer:

                HomeActivity.mDrawerLayout.openDrawer(HomeActivity.ll_drawer_list);

                break;


            case R.id.iv_friend_notification:

                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                makeNotificationUnread(userNotificationCount, 3);

                break;

            case R.id.iv_cash_back_notification:

                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                makeNotificationUnread(cashBackNotificationCount, 2);


                break;

            case R.id.iv_gernal_notification:

                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                makeNotificationUnread(gernalNotificationCOunt, 1);

                break;
        }


    }

    private void makeNotificationUnread(ArrayList<String> NotificationCount, final int value) {

        StringBuilder mStringBuilder = new StringBuilder();
        for (int i = 0; i < NotificationCount.size(); i++) {

            mStringBuilder.append(NotificationCount.get(i));
            if (!(i == NotificationCount.size() - 1))
                mStringBuilder.append("-");
        }


        String notifiactionURL = "https://www.zoogol.in/newz/api/notification-read-status.php?appid=1f0914b48231e96562edd51395dd0bbb&zkey=ERCMAX11111&rdstatus=&newnoti="
                + mStringBuilder.toString();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, notifiactionURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    String result = response.getString("result");
                    if (result.equals("true")) {

                        if (value == 1) {
                            gernalNotificationCOunt.clear();
                        } else if (value == 2) {
                            cashBackNotificationCount.clear();
                        } else if (value == 3) {
                            userNotificationCount.clear();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());

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

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onBackPressed() {

        if (tabClicked == 1) {


            if (homeFragmentStack.size() > 1) {

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, homeFragmentStack.get(homeFragmentStack.size() - 2));
                homeFragmentStack.pop();
                mFragmentTransaction.commit();

            } else if (homeFragmentStack.size() == 1) {

                super.onBackPressed();
            }

        } else if (tabClicked == 2) {

            if (onlineFragmentStack.size() > 1) {

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, onlineFragmentStack.get(onlineFragmentStack.size() - 2));
                onlineFragmentStack.pop();
                mFragmentTransaction.commit();

            } else if (onlineFragmentStack.size() == 1) {

                super.onBackPressed();
            }

        } else if (tabClicked == 3) {


            if (instoreFragmentStack.size() > 1) {

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, instoreFragmentStack.get(instoreFragmentStack.size() - 2));
                instoreFragmentStack.pop();
                mFragmentTransaction.commit();
            } else if (instoreFragmentStack.size() == 1) {
                super.onBackPressed();
            }

        } else if (tabClicked == 4) {

            if (profileFragmentStack.size() > 1) {

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, profileFragmentStack.get(profileFragmentStack.size() - 2));
                profileFragmentStack.pop();
                mFragmentTransaction.commit();

            } else if (profileFragmentStack.size() == 1) {

                super.onBackPressed();
            }


        } else if (tabClicked == 5) {

            if (shareFragmentStack.size() > 1) {

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, shareFragmentStack.get(shareFragmentStack.size() - 2));
                shareFragmentStack.pop();
                mFragmentTransaction.commit();
            } else if (shareFragmentStack.size() == 1) {
                super.onBackPressed();
            }

        } else if (tabClicked == 6) {


            if (helpFragmentStack.size() > 1) {

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerRelativeLayout, helpFragmentStack.get(helpFragmentStack.size() - 2));
                helpFragmentStack.pop();
                mFragmentTransaction.commit();
            } else if (helpFragmentStack.size() == 1) {
                super.onBackPressed();
            }

        } else {

            super.onBackPressed();
        }

    }

    public void changeTabColor(boolean home, boolean online, boolean instore, boolean account, boolean share, boolean help) {

        if (home) {
            iv_home.setImageResource(R.drawable.new_tab_home_selected);
        } else {
            iv_home.setImageResource(R.drawable.new_tab_home);
        }
        if (online) {
            iv_online.setImageResource(R.drawable.new_tab_online_selected);
        } else {
            iv_online.setImageResource(R.drawable.new_tab_online);
        }

        if (instore) {
            iv_instore.setImageResource(R.drawable.new_tab_instore_selected);
        } else {
            iv_instore.setImageResource(R.drawable.new_tab_instore);
        }
        if (account) {
            iv_profile.setImageResource(R.drawable.new_tab_account_selected);
        } else {
            iv_profile.setImageResource(R.drawable.new_tab_account);
        }

        if (share) {
            iv_share.setImageResource(R.drawable.new_tab_share_selected);
        } else {
            iv_share.setImageResource(R.drawable.new_tab_share);
        }

        if (help) {
            iv_help.setImageResource(R.drawable.new_tab_help_selected);
        } else {
            iv_help.setImageResource(R.drawable.new_tab_help);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // our BitmapDrawable for the thumbnail
                    BitmapDrawable bmpDrawable = null;
                    // try to retrieve the image using the data from the intent
                    Cursor cursor;
                    String[] projection = {MediaStore.Images.Media.DATA};
                    if (Build.VERSION.SDK_INT > 19) {

                        try {
                            String wholeID = DocumentsContract.getDocumentId(data.getData());
                            String id = wholeID.split(":")[1];
                            String sel = MediaStore.Images.Media._ID + "=?";
                            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, sel, new String[]{id}, null);
                        } catch (Exception e) {

                            cursor = getContentResolver().query(data.getData(), null, null, null, null);
                            e.printStackTrace();
                        }


                    } else {
                        cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    }
                    if (cursor != null) {

                        cursor.moveToFirst();

                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        //selectedImagePath = cursor.getString(idx);
                        bitmap = BitmapFactory.decodeFile(selectedImagePath);

                        bitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, false);
                        ProfileFragment.circular_profilePic.setImageBitmap(bitmap);
                        circular_profilePicDrawer.setImageBitmap(bitmap);
                        uploadImage();



                    } else {

                        bmpDrawable = new BitmapDrawable(getResources(), data.getData().getPath());
                        ProfileFragment.circular_profilePic.setImageDrawable(bmpDrawable);
                        circular_profilePicDrawer.setImageDrawable(bmpDrawable);
                        Toast.makeText(getApplicationContext(), "Cancelled2", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {

                if (data.hasExtra("data")) {

                    // retrieve the bitmap from the intent
                    bitmap = (Bitmap) data.getExtras().get("data");

                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
                            MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                            selectedImagePath = uri.toString();
                            //ProfileFragment.circular_profilePic.setImageBitmap(bitmap);

                            // Toast.makeText(getApplicationContext(), "Cancelled3", Toast.LENGTH_SHORT).show();
                        } while (cursor.moveToNext());
                        cursor.close();
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, false);
                    ProfileFragment.circular_profilePic.setImageBitmap(bitmap);
                    circular_profilePicDrawer.setImageBitmap(bitmap);
                    uploadImage();

                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(), "No extras to retrieve!", Toast.LENGTH_SHORT).show();

                    BitmapDrawable thumbnail = new BitmapDrawable(getResources(), data.getData().getPath());
                    Toast.makeText(getApplicationContext(), "Cancelled4", Toast.LENGTH_SHORT).show();
                    // update the image view with the newly created drawable
                    ProfileFragment.circular_profilePic.setImageDrawable(thumbnail);
                    circular_profilePicDrawer.setImageDrawable(thumbnail);

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST_BILL) {
            if (resultCode == RESULT_OK) {

                if (data.hasExtra("data")) {

                    // retrieve the bitmap from the intent
                    billImage = (Bitmap) data.getExtras().get("data");

                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
                            MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                            ImageURI = uri.toString();
                        } while (cursor.moveToNext());
                        cursor.close();
                    }
                    //UploadPurchaseFragment.et_upload_bill.setText(UploadPurchaseFragment.ImageURI);
                    billImage = Bitmap.createScaledBitmap(billImage, 600, 600, false);
                    ImageURI = ImageURI.substring(ImageURI.lastIndexOf("/"));
                    ImageURI = ImageURI.replace("/", "");
                    try {
                        UploadPurchaseFragment.et_upload_bill.setText(ImageURI);
                    } catch (Exception e) {
                    }

                    try {
                        UntracedPurchaseFragment.et_bill.setText(ImageURI);
                    } catch (Exception e) {
                    }

                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(), "No extras to retrieve!", Toast.LENGTH_SHORT).show();

                    BitmapDrawable thumbnail = new BitmapDrawable(getResources(), data.getData().getPath());

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PICTURE_BILL) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // our BitmapDrawable for the thumbnail
                    BitmapDrawable bmpDrawable = null;
                    // try to retrieve the image using the data from the intent
                    Cursor cursor;
                    String[] projection = {MediaStore.Images.Media.DATA};
                    if (Build.VERSION.SDK_INT > 19) {

                        try {
                            String wholeID = DocumentsContract.getDocumentId(data.getData());
                            String id = wholeID.split(":")[1];
                            String sel = MediaStore.Images.Media._ID + "=?";
                            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, sel, new String[]{id}, null);
                        } catch (Exception e) {

                            cursor = getContentResolver().query(data.getData(), null, null, null, null);
                            e.printStackTrace();
                        }


                    } else {
                        cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    }
                    if (cursor != null) {

                        cursor.moveToFirst();

                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        ImageURI = cursor.getString(idx);
                        billImage = BitmapFactory.decodeFile(ImageURI);

                        billImage = Bitmap.createScaledBitmap(billImage, 600, 600, false);

                        ImageURI = ImageURI.substring(ImageURI.lastIndexOf("/"));
                        ImageURI = ImageURI.replace("/", "");
                        try {
                            UploadPurchaseFragment.et_upload_bill.setText(ImageURI);
                        } catch (Exception e) {
                        }

                        try {
                            UntracedPurchaseFragment.et_bill.setText(ImageURI);
                        } catch (Exception e) {
                        }

                    } else {

                        bmpDrawable = new BitmapDrawable(getResources(), data.getData().getPath());


                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        String UPLOAD_URL = "https://www.zoogol.in/newz/api/upload-profile-pick.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Disimissing the progress dialog
                loading.dismiss();

                mPrefs = getSharedPreferences("Zoogol", MODE_PRIVATE);
                mEditor = mPrefs.edit();

                mEditor.putString("profilepick", SystemClock.currentThreadTimeMillis() + selectedImagePath);


                mEditor.commit();

                //Showing toast message of the response
                Toast.makeText(HomeActivity.this, s, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(HomeActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                selectedImagePath = selectedImagePath.substring(selectedImagePath.lastIndexOf("/"));
                selectedImagePath = selectedImagePath.replace("/", "");
                //Adding parameters
                params.put("image", image);
                params.put("name", SystemClock.currentThreadTimeMillis() + selectedImagePath);
                params.put("email", userLogedInEmail);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public class PhotoUpload extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            uploadPic();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();


        }
    }

    public void uploadPic() {

        try {
            String responseString;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost("https://www.zoogol.in/newz/api/upload-profile-pick.php");

            final JSONObject logInRequest = new JSONObject();
            String image = getStringImage(bitmap);
            selectedImagePath = selectedImagePath.substring(selectedImagePath.lastIndexOf("/"));
            selectedImagePath = selectedImagePath.replace("/", "");
            //Adding parameters
            logInRequest.put("image", image);
            logInRequest.put("name", SystemClock.uptimeMillis() + selectedImagePath);
            logInRequest.put("email", userLogedInEmail);
            String st = logInRequest.toString();
            StringEntity st_entity = new StringEntity(st);
            request.setEntity(st_entity);
            HttpResponse response = httpClient.execute(request);

            HttpEntity httpEntity = response.getEntity();
            responseString = EntityUtils.toString(httpEntity);

            System.out.println("Response String==============" + responseString);
            try {

                JSONArray jArray = new JSONArray(responseString);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
