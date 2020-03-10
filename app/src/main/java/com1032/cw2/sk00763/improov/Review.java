package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 10/03/2020.
 */

public class Review {
    private String from = null;
    private String message = null;
    private String stars = null;

    public Review(){

    }

    public Review(String from, String message, String stars) {
        this.from = from;
        this.message = message;
        this.stars = stars;
    }

    public String getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStars() {
        return this.stars;
    }
}
