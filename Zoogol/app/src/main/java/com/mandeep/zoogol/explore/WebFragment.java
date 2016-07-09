package com.mandeep.zoogol.explore;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;

/**
 * Created by mandeep on 5/4/2016.
 */
public class WebFragment extends Fragment {

    private WebView webview;
    private View view;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_fragment, container, false);


        initView();

        return view;
    }

    private void initView() {
        HomeActivity.rl_topbar.setVisibility(View.GONE);
        webview = (WebView) view.findViewById(R.id.webview);

        webview.loadUrl(ExploreMainFragment.urlToLoad);

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