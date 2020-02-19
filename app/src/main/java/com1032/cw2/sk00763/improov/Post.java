package com1032.cw2.sk00763.improov;

import android.util.Log;

/**
 * Created by Stelios on 10/12/2019.
 */

public class Post {

    private String post = null;
    private String postuser = null;

    public Post() {
    }

    public Post(String post, String postuser) {
        this.post = post;
        this.postuser = postuser;
    }

    public String getPost() {
        return this.post;
    }

    public String getPostuser() {
        return this.postuser;
    }
}
