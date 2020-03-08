package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 10/12/2019.
 */

public class Discussion {

    private String about = null;
    private String topic = null;
    private String creatorid = null;
    private String creatorname = null;


    public Discussion() {
    }

    public Discussion(String about, String topic, String creatorid, String creatorname) {
        this.about = about;
        this.topic = topic;
        this.creatorid = creatorid;
        this.creatorname = creatorname;
    }

    public String getAbout() {
        return this.about;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getCreatorname() {
        return this.creatorname;
    }

    public String getCreatorid() {
        return this.creatorid;
    }
}
