package com.example.gauravagarwal.popularmovies;

/**
 * Created by GAURAV AGARWAL on 25-05-2016.
 */
public class Trailer {
    private String name;
    private String src;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Trailer() {

    }

    public Trailer(String name, String src) {
        this.name = name;
        this.src = src;
    }
}

