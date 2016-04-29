package com.example.gauravagarwal.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by GAURAV AGARWAL on 26-04-2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Movie> mThumbIds;

    public ImageAdapter(Context c, ArrayList<Movie> mThumbIds) {
        mContext = c;
        this.mThumbIds = mThumbIds;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Movie getItem(int position) {
        return mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(mThumbIds.get(position).posterUrl).into(imageView);
        return imageView;
    }


    public void setArray(ArrayList<Movie> array) {
        this.mThumbIds = array;
    }
}