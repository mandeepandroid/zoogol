package com.mandeep.zoogol.adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandeep.zoogol.R;
import com.mandeep.zoogol.bean.Data;
import com.mandeep.zoogol.enums.Quality;
import com.mandeep.zoogol.explore.YouTubeActivity;
import com.mandeep.zoogol.explore.YouTubeDetailActivity;
import com.mandeep.zoogol.explore.YouTubeThumbnail;
import com.mandeep.zoogol.interfaces.OnPlayClick;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by mandeep on 6/7/2016.
 */
public class CustomAdapterYouTube extends BaseAdapter {

    private OnPlayClick onPlayClick;
    private Context mcContext;
    private ArrayList<Data> mDataBeen = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public CustomAdapterYouTube(Context mcContext, ArrayList<Data> mDataBeen) {
        this.mcContext = mcContext;
        this.mDataBeen = mDataBeen;
        this.onPlayClick = onPlayClick;
        layoutInflater = LayoutInflater.from(mcContext);
    }

    @Override
    public int getCount() {
        return mDataBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataBeen.get(position);
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
            convertView = layoutInflater.inflate(R.layout.adapter_youtube, null);
            viewHolders.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolders.play_bt = (ImageButton) convertView.findViewById(R.id.play_bt);
            viewHolders.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolders.txt_description = (TextView) convertView.findViewById(R.id.txt_description);
            convertView.setTag(viewHolders);
        } else {
            viewHolders = (ViewHolders) convertView.getTag();
        }
        viewHolders.txt_title.setText(mDataBeen.get(position).getTitle());
        viewHolders.txt_description.setText(mDataBeen.get(position).getDiscription());

        Picasso.with(mcContext).load(YouTubeThumbnail.getUrlFromVideoId(mDataBeen.get(position).getLink(), Quality.HIGH))
                .fit().centerCrop().into(viewHolders.thumbnail);

        viewHolders.play_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YouTubeActivity.itemClicked = mDataBeen.get(position);
                Intent mIntent = new Intent(mcContext, YouTubeDetailActivity.class);
                mcContext.startActivity(mIntent);
            }
        });

        return convertView;
    }

    private class ViewHolders {
        private ImageButton play_bt;
        private ImageView thumbnail;
        private TextView txt_title, txt_description;
    }
}
