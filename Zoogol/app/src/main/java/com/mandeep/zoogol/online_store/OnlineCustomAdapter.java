package com.mandeep.zoogol.online_store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.InstoreDataBean;
import com.mandeep.zoogol.bean.OnlineStoresBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mandeep on 3/29/2016.
 */
public class OnlineCustomAdapter extends ArrayAdapter<OnlineStoresBean> {
    private final Context context;
    private ArrayList<OnlineStoresBean> items;
    private boolean deleteIconVisible;

    public OnlineCustomAdapter(Context context, ArrayList<OnlineStoresBean> items) {
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
        View view = inflater.inflate(R.layout.online_list_item, parent, false);

        ImageView iv_placeholder = (ImageView) view.findViewById(R.id.iv_placeholder);
        TextView tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);

        tv_city_name.setText(items.get(position).getSite_name());
        Picasso.with(context).load("https://www.zoogol.in/newz/images/online_logo/" + items.get(position).getLogo()).into(iv_placeholder);
        return view;
    }
}
