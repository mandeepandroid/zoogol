package com.mandeep.zoogol;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import com.android.volley.toolbox.Volley;
import com.mandeep.zoogol.adapter.ExpandableAdapter;
import com.mandeep.zoogol.authentication.FirstFragment;
import com.mandeep.zoogol.authentication.ForgotPassword;
import com.mandeep.zoogol.bean.Bean;
import com.mandeep.zoogol.bean.NotificationBean;
import com.mandeep.zoogol.explore.ExploreMainFragment;
import com.mandeep.zoogol.gcm.NotificationActivity;
import com.mandeep.zoogol.share.ShareFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mandeep on 3/23/2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {


    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private ExpandableListView navigation_expandablelist;
    List<String> listDataHeader;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    ExpandableAdapter obj_adapter;
    ArrayList<String> al_child;
    private ActionBarDrawerToggle mDrawerToggle;
    ArrayList<Bean> al_data;
    private String mActivityTitle;

    private ImageView iv_explore, iv_shop_now, iv_share, iv_navigationDrawer, iv_deals;
    private ImageView iv_gernal_notification, iv_cash_back_notification, iv_friend_notification;
    private static TextView tv_gernal_notification, tv_cash_back_notification, tv_friend_notification;
    ViewPager pager;
    MyPageAdapter pageAdapter;
    List<Fragment> fragments = getFragments();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    RelativeLayout rl_first, rl_second;

    View view1, view2, view3;
    private CountDownTimer countDownTimer;
    private final long startTimer = 3 * 1000;
    private final long interval = 1 * 1000;
    // private ImageView iv_home, iv_online, iv_instore, iv_profile, iv_share, iv_help;
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        initView();
        return view;
    }


    private void initView() {

        HomeActivity.rl_topbar.setVisibility(View.VISIBLE);



        listDataHeader = new ArrayList<>();
        al_data = new ArrayList<Bean>();


        iv_gernal_notification = (ImageView) view.findViewById(R.id.iv_gernal_notification);
        iv_gernal_notification.setOnClickListener(this);


        iv_cash_back_notification = (ImageView) view.findViewById(R.id.iv_cash_back_notification);
        iv_cash_back_notification.setOnClickListener(this);

        iv_friend_notification = (ImageView) view.findViewById(R.id.iv_friend_notification);
        iv_friend_notification.setOnClickListener(this);

        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(this);

        iv_deals = (ImageView) view.findViewById(R.id.iv_deals);
        iv_deals.setOnClickListener(this);


        iv_explore = (ImageView) view.findViewById(R.id.iv_explore);
        iv_explore.setOnClickListener(this);

        iv_shop_now = (ImageView) view.findViewById(R.id.iv_shop_now);
        iv_shop_now.setOnClickListener(this);

        iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);

        pager = (ViewPager) view.findViewById(R.id.viewpager);
        view1 = (View) view.findViewById(R.id.view1);
        view1.setBackgroundResource(R.drawable.listshade);
        view2 = (View) view.findViewById(R.id.view2);
        view3 = (View) view.findViewById(R.id.view3);


        pageAdapter = new MyPageAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(pageAdapter);


        NUM_PAGES = 3;


        Log.e("NUMBER OF PAGES", "" + NUM_PAGES);

        countDownTimer = new CountDownTimer(startTimer, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub


            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                // pager.setAdapter(pageAdapter);

                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                    pager.setCurrentItem(currentPage, true);
                    view1.setBackgroundResource(R.drawable.listshade);
                    view2.setBackgroundResource(R.drawable.silder_circleview);
                    view3.setBackgroundResource(R.drawable.silder_circleview);
                } else {

                    if (currentPage == 1) {
                        view1.setBackgroundResource(R.drawable.silder_circleview);
                        view2.setBackgroundResource(R.drawable.listshade);
                        view3.setBackgroundResource(R.drawable.silder_circleview);
                    } else if (currentPage == 2) {
                        view1.setBackgroundResource(R.drawable.silder_circleview);
                        view2.setBackgroundResource(R.drawable.silder_circleview);
                        view3.setBackgroundResource(R.drawable.listshade);
                    }
                    pager.setCurrentItem(currentPage++, true);


                }


                // pager.setPageTransformer(true, new ZoomOutPageTransformer());


                countDownTimer.start();
            }
        };

        countDownTimer.start();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                currentPage = position;
                if (position == 0) {
                    pager.setCurrentItem(currentPage, true);
                    view1.setBackgroundResource(R.drawable.listshade);
                    view2.setBackgroundResource(R.drawable.silder_circleview);
                    view3.setBackgroundResource(R.drawable.silder_circleview);
                } else if (position == 1) {
                    view1.setBackgroundResource(R.drawable.silder_circleview);
                    view2.setBackgroundResource(R.drawable.listshade);
                    view3.setBackgroundResource(R.drawable.silder_circleview);
                } else if (position == 2) {
                    view1.setBackgroundResource(R.drawable.silder_circleview);
                    view2.setBackgroundResource(R.drawable.silder_circleview);
                    view3.setBackgroundResource(R.drawable.listshade);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_explore:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                ExploreMainFragment exploreMainFragment = new ExploreMainFragment();
                HomeActivity.homeFragmentStack.add(exploreMainFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, exploreMainFragment);
                mFragmentTransaction.commit();

                break;
            case R.id.iv_shop_now:
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                InstoreOnlineFragment mInstoreOnlineFragment = new InstoreOnlineFragment();
                HomeActivity.homeFragmentStack.add(mInstoreOnlineFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mInstoreOnlineFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.iv_share:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                ShareFragment mShareFragment = new ShareFragment();
                HomeActivity.homeFragmentStack.add(mShareFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, mShareFragment);
                mFragmentTransaction.commit();
                break;

            case R.id.iv_navigationDrawer:

                HomeActivity.mDrawerLayout.openDrawer(HomeActivity.ll_drawer_list);

                break;

            case R.id.iv_deals:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                DealsFragment dealsFragment = new DealsFragment();
                HomeActivity.homeFragmentStack.add(dealsFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, dealsFragment);
                mFragmentTransaction.commit();

                break;

            case R.id.iv_friend_notification:

                startActivity(new Intent(getActivity(), NotificationActivity.class));

                break;

            case R.id.iv_cash_back_notification:

                startActivity(new Intent(getActivity(), NotificationActivity.class));


                break;

            case R.id.iv_gernal_notification:

                startActivity(new Intent(getActivity(), NotificationActivity.class));
                makeNotificationUnread(HomeActivity.gernalNotificationCOunt);

                break;


        }
    }

    private void makeNotificationUnread(ArrayList<String> NotificationCOunt) {

        StringBuilder mStringBuilder = new StringBuilder();
        for (int i = 0; i < NotificationCOunt.size(); i++) {

            mStringBuilder.append(NotificationCOunt.get(i));
            if (!(i == NotificationCOunt.size() - 1))
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

                        HomeActivity.gernalNotificationCOunt.clear();


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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }

    public class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }


    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(new HomeFragmentDrawer1());
        fList.add(new HomeFragmentDrawer2());
        fList.add(new HomeFragmentDrawer3());
        //fList.add(new FirstFragment());
        return fList;
    }

}