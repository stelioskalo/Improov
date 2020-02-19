package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 16/02/2020.
 */

public class Message {
    private String message = null;
    private String receiver = null;
    private String sender = null;
    private String time = null;
    private String senderName = null;
    private String date = null;

    public Message() {

    }

    public Message(String message, String receiver, String sender, String time, String senderName, String date) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.time = time;
        this.senderName = senderName;
        this.date = null;
    }

    public String getMessage() {
        return this.message;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public String getSender() {
        return this.sender;
    }

    public String getTime() {
        return this.time;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public String getDate() {
        return this.date;
    }
}
