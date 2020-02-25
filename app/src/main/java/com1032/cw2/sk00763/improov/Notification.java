package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 14/02/2020.
 */

public class Notification {
    private String notificationId = null;
    private String date = null;
    private String dateofrequest = null;
    private String from = null;
    private String hour = null;
    private String program = null;
    private String type = null;
    private String pending = null;
    private String programid = null;
    private String howlong = null;
    private Long topay = null;

    public Notification() {

    }

    public Notification(String notificationId, String date, String dateofrequest, String from, String hour, String program, String type, String pending,
                        String programid, String howlong, Long topay) {
        this.notificationId = notificationId;
        this.date = date;
        this.dateofrequest = dateofrequest;
        this.from = from;
        this.hour = hour;
        this.program = program;
        this.type = type;
        this.pending = pending;
        this.programid = programid;
        this.howlong = howlong;
        this.topay = topay;
    }

    public String getNotificationId() {
        return this.notificationId;
    }

    public String getDate() {
        return this.date;
    }

    public String getDateofrequest() {
        return this.dateofrequest;
    }

    public String getFrom() {
        return this.from;
    }

    public String getHour() {
        return this.hour;
    }

    public String getProgram() {
        return this.program;
    }

    public String getType() {
        return this.type;
    }

    public String getPending() {
        return this.pending;
    }

    public String getProgramid() {
        return this.programid;
    }

    public String getHowlong() {
        return this.howlong;
    }

    public Long getTopay() {
        return this.topay;
    }
}
