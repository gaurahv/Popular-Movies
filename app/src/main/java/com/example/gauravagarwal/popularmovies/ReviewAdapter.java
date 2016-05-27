package com.example.gauravagarwal.popularmovies;

/**
 * Created by GAURAV AGARWAL on 26-05-2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private ArrayList<Review> reviewList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.author.setText(review.getAuthor());
        holder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView review;

        public MyViewHolder(View view) {
            super(view);
            author = (TextView)view.findViewById(R.id.author_text_view);
            review = (TextView)view.findViewById(R.id.review_text_view);
        }
    }

    public ReviewAdapter(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
