package com.mandeep.zoogol.user_profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.InstoreCityDataBean;
import com.mandeep.zoogol.bean.UntracePurchaseBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mandeep on 5/22/2016.
 */
public class UntraceAdapter extends ArrayAdapter<UntracePurchaseBean> {
    private final Context context;
    private ArrayList<UntracePurchaseBean> items;
    private boolean deleteIconVisible;

    public UntraceAdapter(Context context, ArrayList<UntracePurchaseBean> items) {
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
        View view = inflater.inflate(R.layout.untrace_items, parent, false);

        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        TextView tv_website = (TextView) view.findViewById(R.id.tv_website);
        TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
        TextView tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        TextView tv_status = (TextView) view.findViewById(R.id.tv_status);

        tv_date.setText(items.get(position).getTime());
        tv_website.setText(items.get(position).getBillno());
        tv_description.setText(items.get(position).getRemarks());
        tv_amount.setText(items.get(position).getAmount());
        tv_status.setText(items.get(position).getStatus());

        return view;
    }
}
