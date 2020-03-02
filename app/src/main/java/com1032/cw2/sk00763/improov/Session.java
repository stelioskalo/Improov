package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 01/03/2020.
 */

public class Session {
    private String coach = null;
    private String student = null;
    private String program = null;
    private String paid = null;
    private String sessionId = null;
    private String date = null;
    private String howlong = null;

    public Session() {

    }

    public Session(String coach, String student, String program, String paid, String sessionId, String date, String howlong) {
        this.coach = coach;
        this.student = student;
        this.program = program;
        this.paid = paid;
        this.sessionId = sessionId;
        this.date = date;
        this.howlong = howlong;
    }

    public String getCoach() {
        return this.coach;
    }

    public String getStudent() {
        return this.student;
    }

    public String getProgram() {
        return this.program;
    }

    public String getPaid() {
        return this.paid;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getDate() {
        return this.date;
    }

    public String getHowlong() {
        return this.howlong;
    }
}
