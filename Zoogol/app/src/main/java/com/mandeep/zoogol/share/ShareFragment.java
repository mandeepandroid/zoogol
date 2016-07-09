package com.mandeep.zoogol.share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.HomeFragmentDrawer1;
import com.mandeep.zoogol.HomeFragmentDrawer2;
import com.mandeep.zoogol.HomeFragmentDrawer3;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.authentication.FirstFragment;
import com.mandeep.zoogol.bean.Bean;
import com.mandeep.zoogol.help.CashBackFragment;
import com.mandeep.zoogol.help.FaqFragment;
import com.mandeep.zoogol.help.RequestCallBackFragment;
import com.mandeep.zoogol.help.SendQueryFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mandeep on 3/23/2016.
 */
public class ShareFragment extends Fragment implements View.OnClickListener {

    ViewPager pager;
    MyPageAdapter pageAdapter;
    List<Fragment> fragments = getFragments();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    RelativeLayout rl_first, rl_second;
    View view;
    View view1, view2, view3;
    private CountDownTimer countDownTimer;
    private final long startTimer = 3 * 1000;
    private final long interval = 1 * 1000;
    private ImageView iv_navigationDrawer;
    private LinearLayout ll_facebook, ll_twitter, ll_whatsaap, ll_email, ll_google_plus, ll_sms;
    private ProgressDialog pDialog;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;
    private String partnerZkey;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.share_fragment, container, false);

        initView();
        return view;
    }

    private void initView() {

        mPrefs = getActivity().getSharedPreferences("Zoogol", getActivity().MODE_PRIVATE);
        partnerZkey = mPrefs.getString("zkey", "");

        HomeActivity.rl_topbar.setVisibility(View.VISIBLE);

        ll_facebook = (LinearLayout) view.findViewById(R.id.ll_facebook);
        ll_facebook.setOnClickListener(this);

        ll_google_plus = (LinearLayout) view.findViewById(R.id.ll_google_plus);
        ll_google_plus.setOnClickListener(this);

        ll_twitter = (LinearLayout) view.findViewById(R.id.ll_twitter);
        ll_twitter.setOnClickListener(this);

        ll_whatsaap = (LinearLayout) view.findViewById(R.id.ll_whatsaap);
        ll_whatsaap.setOnClickListener(this);

        ll_email = (LinearLayout) view.findViewById(R.id.ll_email);
        ll_email.setOnClickListener(this);

        ll_sms = (LinearLayout) view.findViewById(R.id.ll_sms);
        ll_sms.setOnClickListener(this);

        iv_navigationDrawer = (ImageView) view.findViewById(R.id.iv_navigationDrawer);
        iv_navigationDrawer.setOnClickListener(this);

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

            case R.id.ll_facebook:

                String urlToShare = "https://www.zoogol.in/cashback/" + partnerZkey;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
                intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                // See if official Facebook app is found
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }
                // As fallback, launch sharer.php in a browser
                if (!facebookAppFound) {
                    String sharerUrl = "https://www.zoogol.in/cashback/ZMPDIN0400";
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
                break;

            case R.id.ll_twitter:

                // Create intent using ACTION_VIEW and a normal Twitter url:
                String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                        urlEncode("Tweet text"),
                        urlEncode("https://www.zoogol.in/cashback/" + partnerZkey));
                Intent mIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
                List<ResolveInfo> matches1 = getActivity().getPackageManager().queryIntentActivities(mIntent1, 0);
                for (ResolveInfo info : matches1) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                        mIntent1.setPackage(info.activityInfo.packageName);
                    }
                }
                startActivity(mIntent1);

                break;

            case R.id.ll_whatsaap:
                PackageManager pm = getActivity().getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "https://www.zoogol.in/cashback/" + partnerZkey;

                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_email:

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                //sendIntent.setData(Uri.parse("test@gmail.com"));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                //sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "test@gmail.com" });
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Zoogol Sharing");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi Friend\n" +
                        "\n" +
                        "Hey Dear ! I came across an awesome website with an awesome idea. The name is Zoogol. Whatever you buy through Zoogol, you can make it free. Yes ! Through a very simple and logical process you can actually earn 111% Cashback on almost Everything. It made sense to me. Try for yourself, visit www.zoogol.in. I fell in love with it, am sure you'd like it too.\n" +
                        "\n" +
                        "Click here https://www.zoogol.in/cashback/" + partnerZkey + " and earn cashbacks every time you shop online or instore. Its easy and Free, so sign up now to get awesome rewards for shopping.\n" +
                        "\n" +
                        "Try it Now !!!\n" +
                        "Zoogol Team");
                startActivity(sendIntent);

                break;

            case R.id.ll_google_plus:

                try {
                    Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                            .setText("https://www.zoogol.in/cashback/" + partnerZkey)
                            .setType("text/plain")
                            .getIntent()
                            .setPackage("com.google.android.apps.plus");
                    startActivity(shareIntent);
                } catch (Exception e) {
                }
                break;

            case R.id.iv_navigationDrawer:

                HomeActivity.mDrawerLayout.openDrawer(HomeActivity.ll_drawer_list);

                break;

            case R.id.ll_sms:

                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                SendSmsFragment sendSmsFragment = new SendSmsFragment();
                HomeActivity.shareFragmentStack.add(sendSmsFragment);
                mFragmentTransaction.replace(R.id.containerRelativeLayout, sendSmsFragment);
                mFragmentTransaction.commit();
                break;


        }
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
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