package com.mandeep.zoogol.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.mandeep.zoogol.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends FragmentActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_activity);

        pager = (ViewPager) findViewById(R.id.viewpager);
        view1 = (View) findViewById(R.id.view1);
        view1.setBackgroundResource(R.drawable.listshade);
        view2 = (View) findViewById(R.id.view2);
        view3 = (View) findViewById(R.id.view3);

        rl_first = (RelativeLayout) findViewById(R.id.rl_first);
        rl_second = (RelativeLayout) findViewById(R.id.rl_second);

        rl_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                //finish();429868
            }
        });

        rl_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
                //finish();429868
            }
        });

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
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
        fList.add(new FirstFragment());
        fList.add(new SecondFragment());
        fList.add(new ThirdFragment());
        //fList.add(new FirstFragment());
        return fList;
    }


}
