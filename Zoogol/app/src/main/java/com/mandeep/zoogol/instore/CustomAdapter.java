package com.mandeep.zoogol.instore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.InstoreDataBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mandeep on 3/29/2016.
 */
public class CustomAdapter extends ArrayAdapter<InstoreDataBean> {
    private final Context context;
    private ArrayList<InstoreDataBean> items;
    private boolean deleteIconVisible;

    public CustomAdapter(Context context, ArrayList<InstoreDataBean> items) {
        super(context,0, items);

        this.context = context;
        this.items = items;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public View getView(final int position, View convertView, ViewGroup parent){


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.instore_items, parent, false);

        ImageView iv_placeholder=(ImageView)view.findViewById(R.id.iv_placeholder);
        TextView tv_city_name=(TextView)view.findViewById(R.id.tv_city_name);

        tv_city_name.setText(items.get(position).getCity());
        Picasso.with(context).load(items.get(position).getImage()).into(iv_placeholder);
        return view;
    }
}
