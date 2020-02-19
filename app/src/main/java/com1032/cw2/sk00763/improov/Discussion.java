package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 10/12/2019.
 */

public class Discussion {

    private String about = null;
    private String topic = null;
    private String creatorid = null;
    private String creatorname = null;
    private String discussionId = null;


    public Discussion() {
    }

    public Discussion(String about, String topic, String creatorid, String creatorname, String discussionId) {
        this.about = about;
        this.topic = topic;
        this.creatorid = creatorid;
        this.creatorname = creatorname;
        this.discussionId = discussionId;
    }

    public String getAbout() {
        return this.about;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getDiscussionId() {
        return this.discussionId;
    }

    public String getCreatorname() {
        return this.creatorname;
    }

    public String getCreatorid() {
        return this.creatorid;
    }
}
