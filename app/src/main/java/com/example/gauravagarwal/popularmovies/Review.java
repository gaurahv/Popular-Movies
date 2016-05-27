package com.example.gauravagarwal.popularmovies;

/**
 * Created by GAURAV AGARWAL on 26-05-2016.
 */
public class Review {
    private String review;
    private String author;

    public Review(String review, String author) {
        this.review = review;
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
