package com.mandeep.zoogol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.Data;
import com.mandeep.zoogol.interfaces.OnPlayClick;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mandeep on 6/7/2016.
 */
public class CustomAdapterPhotos extends BaseAdapter {


    private Context mcContext;
    private ArrayList<Data> mDataBeen = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public CustomAdapterPhotos(Context mcContext, ArrayList<Data> mDataBeen) {
        this.mcContext = mcContext;
        this.mDataBeen = mDataBeen;

        layoutInflater = LayoutInflater.from(mcContext);
    }

    @Override
    public int getCount() {
        return mDataBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolders viewHolders = null;
        if (convertView == null) {
            viewHolders = new ViewHolders();
            convertView = layoutInflater.inflate(R.layout.adapter_photos, null);
            viewHolders.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHolders.txt_category = (TextView) convertView.findViewById(R.id.txt_category);
            viewHolders.txt_subCategory = (TextView) convertView.findViewById(R.id.txt_subCategory);
            convertView.setTag(viewHolders);
        } else {
            viewHolders = (ViewHolders) convertView.getTag();
        }

        Picasso.with(mcContext).load("https://www.zoogol.in/images/zvm-final-slider/"+mDataBeen.get(position).getImage_name())

                .into(viewHolders.photo);
        viewHolders.txt_category.setText(mDataBeen.get(position).getCategory());
        viewHolders.txt_subCategory.setText(mDataBeen.get(position).getSub_category());

        return convertView;
    }

    private class ViewHolders {
        private ImageView photo;
        private TextView txt_category,txt_subCategory;
    }
}