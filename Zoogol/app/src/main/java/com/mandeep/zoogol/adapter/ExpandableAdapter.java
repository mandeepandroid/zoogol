package com.mandeep.zoogol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.Bean;

import java.util.ArrayList;


public class ExpandableAdapter extends BaseExpandableListAdapter {

    ArrayList<Bean> al_data = new ArrayList<Bean>();
    Context context;

    public ExpandableAdapter(ArrayList<Bean> al_data, Context context) {
        this.al_data = al_data;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return al_data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return al_data.get(groupPosition).getAl_subcategory().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return al_data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return al_data.get(groupPosition).getAl_subcategory().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.headerlayout, null);

        }

        ImageView iv_placeholder=(ImageView)convertView.findViewById(R.id.iv_placeholder);
        iv_placeholder.setImageResource(al_data.get(groupPosition).getPlaceholder());
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_header);
        tv_time.setText(al_data.get(groupPosition).getStr_name());


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childlayout, null);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_child);
        tv_name.setText(al_data.get(groupPosition).getAl_subcategory().get(childPosition).getStr_categoryname());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
