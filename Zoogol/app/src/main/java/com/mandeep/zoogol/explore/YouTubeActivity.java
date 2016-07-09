
package com.mandeep.zoogol.explore;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.youtube.player.YouTubePlayer;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.adapter.CustomAdapterYouTube;
import com.mandeep.zoogol.app.AppController;
import com.mandeep.zoogol.bean.Data;
import com.mandeep.zoogol.bean.OnlineStoresBean;
import com.mandeep.zoogol.interfaces.OnPlayClick;
import com.mandeep.zoogol.online_store.OnlineCustomAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class YouTubeActivity extends Fragment {
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String TAG = YouTubeActivity.class.getSimpleName();

    private ArrayList<Data> mDataBean = new ArrayList<>();
    private ListView listVideo;
    private CustomAdapterYouTube customAdapterYouTube;
    YouTubePlayer.PlayerStyle playerStyle;

    boolean showAudioUi;
    boolean showFadeAnim;

    private static String VIDEO_ID = "iS1g8G_njx8";

    private View view;
    private ProgressDialog pDialog;
    public static Data itemClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_youtube, container, false);


        initViews();
        makeJsonRquest();
        return view;
    }


    private void initViews() {

        HomeActivity.rl_topbar.setVisibility(View.GONE);
        listVideo = (ListView) view.findViewById(R.id.listVideo1);



      /*  listVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_LONG).show();
                itemClicked=(Data)listVideo.getAdapter().getItem(position);
                startActivity(new Intent(getActivity(),YouTubeDetailActivity.class));
            }
        });*/
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

    private void makeJsonRquest() {

        showProgressDialog();
        String category_url = "https://www.zoogol.in/newz/api/video.php?appid=1f0914b48231e96562edd51395dd0bbb";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideProgressDialog();
                try {
                    String result = response.getString("result");
                    if (result.equals("true")) {

                        JSONArray json_data = response.getJSONArray("data");
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject json_dataobject = json_data.getJSONObject(i);
                            Data obj_bean_data = new Data();
                            obj_bean_data.setId(json_dataobject.getString("id"));
                            obj_bean_data.setTitle(json_dataobject.getString("title"));
                            obj_bean_data.setDiscription(json_dataobject.getString("discription"));

                            String link = json_dataobject.getString("link");
                            link = link.substring(link.lastIndexOf("/"), link.length());
                            link = link.replace("/", "");
                            obj_bean_data.setLink(link);

                            mDataBean.add(obj_bean_data);

                        }
                        customAdapterYouTube = new CustomAdapterYouTube(getActivity(), mDataBean);
                        listVideo.setAdapter(customAdapterYouTube);

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


}
