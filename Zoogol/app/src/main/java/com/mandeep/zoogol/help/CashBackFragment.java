package com.mandeep.zoogol.help;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;

/**
 * Created by mandeep on 4/16/2016.
 */
public class CashBackFragment extends Fragment {

    private WebView webview;
    private View view;
    private ProgressDialog pDialog;
    private ImageView iv_navigationDrawer;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_fragment, container, false);


        initView();

        return view;
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


        webview = (WebView) view.findViewById(R.id.webview);

        webview.loadUrl("https://www.zoogol.in/newz/app/cash-back-tips.php");

        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("WebView", "onPageStarted " + url);

                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "onPageFinished " + url);

                pDialog.dismiss();
            }
        });
    }

}