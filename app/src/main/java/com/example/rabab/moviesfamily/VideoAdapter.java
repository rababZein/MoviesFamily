package com.example.rabab.moviesfamily;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rabab on 18/12/15.
 */



public class VideoAdapter extends ArrayAdapter<VideoItem> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<VideoItem> mGridData = new ArrayList<VideoItem>();
    private VideoItem item;

    public VideoAdapter( Context mContext, int layoutResourceId, ArrayList<VideoItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<VideoItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.urlTextView = (TextView) row.findViewById(R.id.videoUrl);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        item = mGridData.get(position);
        holder.urlTextView.setText(item.getName());
        holder.urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v="+item.getKey()));
                mContext.startActivity(intent);
            }
        });

        return row;
    }

    static class ViewHolder {
        TextView urlTextView;


    }
}