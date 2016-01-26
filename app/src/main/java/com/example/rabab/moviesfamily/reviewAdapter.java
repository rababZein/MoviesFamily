package com.example.rabab.moviesfamily;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rabab on 18/12/15.
 */



public class reviewAdapter extends ArrayAdapter<ReviewItem> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ReviewItem> mGridData = new ArrayList<ReviewItem>();

    public reviewAdapter(Context mContext, int layoutResourceId, ArrayList<ReviewItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.mGridData = mGridData;
    }



    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<ReviewItem> mGridData) {
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
            holder.authersTextView = (TextView) row.findViewById(R.id.authers);
            holder.contentTextView = (TextView) row.findViewById(R.id.content);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ReviewItem item = mGridData.get(position);
        holder.contentTextView.setText(item.getContent());
        holder.authersTextView.setText(Html.fromHtml(item.getAuthor()));
        return row;
    }

    static class ViewHolder {
        TextView authersTextView;
        TextView contentTextView;

    }
}