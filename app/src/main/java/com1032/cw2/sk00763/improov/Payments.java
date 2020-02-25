package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 25/02/2020.
 */

public class Payments {
    private String coach = null;
    private String coachpaid = null;
    private String customer = null;
    private String howmuch = null;

    public Payments() {

    }

    public Payments(String coach, String coachpaid, String customer, String howmuch) {
        this.coach = coach;
        this.coachpaid = coachpaid;
        this.customer = customer;
        this.howmuch = howmuch;
    }

    public String getCoach() {
        return this.coach;
    }

    public String getCoachpaid() {
        return this.coachpaid;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getHowmuch() {
        return this.howmuch;
    }
}
