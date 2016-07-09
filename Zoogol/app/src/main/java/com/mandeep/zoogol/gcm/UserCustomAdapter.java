package com.mandeep.zoogol.gcm;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mandeep.zoogol.HomeActivity;
import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.NotificationBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mandeep on 6/14/2016.
 */
public class UserCustomAdapter extends ArrayAdapter<NotificationBean> {
    private final Context context;
    private ArrayList<NotificationBean> items;
    private boolean deleteIconVisible;

    public UserCustomAdapter(Context context, ArrayList<NotificationBean> items) {
        super(context, 0, items);

        this.context = context;
        this.items = items;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_items_adapter, parent, false);

        final TextView notification_text = (TextView) view.findViewById(R.id.notification_text);

        notification_text.setText(Html.fromHtml(items.get(position).getMessage()));
        if (items.get(position).getReadstatus().equals("1")) {

            notification_text.setTextColor(Color.parseColor("#000000"));
        } else {
            notification_text.setTextColor(Color.parseColor("#c0c0c0"));
        }

        notification_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeNotificationUnread(items.get(position));
                HomeActivity.gernalNotificationBean.get(position).setReadstatus("0");
                items.get(position).setReadstatus("0");
                notification_text.setTextColor(Color.parseColor("#c0c0c0"));
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void makeNotificationUnread(NotificationBean NotificationValue) {


        String notifiactionURL = "https://www.zoogol.in/newz/api/notification-read-status.php?appid=1f0914b48231e96562edd51395dd0bbb" +
                "&zkey=" + NotificationValue.getZkey() + "&rdstatus=" + NotificationValue.getId() + "&newnoti=";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, notifiactionURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    String result = response.getString("result");
                    if (result.equals("true")) {


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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        jsonObjReq.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(jsonObjReq);
    }
}
