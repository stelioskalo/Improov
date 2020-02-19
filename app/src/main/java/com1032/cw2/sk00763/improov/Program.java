package com1032.cw2.sk00763.improov;

/**
 * Created by Stelios on 20/12/2019.
 */

public class Program  {

    private String coach = null;
    private String description = null;
    private String hourRate = null;
    private String monthRate = null;
    private String name = null;
    private String topic = null;
    private String programImage = null;
    private String programId = null;

    public Program() {
    }

    public Program(String coach, String description, String hourRate, String monthRate, String name, String topic, String programImage, String programId) {
        this.coach = coach;
        this.description = description;
        this.hourRate = hourRate;
        this.monthRate = monthRate;
        this.name = name;
        this.topic = topic;
        this.programImage = programImage;
        this.programId = programId;
    }

    public String getCoach() {
        return this.coach;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHourRate() {
        return this.hourRate;
    }

    public String getMonthRate() {
        return this.monthRate;
    }

    public String getName() {
        return this.name;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getProgramImage() {
        return this.programImage;
    }

    public String getProgramId() {
        return this.programId;
    }
}
