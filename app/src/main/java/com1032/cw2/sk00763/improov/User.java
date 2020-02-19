package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 28/10/2019.
 */

public class User {
    private String name = null;
    private String surname = null;
    private String bio = null;
    private String qualifications = null;
    private String image = null;
    private String email = null;
    private String id = null;
    private String topic1 = null;
    private String topic2 = null;
    private String topic3 = null;
    private String topic4 = null;
    private String topic5 = null;
    private String firsttime = null;
    private String usertype = null;
    private String topicpic1 = null;
    private String topicpic2 = null;
    private String topicpic3 = null;
    private String topicpic4 = null;
    private String topicpic5 = null;

    public User(){
        super();
    }

    public User(String name, String surname, String bio, String qualifications, String image, String email, String id, String topic1, String topic2, String topic3,
                String topic4, String topic5, String firsttime, String usertype, String topicpic1, String topicpic2, String topicpic3, String topicpic4, String topicpic5) {
        this.name = name;
        this.surname = surname;
        this.bio = bio;
        this.qualifications = qualifications;
        this.image = image;
        this.email = email;
        this.id = id;
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.topic3 = topic3;
        this.topic4 = topic4;
        this.topic5 = topic5;
        this.firsttime = firsttime;
        this.usertype = usertype;
        this.topicpic1 = topicpic1;
        this.topicpic2 = topicpic2;
        this.topicpic3 = topicpic3;
        this.topicpic4 = topicpic4;
        this.topicpic5 = topicpic5;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getBio() {
        return this.bio;
    }

    public String getImage() {
        return this.image;
    }

    public String getEmail() {
        return this.email;
    }

    public String getId() {
        return this.id;
    }

    public String getTopic1() {
        return this.topic1;
    }

    public String getTopic2() {
        return this.topic2;
    }

    public String getTopic3() {
        return this.topic3;
    }

    public String getTopic4() {
        return this.topic4;
    }

    public String getTopic5() {
        return this.topic5;
    }

    public String getFirsttime() {
        return this.firsttime;
    }

    public String getUsertype() {
        return this.usertype;
    }

    public String getTopicpic1() {
        return this.topicpic1;
    }

    public String getTopicpic2() {
        return this.topicpic2;
    }

    public String getTopicpic3() {
        return this.topicpic3;
    }

    public String getTopicpic4() {
        return this.topicpic4;
    }

    public String getTopicpic5() {
        return this.topicpic5;
    }

    public String getQualifications() {
        return this.qualifications;
    }
}


