package com.example.gauravagarwal.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GAURAV AGARWAL on 25-05-2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {
    private ArrayList<Trailer> trailerList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        holder.title.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton imageButton;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public TrailerAdapter(ArrayList<Trailer> trailerList) {
        this.trailerList = trailerList;
    }
}
